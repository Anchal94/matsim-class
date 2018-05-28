package pop;

import java.util.List;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationWriter;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.scenario.ScenarioUtils;

public class SelectedPlansFIlter {

	public static void main(String[] args) {
		
		String configFile = ""; // this include a plans file 
		Config config = ConfigUtils.loadConfig(configFile);
		Scenario scenario = ScenarioUtils.loadScenario(config);
		
		Population population = scenario.getPopulation();
		
		Population outputPopulation = ScenarioUtils.createScenario(ConfigUtils.createConfig()).getPopulation();
		
		for (Person person : population.getPersons().values()) {
			
			Person outPerson = outputPopulation.getFactory().createPerson(person.getId());
			
			Plan selectedPlan = person.getSelectedPlan();
			
			Plan outputPlan = outputPopulation.getFactory().createPlan();
			PopulationUtils.copyFromTo(selectedPlan, outputPlan);
			outPerson.addPlan(outputPlan);
			outputPopulation.addPerson(outPerson);
			
//			int noOfPlans = person.getPlans().size();
//			
//			for (int index =0; index < noOfPlans; index++ ) {
//				
//				Plan plan = person.getPlans().get(index);
//				
//				if ( person.getSelectedPlan().equals(plan) ) {
//					//keep it
//				} else {
//					//remove it
//				}
//				
//			}
			
			List<PlanElement> planElements = selectedPlan.getPlanElements();
			for (PlanElement pe : planElements) {
				if (pe instanceof Activity) {
					Activity act = (Activity) pe;
					
					act.getCoord();
					act.getEndTime();
					
				} else if (pe instanceof Leg) {
					Leg leg = (Leg) pe;
					
				} else {
					throw new RuntimeException("Unrecognized plan element: "+pe);
				}
			}
			
		}
		
		new PopulationWriter(outputPopulation).write(" ");
		
		
	}
	
	
}
