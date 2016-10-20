import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import navigation.ControlSystemService;
import navigation.Navigation;

import org.xml.sax.SAXException;

import sensor.SensorServices;

public class Driver {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		SensorServices sL = SensorServices.getInstance();
		ControlSystemService cS = ControlSystemService.getInstance();
		sL.loadFloorPlan();
		System.out.println("Start x: " + sL.getStartPosition().getX());
		cS.setNavigation(sL.getStartPosition());
		sL.getLeftPosition();
		sL.cleanDirt(
		(int)sL.getLeftPosition().getX(), (int)sL.getLeftPosition().getY());
		sL.cleanDirt((int)sL.getStartPosition().getX(), (int) sL.getStartPosition().getY());
	}
}
