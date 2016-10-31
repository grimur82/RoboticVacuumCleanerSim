package control;

import java.util.EnumSet;
import java.util.HashMap;

import floor.Cell;
import floor.Coordinate;
import floor.DoorStatus;
import floor.Obstacle;
import sensor.SensorServices;
import util.Debugger;

/**
 * Dynamically calculates obstacles and based on door status.
 */
public class ObstacleDetector {

	public static EnumSet<Obstacle> generate(Coordinate currentPos) {

		SensorServices sensor = SensorServices.getInstance();
		HashMap<Coordinate, DoorStatus> doorList = sensor.getDoorList();

		double x = currentPos.getX();
		double y = currentPos.getY();

		Cell currentCell = sensor.getCell(currentPos);
		EnumSet<Obstacle> obstacles = currentCell.getObstacles();

		// Checks for presence of stairs
		EnumSet<Obstacle> free = EnumSet.allOf(Obstacle.class);
		free.removeAll(obstacles);
		for (Obstacle o : free) {

			switch (o) {

				case TOP:
					if (sensor.getCell(new Coordinate(x + 1, y)).hasStairs()) {
						Debugger.log("Stairs detected to the TOP");
						obstacles.add(Obstacle.TOP);
					}
					break;

				case BOTTOM:
					if (sensor.getCell(new Coordinate(x - 1, y)).hasStairs()) {
						Debugger.log("Stairs detected to the BOTTOM");
						obstacles.add(Obstacle.BOTTOM);
					}
					break;

				case LEFT:
					if (sensor.getCell(new Coordinate(x, y - 1)).hasStairs()) {
						Debugger.log("Stairs detected to the LEFT");
						obstacles.add(Obstacle.LEFT);
					}
					break;

				case RIGHT:
				default:
					if (sensor.getCell(new Coordinate(x, y + 1)).hasStairs()) {
						Debugger.log("Stairs detected to the RIGHT");
						obstacles.add(Obstacle.RIGHT);
					}
					break;
			}
		}

		// Checks each obstacle to see if it's still blocked by a door
		for (Obstacle o : obstacles) {

			switch (o) {

				case TOP:
					if (doorList.get(new Coordinate(x + 0.5, y + 0.5)) == DoorStatus.OPEN
							|| doorList.get(new Coordinate(x + 0.5, y - 0.5)) == DoorStatus.OPEN)
						obstacles.remove(Obstacle.TOP);
					break;

				case BOTTOM:
					if (doorList.get(new Coordinate(x - 0.5, y + 0.5)) == DoorStatus.OPEN
							|| doorList.get(new Coordinate(x - 0.5, y - 0.5)) == DoorStatus.OPEN)
						obstacles.remove(Obstacle.BOTTOM);
					break;

				case LEFT:
					if (doorList.get(new Coordinate(x, y - 0.5)) == DoorStatus.OPEN)
						obstacles.remove(Obstacle.LEFT);
					break;

				case RIGHT:
				default:
					if (doorList.get(new Coordinate(x, y + 0.5)) == DoorStatus.OPEN)
						obstacles.remove(Obstacle.RIGHT);
					break;

			}
		}

		return obstacles;
	}
}
