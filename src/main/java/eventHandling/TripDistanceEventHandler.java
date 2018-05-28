package eventHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.VehicleEntersTrafficEvent;
import org.matsim.api.core.v01.events.VehicleLeavesTrafficEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.events.handler.VehicleEntersTrafficEventHandler;
import org.matsim.api.core.v01.events.handler.VehicleLeavesTrafficEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.TeleportationArrivalEvent;
import org.matsim.core.api.experimental.events.handler.TeleportationArrivalEventHandler;
import org.matsim.vehicles.Vehicle;

public class TripDistanceEventHandler implements VehicleEntersTrafficEventHandler, LinkEnterEventHandler, 
VehicleLeavesTrafficEventHandler, TeleportationArrivalEventHandler{

	private final List<Double> tripDistance = new ArrayList<>();
	private final Map<Id<Vehicle>, Double> vehicleId2TripDistance = new HashMap<>();
	private final Network network;
	
	public TripDistanceEventHandler(Network network) {
		this.network = network;
	}
	
	@Override
	public void handleEvent(LinkEnterEvent event) {
		Id<Link> linkId = event.getLinkId();
		double distance = this.network.getLinks().get(linkId).getLength();
		
		if (this.vehicleId2TripDistance.containsKey(event.getVehicleId()) ){
			double distanceSoFar = this.vehicleId2TripDistance.get(event.getVehicleId());
			this.vehicleId2TripDistance.put(event.getVehicleId(), distance + distanceSoFar);
		} else {
			throw new RuntimeException("Trip must be registered already.");
		}
	}

	@Override
	public void handleEvent(VehicleEntersTrafficEvent event) {
		//register a trip
		this.vehicleId2TripDistance.put( event.getVehicleId(), 0.);
	}

	@Override
	public void handleEvent(VehicleLeavesTrafficEvent event) {
		// de-register a trip-start
		double tripDistance = this.vehicleId2TripDistance.remove(event.getVehicleId());
		this.tripDistance.add(tripDistance);
//		this.vehicleId2TripDistance.remove(event.getVehicleId());
	}
	
	public void getNumberOfTrips() {
		
		Map<String, List<Double>> distanceBinsToTrips = new HashMap<>();
		
		for (Double d : this.tripDistance) {
		
			if (d < 1000.) {
				List<Double> tripsSoFar = distanceBinsToTrips.get("0-1km");
				tripsSoFar.add(d);
				distanceBinsToTrips.put("0-1km", tripsSoFar);
			} else if (d < 3000.) {
				
			}
			
			
		}
	}
	
	public double getTotalDistance() {
		double sum = 0.;
		for (double dist :this.tripDistance) {
			sum += dist;
		}
		return sum;
	}
	
	public double getAverageTripDistance() {
		return getTotalDistance() / this.tripDistance.size();
	}

	@Override
	public void handleEvent(TeleportationArrivalEvent arg0) {
		// to include modes which are teleported (not simulated on network)
			this.tripDistance.add(arg0.getDistance());
	}
	

}
