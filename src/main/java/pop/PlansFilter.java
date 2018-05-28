package pop;

import java.util.Collection;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class PlansFilter {
	
	public static void main(String[] args) {
		
		String plansFile = "../data_nemo/run255.output_plans.xml.gz";
		String shapeFile = "../data_nemo/ruhrgebiet_boundary.shp";
				
		 Collection<SimpleFeature> features = ShapeFileReader.getAllFeatures(shapeFile);
		 
		 Config config = ConfigUtils.createConfig();
		 config.plans().setInputFile(plansFile);
		 
		 Scenario scenario = ScenarioUtils.loadScenario(config);
		 
		 Population population = scenario.getPopulation();
		 
		 for (Person person : population.getPersons().values()) {
			 Plan selectedPlan = person.getSelectedPlan();
			 
			 for (PlanElement pe : selectedPlan.getPlanElements()) {
				 if (pe instanceof Activity) {
					 Activity act = (Activity) pe;
					 if (act.getType().startsWith("h")) {
						 Coord cord = act.getCoord();
						 if (isCoordInsideFeatures(cord, features)) {
							 System.out.println(pe);
						 }
					 }
					 act.setLinkId(null);
				 } else if (pe instanceof Leg) {
					 ((Leg) pe).setRoute(null);
				 }
			 }
		 }	
	}
	
	private static boolean isCoordInsideFeatures (Coord cord, Collection<SimpleFeature> features) {
		GeometryFactory factory = new GeometryFactory();
		Geometry point = factory.createPoint(new Coordinate(cord.getX(), cord.getY()));
		
		for (SimpleFeature feature : features) {	
			//include anything inside collection of polygons
			if ( ! ( (Geometry) feature.getDefaultGeometry()).contains(point) ) {
				return true;
			}
		}
		return false;
	}
	

}
