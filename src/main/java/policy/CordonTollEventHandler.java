package policy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkLeaveEvent;
import org.matsim.api.core.v01.events.PersonMoneyEvent;
import org.matsim.api.core.v01.events.VehicleEntersTrafficEvent;
import org.matsim.api.core.v01.events.handler.LinkLeaveEventHandler;
import org.matsim.api.core.v01.events.handler.VehicleEntersTrafficEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.matsim.vehicles.Vehicle;
import org.opengis.feature.simple.SimpleFeature;

import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class CordonTollEventHandler implements LinkLeaveEventHandler, VehicleEntersTrafficEventHandler{

	private final Map<Id<Link>, Double> linksToTollPayments = new HashMap<>();
	private final Map<Id<Vehicle>, Id<Person>> vehiclesToDrivers = new HashMap<>();
	
	@Inject
	private EventsManager eventsManager;
	
	@Inject
	private Network network;
	
	// a constructor in which shape file and network are required arguments
	public CordonTollEventHandler (String shapeFile) {
		storeCrossingLinks(shapeFile);
	}
	
	@Override
	public void handleEvent(LinkLeaveEvent event) {
		if ( this.linksToTollPayments.get(event.getLinkId()) != null ) {
			Id<Person> personId = this.vehiclesToDrivers.get(event.getVehicleId());
			double toll = this.linksToTollPayments.get(event.getLinkId());
			// if it's crossing one of the stored links, throw a person money event
			PersonMoneyEvent moneyEvent = new PersonMoneyEvent(event.getTime(), personId, toll);
			this.eventsManager.processEvent(moneyEvent);
		}
	}

	private void storeCrossingLinks(String shapeFile) {
		// some filtering to identify the crossing links and tolls for each of those links
		
		Collection<SimpleFeature> allFeatures = ShapeFileReader.getAllFeatures(shapeFile);
		
		for (Link link : network.getLinks().values()) {
			
			// to node			
			boolean toNodeInsidePolygon = isCoordInsideFeatures(allFeatures, link.getToNode().getCoord());
			
			boolean fromNodeInsidePolygon = isCoordInsideFeatures(allFeatures, link.getFromNode().getCoord());
			
			if (toNodeInsidePolygon == fromNodeInsidePolygon) {
				// either both nodes inside the polygon or both nodes outside the polygon
			} else {
				this.linksToTollPayments.put(link.getId(), 0.50);
			}
		}
	}
	
	private boolean isCoordInsideFeatures(Collection<SimpleFeature> allFeatures, Coord cord) {
		Point point = MGC.coord2Point( cord );
		for (SimpleFeature feature : allFeatures) {
			  if ( ( (Geometry) feature.getDefaultGeometry()).contains(point) ) {
				  return true;
			  }
		}
		return false;
	}

	@Override
	public void handleEvent(VehicleEntersTrafficEvent event) {
		this.vehiclesToDrivers.put(event.getVehicleId(), event.getPersonId());
	}
}
