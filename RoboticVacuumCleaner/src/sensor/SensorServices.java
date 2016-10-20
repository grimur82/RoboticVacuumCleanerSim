package sensor;

import floor.Cell;
import floor.Coordinate;
import org.xml.sax.SAXException;
import util.Debugger;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class SensorServices {

    private static SensorServices sensorServices;
    private static Cell[][] floorPlan;
    private static Coordinate startPosition;

    private SensorServices() throws ParserConfigurationException, SAXException, IOException {
        Debugger.log("Starting sensor simulator");
        FloorPlanLoader floorPlanLoader = new FloorPlanLoader();
        floorPlan = floorPlanLoader.getFloorPlan();
        startPosition = floorPlanLoader.getStartPosition();
    }

    public static SensorServices getInstance() throws ParserConfigurationException, SAXException, IOException {
        if (sensorServices == null)
            sensorServices = new SensorServices();

        return sensorServices;
    }

    public Cell getCell(int x, int y) {
        return floorPlan[x][y];
    }

    public Coordinate getStartPosition() {
        return startPosition;
    }
 
}