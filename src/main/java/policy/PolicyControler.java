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

/**
 * 
 * Set maximum speed of all links inside Essen to 30 kph.
 * 
 * @author amit
 */
public class PolicyControler {
	
	
	public static void main(String[] args) {
		
		String configFile = "/Users/amit/Documents/matsimClass/ue_05June/data2/run255.output_config.xml";
		
		Config config = ConfigUtils.loadConfig(configFile);
		
		config.network().setInputFile("run255.output_network.xml.gz");
		config.plans().setInputFile("run255.output_plans.xml.gz");
		
		config.controler().setFirstIteration(300);
		config.controler().setLastIteration(310);
		config.controler().setOutputDirectory("output_policy");
		
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.overwriteExistingFiles);
		
		//don't copy
		config.plansCalcRoute().getOrCreateModeRoutingParams("ride").setTeleportedModeFreespeedFactor(1.5);
		// up to here
		
		
		Scenario scenario = ScenarioUtils.loadScenario(config);
		
		// 
		String essen_shapeFile = "";
		Collection<SimpleFeature> allFeatures = ShapeFileReader.getAllFeatures(essen_shapeFile);
		
		for( Link link : scenario.getNetwork().getLinks().values()) {
			Point point = MGC.coord2Point(link.getCoord()); // Centroid of the link --> to a point geometry
			for (SimpleFeature  sf : allFeatures) {
				if (((Geometry)sf.getDefaultGeometry()).contains(point)) {
					link.setFreespeed(30.0/3.6);
					break;
				}
			}
		}
		
		Controler controler = new Controler(scenario);
		

		controler.run();
		
	}
	

}
