package eventHandling;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.PersonArrivalEvent;
import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.events.PersonStuckEvent;
import org.matsim.api.core.v01.events.VehicleEntersTrafficEvent;
import org.matsim.api.core.v01.events.VehicleLeavesTrafficEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.events.handler.PersonArrivalEventHandler;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;
import org.matsim.api.core.v01.events.handler.PersonStuckEventHandler;
import org.matsim.api.core.v01.events.handler.VehicleEntersTrafficEventHandler;
import org.matsim.api.core.v01.events.handler.VehicleLeavesTrafficEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.api.experimental.events.TeleportationArrivalEvent;
import org.matsim.core.api.experimental.events.handler.TeleportationArrivalEventHandler;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.vehicles.Vehicle;
import org.opengis.feature.simple.SimpleFeature;

public class HomeworkSample {

	private static final String dir = "/Users/amit/Documents/matsimClass/ha1/";

    public static void main(String[] args) {

        String eventsFile = dir + "task1/run255.output_events.xml.gz";
        String networkFile = dir + "task1/run255.output_network.xml.gz";

        Network network = loadScenarioFromNetwork(networkFile).getNetwork();

        new HomeworkSample().task1a(eventsFile, network, dir + "task1/result_ha1a");
        new HomeworkSample().task2a(eventsFile, network, dir + "task2/result_ha2a", dir+"task2/Essen_EPSG-25832.shp");
        new HomeworkSample().task1b(eventsFile, network, dir + "task1/result_ha1b");
        new HomeworkSample().task2b(eventsFile, network, dir + "task2/result_ha2b", dir+"task2/Essen_EPSG-25832.shp");
    }

    private void task1b(String eventFile, Network network, String outFile){ //for nrw, dur bins and for mode
        {
            EventsManager eventsManager = EventsUtils.createEventsManager();
            TripDurationHandler handler = new TripDurationHandler(network, null);
            eventsManager.addHandler(handler);
            new MatsimEventsReader(eventsManager).readFile(eventFile);
            writeData(handler.getDurationsToTrips(), outFile+".txt");
        }
        {
            EventsManager eventsManager = EventsUtils.createEventsManager();
            TripDurationHandler handler = new TripDurationHandler(network, null);
            eventsManager.addHandler(handler);
            new MatsimEventsReader(eventsManager).readFile(eventFile);
            writeData(handler.getModeToDurations(), outFile+"_mode.txt");
        }
    }

    private void task2b(String eventFile, Network network, String outFile, String essenShapeFile){//for essen, dur bins and for mode
        {
            EventsManager eventsManager = EventsUtils.createEventsManager();
            TripDurationHandler handler = new TripDurationHandler(network, essenShapeFile);
            eventsManager.addHandler(handler);
            new MatsimEventsReader(eventsManager).readFile(eventFile);
            writeData(handler.getDurationsToTrips(), outFile+".txt");
        }
        {
            EventsManager eventsManager = EventsUtils.createEventsManager();
            TripDurationHandler handler = new TripDurationHandler(network, essenShapeFile);
            eventsManager.addHandler(handler);
            new MatsimEventsReader(eventsManager).readFile(eventFile);
            writeData(handler.getModeToDurations(), outFile+"_mode.txt");
        }
    }


    private void task1a(String eventFile, Network network, String outFile){ //for nrw, dist bins and for mode
        {
            EventsManager eventsManager = EventsUtils.createEventsManager();
            TripDistanceHandler handler = new TripDistanceHandler(network, null);
            eventsManager.addHandler(handler);
            new MatsimEventsReader(eventsManager).readFile(eventFile);
            writeData(handler.getDistanceBinsToTrips(), outFile+".txt");
        }
        {
            EventsManager eventsManager = EventsUtils.createEventsManager();
            TripDistanceHandler handler = new TripDistanceHandler(network, null);
            eventsManager.addHandler(handler);
            new MatsimEventsReader(eventsManager).readFile(eventFile);
            writeData(handler.getModeToTrips(), outFile+"_mode.txt");
        }
    }

    private void task2a(String eventFile, Network network, String outFile, String essenShapeFile){//for essen, dist bins and for mode
        {
            EventsManager eventsManager = EventsUtils.createEventsManager();
            TripDistanceHandler handler = new TripDistanceHandler(network, essenShapeFile);
            eventsManager.addHandler(handler);
            new MatsimEventsReader(eventsManager).readFile(eventFile);
            writeData(handler.getDistanceBinsToTrips(), outFile+".txt");
        }
        {
            EventsManager eventsManager = EventsUtils.createEventsManager();
            TripDistanceHandler handler = new TripDistanceHandler(network, essenShapeFile);
            eventsManager.addHandler(handler);
            new MatsimEventsReader(eventsManager).readFile(eventFile);
            writeData(handler.getModeToTrips(), outFile+"_mode.txt");
        }
    }

