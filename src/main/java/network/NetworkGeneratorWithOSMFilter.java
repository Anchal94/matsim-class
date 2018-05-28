package network;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.io.NetworkWriter;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.io.OsmNetworkReader;
import org.matsim.core.utils.io.OsmNetworkReader.OsmFilter;

public class NetworkGeneratorWithOSMFilter {
	
	
	public static void main(String[] args) {
		
		Config config = ConfigUtils.createConfig();
		Scenario scenario = ScenarioUtils.createScenario(config);
		
		Network network = scenario.getNetwork();

		String epsg = "EPSG:25832";
		String osmFileName = "";
		String outputNetwork = "";
		
		CoordinateTransformation transformation = TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, epsg);
		OsmNetworkReader reader = new OsmNetworkReader(network, transformation);
		reader.addOsmFilter(new OsmFilter() {
			
			@Override
			public boolean coordInFilter(Coord coord, int hierarchyLevel) {
				if (hierarchyLevel >= 5) return false;
				else return true;
			}
		});
		
		reader.parse(osmFileName);
			
		NetworkWriter writer = new NetworkWriter(network);
		writer.write(outputNetwork);
		
		
	}

}
