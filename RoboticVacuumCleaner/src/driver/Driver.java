package driver;

import java.io.IOException;
import java.net.URISyntaxException;
import javax.xml.parsers.ParserConfigurationException;

import control.ControlSystemService;
import controllers.SensorController;
import controllers.UtilityController;

import org.xml.sax.SAXException;
import sensor.FloorPlan;

public class Driver {

	private Driver() {}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		UtilityController.getServices().listFileNames();
		FloorPlan.getInstance().askuserforfile();
		SensorController.getServices().initDoorSimulator();
		ControlSystemService.getServices().beginCleaning();
	}
}