    private static void writeData(Map<String, List<Double>> data, String outFile){
        try (BufferedWriter writer = IOUtils.getBufferedWriter(outFile)) {
            writer.write("category\tcount\tavgValue\n");
            for (Map.Entry<String, List<Double>> bin : data.entrySet()) {
                writer.write(bin.getKey()+"\t"+bin.getValue().size()+"\t"+doubleMean(bin.getValue())+"\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Data is not written/read. Reason : " + e);
        }
    }

    static class TripDurationHandler implements  PersonDepartureEventHandler, PersonArrivalEventHandler, PersonStuckEventHandler {

        private final Map<Id<Person>, Double> personId2TripDurations = new HashMap<>();
        private final Map<String, List<Double>> durationsToTrips = new TreeMap<>();
        private final Map<String, List<Double>> modeToDurations = new TreeMap<>();

        private final Network network;
        private final Collection<SimpleFeature> features;

        private final Map<Id<Person>, Boolean> keepOriginatedPersons = new HashMap<>();

        TripDurationHandler(Network network, String shapeFile) {
            this.network = network;
            if (shapeFile!=null) this.features = ShapeFileReader.getAllFeatures(shapeFile);
            else this.features =null;
        }

        @Override
        public void handleEvent(PersonArrivalEvent event) {
            if ( this.keepOriginatedPersons.get(event.getPersonId())  //origin in given shape
                    || includePerson(event.getLinkId(), this.features, network)  // destination in given shape
                    ) {
                double dur = event.getTime() + this.personId2TripDurations.remove(event.getPersonId());
                updateDurationBins(dur);
                updateModeBins(dur, event.getLegMode());
            }
        }

        @Override
        public void handleEvent(PersonDepartureEvent event) {
            this.keepOriginatedPersons.put(event.getPersonId(), includePerson(event.getLinkId(), this.features, network));
            this.personId2TripDurations.put(event.getPersonId(), -event.getTime());
        }

        @Override
        public void handleEvent(PersonStuckEvent event) {
            System.out.println(event.toString());
            this.personId2TripDurations.remove(event.getPersonId());
        }

        private void updateDurationBins(double dur) {
            if (dur<300.) {
                List<Double> tripsSoFar = durationsToTrips.getOrDefault("0-5min", new ArrayList<>());
                tripsSoFar.add(dur);
                durationsToTrips.put("0-5min", tripsSoFar);
            } else if (dur<900.){
                List<Double> tripsSoFar = durationsToTrips.getOrDefault("5-15min", new ArrayList<>());
                tripsSoFar.add(dur);
                durationsToTrips.put("5-15min", tripsSoFar);
            } else if (dur<1800.){
                List<Double> tripsSoFar = durationsToTrips.getOrDefault("15-30min", new ArrayList<>());
                tripsSoFar.add(dur);
                durationsToTrips.put("15-30min", tripsSoFar);
            } else if (dur<3600.){
                List<Double> tripsSoFar = durationsToTrips.getOrDefault("30-60min", new ArrayList<>());
                tripsSoFar.add(dur);
                durationsToTrips.put("30-60min", tripsSoFar);
            } else {
                List<Double> tripsSoFar = durationsToTrips.getOrDefault("60+min", new ArrayList<>());
                tripsSoFar.add(dur);
                durationsToTrips.put("60+min", tripsSoFar);
            }
        }

        private void updateModeBins(double dist, String mode) {
            List<Double> tripsSoFar = modeToDurations.getOrDefault(mode, new ArrayList<>());
            tripsSoFar.add(dist);
            modeToDurations.put(mode, tripsSoFar);
        }

        public Map<String, List<Double>> getDurationsToTrips() {
            return durationsToTrips;
        }

        public Map<String, List<Double>> getModeToDurations() {
            return modeToDurations;
        }

    }

    static class TripDistanceHandler implements VehicleEntersTrafficEventHandler, VehicleLeavesTrafficEventHandler, LinkEnterEventHandler,
            PersonArrivalEventHandler, PersonDepartureEventHandler, TeleportationArrivalEventHandler {

        private final Map<Id<Vehicle>, Double> vehicleId2TripDistance = new HashMap<>();
        private final Map<String, List<Double>> distanceBinsToTrips = new TreeMap<>();
        private final Map<String, List<Double>> modeToDistances = new TreeMap<>();
        private final Network network;
        private final Collection<SimpleFeature> features;

        private final Map<Id<Person>, Boolean> keepOriginatedPersons = new HashMap<>();
        private Map<Id<Person>, Double> teleportationDists = new HashMap<>();

        TripDistanceHandler(Network network, String shapeFile) {
            this.network = network;
            if (shapeFile!=null) this.features = ShapeFileReader.getAllFeatures(shapeFile);
            else this.features =null;
        }

        @Override
        public void handleEvent(PersonDepartureEvent event) { // filtering of teleportation
            this.keepOriginatedPersons.put(event.getPersonId(), includePerson(event.getLinkId(), this.features, network));
        }

        @Override
        public void handleEvent(VehicleEntersTrafficEvent event) {
            //register a trip
            this.vehicleId2TripDistance.put( event.getVehicleId(), 0.);
        }

        @Override
        public void handleEvent(LinkEnterEvent event) {
            Id<Link> linkId = event.getLinkId(); // i.e. exclude departure links but include arrival links
            double distance = this.network.getLinks().get(linkId).getLength();

            this.vehicleId2TripDistance.put(event.getVehicleId(), this.vehicleId2TripDistance.get(event.getVehicleId())+distance);
        }

        @Override
        public void handleEvent(VehicleLeavesTrafficEvent event) {
            // de-register a trip-start
            if ( this.keepOriginatedPersons.get(event.getPersonId())  //origin in given shape
                    || includePerson(event.getLinkId(), this.features, network)  // destination in given shape
                    ) {
                double tripDistance = this.vehicleId2TripDistance.remove(event.getVehicleId());
                updateDistanceBins(tripDistance);
                updateModeBins(tripDistance, event.getNetworkMode());
            }
        }

        @Override
        public void handleEvent(TeleportationArrivalEvent event) {
            this.teleportationDists.put(event.getPersonId(), event.getDistance());
        }

        @Override
        public void handleEvent(PersonArrivalEvent event) {
            // only agents which are teleportated
            if (this.teleportationDists.get(event.getPersonId())!=null){
                if ( this.keepOriginatedPersons.get(event.getPersonId()) //origin in given shape
                        || includePerson(event.getLinkId(), this.features, network)  // destination in given shape
                        ) {
                    double dist = this.teleportationDists.remove(event.getPersonId());
                    updateDistanceBins(dist);
                    updateModeBins(dist, event.getLegMode());
                }
            }
        }

        private void updateDistanceBins(double dist) {
            if (dist<1000.) {
                List<Double> tripsSoFar = distanceBinsToTrips.getOrDefault("0-1km", new ArrayList<>());
                tripsSoFar.add(dist);
                distanceBinsToTrips.put("0-1km", tripsSoFar);
            } else if (dist<3000.){
                List<Double> tripsSoFar = distanceBinsToTrips.getOrDefault("1-3km", new ArrayList<>());
                tripsSoFar.add(dist);
                distanceBinsToTrips.put("1-3km", tripsSoFar);
            } else if (dist<5000.){
                List<Double> tripsSoFar = distanceBinsToTrips.getOrDefault("3-5km", new ArrayList<>());
                tripsSoFar.add(dist);
                distanceBinsToTrips.put("3-5km", tripsSoFar);
            } else if (dist<10000.){
                List<Double> tripsSoFar = distanceBinsToTrips.getOrDefault("5-10km", new ArrayList<>());
                tripsSoFar.add(dist);
                distanceBinsToTrips.put("5-10km", tripsSoFar);
            } else {
                List<Double> tripsSoFar = distanceBinsToTrips.getOrDefault("10+km", new ArrayList<>());
                tripsSoFar.add(dist);
                distanceBinsToTrips.put("10+km", tripsSoFar);
            }
        }

        private void updateModeBins(double dist, String mode) {
            List<Double> tripsSoFar = modeToDistances.getOrDefault(mode, new ArrayList<>());
            tripsSoFar.add(dist);
            modeToDistances.put(mode, tripsSoFar);
        }

        private Map<String, List<Double>> getModeToTrips() {
            return modeToDistances;
        }

        private Map<String, List<Double>> getDistanceBinsToTrips() {
            return distanceBinsToTrips;
        }
    }

    private static boolean includePerson(Id<Link> linkId, Collection<SimpleFeature> features, Network network) {
        if (features==null) return true;
        else {
            Point point = MGC.coord2Point(network.getLinks().get(linkId).getCoord());
            for(SimpleFeature sf : features){
                if ( ((Geometry)sf.getDefaultGeometry()).contains(point)) return true;
            }
        }
        return false;
    }
	
    private static Scenario loadScenarioFromNetwork(String networkFile) {
    	Config config = ConfigUtils.createConfig();
    	config.network().setInputFile(networkFile);
    	return ScenarioUtils.loadScenario(config);
    }
    
    private static double doubleMean(List<Double> in) {
    	 double sum = 0;
    	 for (double d : in) {
    		 sum+=d;
    	 }
    	 return sum/in.size();
    }
    
}
