package pop;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.Route;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.io.PopulationWriter;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.scenario.ScenarioUtils;

public class LinksFromPlansRemover {
	
	public static void main(String[] args) {
		
		String inputPlansFile = "";
		String outputFile = "";
		
		Config config = ConfigUtils.createConfig();
		config.plans().setInputFile(inputPlansFile);
		
		Scenario scenario = ScenarioUtils.loadScenario(config);
		
		Population inputPopulation = scenario.getPopulation();
		
		for (Person person : inputPopulation.getPersons().values()) {
			
			for (Plan plan : person.getPlans()) {
				
				for (PlanElement planElement: plan.getPlanElements()) {
					
					if (planElement instanceof Activity) {
						// remove link info from the activities however, check the coord
					
						Activity act = (Activity) planElement;
						
						if (act.getCoord()!=null) {
							act.setLinkId(null);	
						} else {
							// if there is no coordinate assigned, set a coord to activity (e.g. centroid of the link, fromNode, toNode)
							Id<Link> linkId = act.getLinkId();
							// get coord on the link using linkId and network
						}
						
					} else if (planElement instanceof Leg) {
						// network mode or teleported mode
						Leg leg = (Leg) planElement;
						leg.setRoute(null);
						
					} else {
						throw new RuntimeException("PlanElement "+ planElement+" is not recognized.");
					}
					
					
				}
				
				
			}
			
			
			
		}
		
		
		new PopulationWriter(inputPopulation).write(outputFile);
		
		
	}

}
