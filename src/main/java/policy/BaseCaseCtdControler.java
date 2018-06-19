package policy;

import java.util.Arrays;

import org.hsqldb.lib.HashSet;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup.VehiclesSource;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.run.NetworkCleaner;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;

public class BaseCaseCtdControler {
	
	
	public static void main(String[] args) {
		
		String configFile = "/Users/amit/Documents/matsimClass/ue_05June/data2/run255.output_config.xml";
		
		Config config = ConfigUtils.loadConfig(configFile);
		
		config.network().setInputFile("run255.output_network.xml.gz");
		config.plans().setInputFile("run255.output_plans.xml.gz");
		
		config.controler().setFirstIteration(300);
		config.controler().setLastIteration(310);
		config.controler().setRunId("bau");
		
		config.qsim().setMainModes(Arrays.asList("car","bike"));
		config.qsim().setVehiclesSource(VehiclesSource.modeVehicleTypesFromVehiclesData);
		
		config.plansCalcRoute().setNetworkModes(Arrays.asList("car","bike"));
		config.travelTimeCalculator().setAnalyzedModes("car,bike");
		config.travelTimeCalculator().setSeparateModes(true);
		
		config.plansCalcRoute().removeModeRoutingParams("bike");
		
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.overwriteExistingFiles);
		
		//let's use 'ride' as teleported mode --> need to set speed factor / speed
		config.plansCalcRoute().getOrCreateModeRoutingParams("ride").setTeleportedModeFreespeedFactor(1.5);
		
		Scenario scenario = ScenarioUtils.loadScenario(config);
		scenario.getVehicles().addVehicleType(VehicleUtils.getFactory().createVehicleType(Id.create("bike", VehicleType.class)));
		scenario.getVehicles().addVehicleType(VehicleUtils.getFactory().createVehicleType(Id.create("car", VehicleType.class)));
		
		scenario
		.getNetwork()
		.getLinks()
		.values()
//		.stream()
//		.filter(l -> ! ((String)l.
//				getAttributes().
//				getAttribute("type")).
//				contains("motorway")).
		.forEach(l -> l.setAllowedModes(new java.util.HashSet<>(Arrays.asList("car","bike"))));
		
		for (Person person : scenario.getPopulation().getPersons().values()) {
			for (Plan plan : person.getPlans()) {
				for (PlanElement pe : plan.getPlanElements()) {
					if (pe instanceof Leg) {
						Leg leg = ((Leg)pe);
						if (leg.getMode().equals("bike")) {
							leg.setRoute(null);
						}
					}
				}
			}
		}
		
		Controler controler = new Controler(scenario);
		

		controler.run();
		
	}
	

}
