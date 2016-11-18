package sensor;

import floor.Coordinate;
import floor.DoorStatus;
import util.Debugger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Randomly changes the door status.
 */

class DoorSimulator implements Runnable {

	private boolean running;

	DoorSimulator() {
		this.running = true;
	}

	@Override
	public void run() {

		FloorPlan floorPlan = FloorPlan.getInstance();

		Random r;
		Coordinate randomCoordinate;

		try {
			while (running) {

				// Get the updated door list
				HashMap<Coordinate, DoorStatus> doorList = floorPlan.getDoorList();
				List<Coordinate> coordinates = new ArrayList<>(doorList.keySet());

				// Choose a random coordinate
				r = new Random();
				randomCoordinate = coordinates.get(r.nextInt(coordinates.size()));

				// Print that the status is changed.
				Debugger.log("Changing door status of (" + randomCoordinate.getX() + ", "
						+ randomCoordinate.getY() + ")");

				// Change door status every number of milliseconds
				floorPlan.changeDoorStatus(randomCoordinate);
				Thread.sleep(5555);

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
