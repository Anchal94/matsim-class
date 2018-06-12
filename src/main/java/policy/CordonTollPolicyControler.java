package policy;

import java.util.Collection;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class CordonTollPolicyControler {
	
public static void main(String[] args) {
		
		String configFile = "/Users/amit/Documents/matsimClass/ue_05June/data2/run255.output_config.xml";
		
		Config config = ConfigUtils.loadConfig(configFile);
		
		config.network().setInputFile("run255.output_network.xml.gz");
		config.plans().setInputFile("run255.output_plans.xml.gz");
		
		config.controler().setFirstIteration(300);
		config.controler().setLastIteration(310);
		config.controler().setOutputDirectory("output_cordonToll");
		
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.overwriteExistingFiles);
		
		//let's use 'ride' as teleported mode --> need to set speed factor / speed
		config.plansCalcRoute().getOrCreateModeRoutingParams("ride").setTeleportedModeFreespeedFactor(1.5);
		
		Scenario scenario = ScenarioUtils.loadScenario(config);
		
		// 
		String essen_shapeFile = ""; // --> Essen
		
		Controler controler = new Controler(scenario);
		
		controler.addOverridingModule(new AbstractModule() {
			
			@Override
			public void install() {
				CordonTollEventHandler instance = new CordonTollEventHandler(essen_shapeFile);
				addEventHandlerBinding().toInstance(instance);
			}
		});
		
		
		controler.run();
		
	}
	

}
