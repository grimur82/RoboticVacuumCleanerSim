package driver;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import control.ControlSystemService;

import org.xml.sax.SAXException;

import sensor.SensorServices;

public class Driver {

	private Driver() {}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		SensorServices sL = SensorServices.getInstance();
		ControlSystemService control = ControlSystemService.getInstance();
		control.setSensor(sL);
		control.clean();
	}
}
