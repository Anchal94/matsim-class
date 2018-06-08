package policy;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;

public class BaseCaseCtdControler {
	
	
	public static void main(String[] args) {
		
		String configFile = "/Users/amit/Documents/matsimClass/ue_05June/data2/run255.output_config.xml";
		
		Config config = ConfigUtils.loadConfig(configFile);
		
		config.network().setInputFile("run255.output_network.xml.gz");
		config.plans().setInputFile("run255.output_plans.xml.gz");
		
		config.controler().setFirstIteration(300);
		config.controler().setLastIteration(310);
		config.controler().setOutputDirectory("output");
		
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.overwriteExistingFiles);
		
		//don't copy
		config.plansCalcRoute().getOrCreateModeRoutingParams("ride").setTeleportedModeFreespeedFactor(1.5);
		// up to here
		
		
		Scenario scenario = ScenarioUtils.loadScenario(config);
		
		Controler controler = new Controler(scenario);
		

		controler.run();
		
	}
	

}
