package eventHandling;

import java.util.Map;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;

public class ModalShareAnalysis {
	
	public static void main(String[] args) {
		
		String eventsFile =  "/Users/amit/Documents/matsimClass/data_nemo/run255.output_events.xml.gz";
		
		EventsManager eventsManager = EventsUtils.createEventsManager();
		
		ModalShareEventHandler modalShareEventHandler = new ModalShareEventHandler();
		
		eventsManager.addHandler(modalShareEventHandler);
		
		MatsimEventsReader reader = new MatsimEventsReader(eventsManager);
		
		reader.readFile(eventsFile);
		
		Map<String, Integer> mode2NumberOfLegs = modalShareEventHandler.getModeToNumberOfLegs();
		
		for (String mode : mode2NumberOfLegs.keySet()) {
			System.out.println("Number of legs for mode "+ mode + " are "+ mode2NumberOfLegs.get(mode));
		}
		
	}

}
