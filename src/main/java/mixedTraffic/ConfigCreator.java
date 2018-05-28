package mixedTraffic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup.LinkDynamics;
import org.matsim.core.config.groups.QSimConfigGroup.TrafficDynamics;
import org.matsim.core.config.groups.QSimConfigGroup.VehiclesSource;

public class ConfigCreator {
	
	
	public static void main(String[] args) {
		
		String inputConfigFile = "";
		
		Config config = ConfigUtils.loadConfig(inputConfigFile);
		
		// qsim
		config.qsim().setLinkDynamics(LinkDynamics.PassingQ);
		config.qsim().setTrafficDynamics(TrafficDynamics.kinematicWaves);
		config.qsim().setVehiclesSource(VehiclesSource.modeVehicleTypesFromVehiclesData);
		
		
		Collection<String> mainModes = new ArrayList<>();
		mainModes.add("car");
		mainModes.add("bike");
		config.qsim().setMainModes(mainModes);
//		config.qsim().setMainModes(Arrays.asList("car","bike");
		
		config.plansCalcRoute().setNetworkModes(mainModes);
		
		config.travelTimeCalculator().setAnalyzedModes("car,bike");
		config.travelTimeCalculator().setSeparateModes(true);
		
		config.vehicles().setVehiclesFile(" ... ");
		
		
		
		
		
		
		
		
		
	}
	

}
