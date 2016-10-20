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
	public Coordinate getLeftPosition(){
		return sensorLoader.getLeftPosition(sensorLoader.getStartPosition());
	}
	public Coordinate getRightPosition(){
		return sensorLoader.getRightPosition(sensorLoader.getStartPosition());
	}
	public Coordinate getDownPosition(){
		return sensorLoader.getDownPosition(sensorLoader.getStartPosition());
	}
	public Coordinate getUpPosition(){
		return sensorLoader.getUpPosition(sensorLoader.getStartPosition());
	}
	public void cleanDirt(int x, int y){
		sensorLoader.getCell(x, y).checkDirtCondition();
	}
	public void loadFloorPlan() throws ParserConfigurationException, SAXException, IOException{
		sensorLoader.loadFloorPlan();
	}
}
