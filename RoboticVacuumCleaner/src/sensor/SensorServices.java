package sensor;

import floor.Cell;
import floor.Coordinate;
import floor.DoorStatus;
import floor.Obstacle;

import org.xml.sax.SAXException;
import util.Debugger;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SensorServices {

    private static SensorServices sensorServices;

	private FloorPlan floorPlan;

    private SensorServices() {
    	floorPlan = FloorPlan.getInstance();
        Debugger.log("Starting sensor simulator");
		initDoorSimulator();
    }

    public static SensorServices getInstance() {
        if (sensorServices == null)
            sensorServices = new SensorServices();

        return sensorServices;
    }

	/**
	 * Initiate door simulator.
	 */
	private void initDoorSimulator() {
		Thread t = new Thread(new DoorSimulator());
		t.start();
	}

    public Cell getCell(int x, int y) {
        return floorPlan.getCell(x, y);
    }

	/**
	 * Get cell by Coordinate object.
	 *
	 * @param c Coordinate.
	 * @return cell
	 */
	public Cell getCell(Coordinate c) {
		return floorPlan.getCell((int) c.getX(), (int) c.getY());
	}

    public Coordinate getStartPosition() {
        return floorPlan.getStartPosition();
    }

	public Map<Coordinate, DoorStatus> getDoorList() {
		return floorPlan.getDoorList();
	}
}