package controllers;

import floor.Cell;
import floor.Coordinate;
import floor.DoorStatus;
import sensor.DoorSimulator;
import sensor.FloorPlan;

import java.net.URISyntaxException;
import java.util.Map;
public class SensorController {

    private static SensorController sensorServices;

    public static SensorController getServices() {
        if (sensorServices == null)
            sensorServices = new SensorController();

        return sensorServices;
    }

	/**
	 * Initiate door simulator.
	 */
	public void initDoorSimulator() {
		Thread t = new Thread(new DoorSimulator());
		t.start();
	}

    public Cell getCell(int x, int y) throws URISyntaxException {
        return FloorPlan.getInstance().getCell(x, y);
    }

	/**
	 * Get cell by Coordinate object.
	 *
	 * @param c Coordinate.
	 * @return cell
	 * @throws URISyntaxException 
	 */
	public Cell getCell(Coordinate c) throws URISyntaxException {
		return FloorPlan.getInstance().getCell((int) (c.getX()), (int) (c.getY()));
	}

	/**
	* Gets starting position that was read from xml file.
	*/
    public Coordinate getStartPosition() throws URISyntaxException {
        return FloorPlan.getInstance().getStartPosition();
    }

	public Map<Coordinate, DoorStatus> getDoorList() throws URISyntaxException {
		return FloorPlan.getInstance().getDoorList();
	}
}