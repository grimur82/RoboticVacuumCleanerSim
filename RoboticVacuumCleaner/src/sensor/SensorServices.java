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

public class SensorServices {

    private static SensorServices sensorServices;

	private FloorPlan floorPlan;
	private HashMap<Coordinate, DoorStatus> doorList;

    private SensorServices() {
        Debugger.log("Starting sensor simulator");
		floorPlan = FloorPlan.getInstance();
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
    public Cell[][] getFloorPlan(){
    	return floorPlan.getFloorPlan();
    }

	public HashMap<Coordinate, DoorStatus> getDoorList() {
		return floorPlan.getDoorList();
	}

	public boolean senseObstacleTop(Cell cell) {
		if (cell.getObstacles().contains(Obstacle.TOP)){
			Debugger.log("Path to TOP cell is obstructed");
			return true;
		}
		return false;
	}

	public boolean senseObstacleBottom(Cell cell) {
		if (cell.getObstacles().contains(Obstacle.BOTTOM)){
			Debugger.log("Path to BOTTOM cell is obstructed");
			return true;
		}
		return false;
	}

	public boolean senseObstacleLeft(Cell cell) {
		if (cell.getObstacles().contains(Obstacle.LEFT)){
			Debugger.log("Path to LEFT cell is obstructed");
			return true;
		}
		return false;
	}

	public boolean senseObstacleRight(Cell cell) {
		if (cell.getObstacles().contains(Obstacle.RIGHT)){
			Debugger.log("Path to RIGHT cell is obstructed");
			return true;
		}
		return false;
	}
}