package eventHandling;

import java.io.BufferedWriter;
import java.io.IOException;

import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.utils.io.IOUtils;

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
		
		String outFile = "/Users/amit/Documents/matsimClass/data_nemo/averageTripDistance.txt";
		
		
		try (BufferedWriter writer = IOUtils.getBufferedWriter(outFile))  {
//			headers
			writer.write("averageTripDistInKm");
			writer.newLine();
			writer.write(avgTripDistance/1000.+"\n");
		} catch (IOException e) {
			throw new RuntimeException("Data is not written. Reason: "+ e);
		}
		
		System.out.println("The average trip distance is "+ avgTripDistance + " in m.");
	}

}










