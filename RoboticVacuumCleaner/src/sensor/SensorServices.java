package sensor;

import floor.Coordinate;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class SensorServices {

	private static SensorServices sensorServices;
	private static SensorLoader sensorLoader;

	private SensorServices() throws ParserConfigurationException, SAXException, IOException {
        sensorLoader = new SensorLoader();
    }

	public static SensorServices getInstance() throws ParserConfigurationException, SAXException, IOException {
        if (sensorServices == null)
            sensorServices = new SensorServices();

		return sensorServices;
	}

	public Coordinate getStartPosition(){
		return sensorLoader.getStartPosition();
	}
}
