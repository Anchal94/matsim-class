package eventHandling;

import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;

public class TripDistanceAnalysis {
	
	public static void main(String[] args) {
		
		String eventsFile =  "/Users/amit/Documents/matsimClass/data_nemo/run255.output_events.xml.gz";
		String networkFile =  "/Users/amit/Documents/matsimClass/data_nemo/run255.output_network.xml.gz";
		
		EventsManager eventsManager = EventsUtils.createEventsManager();
		
		Network network = NetworkUtils.createNetwork();
		new MatsimNetworkReader(network).readFile(networkFile);
		
		TripDistanceEventHandler tripDistanceHandler = new TripDistanceEventHandler(network);
		
		eventsManager.addHandler(tripDistanceHandler);
		
		MatsimEventsReader reader = new MatsimEventsReader(eventsManager);
		
		reader.readFile(eventsFile);
		
		double avgTripDistance = tripDistanceHandler.getAverageTripDistance();

		System.out.println("The average trip distance is "+ avgTripDistance + " in m.");
	}

}
