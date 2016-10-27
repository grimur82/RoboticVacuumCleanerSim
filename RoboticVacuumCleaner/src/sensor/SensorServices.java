package sensor;

import floor.Cell;
import floor.Coordinate;
import floor.Obstacle;

import org.xml.sax.SAXException;
import util.Debugger;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.ArrayList;

public class SensorServices {

    private static SensorServices sensorServices;
    private static ArrayList<Coordinate> doorList;

	private static FloorPlan floorPlan;

    private SensorServices() throws ParserConfigurationException, SAXException, IOException {
        Debugger.log("Starting sensor simulator");
		floorPlan = FloorPlan.getInstance();
    }

    public static SensorServices getInstance() throws ParserConfigurationException, SAXException, IOException {
        if (sensorServices == null)
            sensorServices = new SensorServices();

        return sensorServices;
    }

    public Cell getCell(int x, int y) {
        return FloorPlan.getCell(x, y);
    }

    public Coordinate getStartPosition() {
        return FloorPlan.getStartPosition();
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
			Debugger.log("Path to RIGHT is obstructed");
			return true;
		}
		return false;
	}
}