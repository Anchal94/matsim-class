package mixedTraffic;

import org.matsim.api.core.v01.Id;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;
import org.matsim.vehicles.VehicleWriterV1;
import org.matsim.vehicles.Vehicles;

public class VehiclesGenerator {

	
	public static void main(String[] args) {
		
		String fileName = "";
		
		Vehicles vehicles = VehicleUtils.createVehiclesContainer();
		
		VehicleType car = vehicles.getFactory().createVehicleType(Id.create("car", VehicleType.class));
		car.setMaximumVelocity(60./3.6); //--> m/s
		car.setPcuEquivalents(1.0);
		
		vehicles.addVehicleType(car);
		
		VehicleType bike = vehicles.getFactory().createVehicleType(Id.create("bike", VehicleType.class));
		bike.setMaximumVelocity(15.0/3.6);
		bike.setPcuEquivalents(0.25);
		
		vehicles.addVehicleType(bike);
		
		new VehicleWriterV1(vehicles).writeFile(fileName);
		
		
	}
	
	
}
