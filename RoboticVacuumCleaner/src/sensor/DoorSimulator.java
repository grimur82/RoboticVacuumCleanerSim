package sensor;

import floor.Coordinate;
import floor.DoorStatus;
import util.Debugger;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Randomly changes the door status.
 */

public class DoorSimulator implements Runnable {

	private boolean running;

	public DoorSimulator() {
		this.running = true;
	}
	public void changeRunning(boolean state) {
		running = state;
	}
	@Override
	public void run() {
		Random r;
		Coordinate randomCoordinate;

		try {
			while (running) {

				// Get the updated door list
				HashMap<Coordinate, DoorStatus> doorList = FloorPlan.getInstance().getDoorList();
				List<Coordinate> coordinates = new ArrayList<>(doorList.keySet());
				r = new Random();
				randomCoordinate = coordinates.get(r.nextInt(coordinates.size()));

				// Print that the status is changed.
				Debugger.log("Changing door status to " + 
				FloorPlan.getInstance().getDoorStatus(randomCoordinate) + 
						" " + " randomCoordinate.getX() + "
						+ randomCoordinate.getY() + ")");
				// Change door status every number of milliseconds
				FloorPlan.getInstance().changeDoorStatus(randomCoordinate);
				Thread.sleep(5555);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}