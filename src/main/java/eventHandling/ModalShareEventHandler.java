package eventHandling;

import java.util.HashMap;
import java.util.Map;

import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;

public class ModalShareEventHandler implements PersonDepartureEventHandler{

	Map<String, Integer> modeToNumberOfLegs = new HashMap<>();
	
	@Override
	public void handleEvent(PersonDepartureEvent event) {
		String mode = event.getLegMode();
//		if (this.modeToNumberOfLegs.get(mode)!=null) {
		if ( this.modeToNumberOfLegs.containsKey(mode)) {
			int countSoFar = this.modeToNumberOfLegs.get(mode);
			this.modeToNumberOfLegs.put(mode, countSoFar+1);
		} else {
			this.modeToNumberOfLegs.put(mode, 1);
		}
	}

	public Map<String, Integer> getModeToNumberOfLegs() {
		return modeToNumberOfLegs;
	}
	
	 
//	public Map<String, Integer> getModeToNumberOfLegs(){
//		return this.modeToNumberOfLegs;
//	}
}
