import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import navigation.ControlSystemService;

import org.xml.sax.SAXException;

import sensor.SensorServices;
import util.Debugger;

public class Driver {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		SensorServices sL = SensorServices.getInstance();
		ControlSystemService.getInstance(sL);
	}
}
