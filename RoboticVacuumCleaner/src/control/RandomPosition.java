package control;

import java.util.Random;

import floor.Coordinate;
import floor.Obstacle;
import sensor.SensorServices;

public class RandomPosition {

	// Current position of sweeper
	private Coordinate currentPos;

	public RandomPosition(Coordinate currentPos){
		this.currentPos = currentPos;
	}

	/**
	 * Select a random surrounding coordinate within bounds.
	 *
	 * @return Random surrounding coordinate.
	 */
	public Coordinate getRandomCoordinate(){
		SensorServices sensor = SensorServices.getInstance();

		Random r = new Random();
		Coordinate chosenCell;
		Obstacle chosenDir;

		double x = currentPos.getX();
		double y = currentPos.getY();

		do {

			switch (r.nextInt(4)) {
				case 1:
					chosenCell = new Coordinate(x - 1, y);
					chosenDir = Obstacle.BOTTOM;
					break;
				case 2:
					chosenCell = new Coordinate(x + 1, y);
					chosenDir = Obstacle.TOP;
					break;
				case 3:
					chosenCell = new Coordinate(x, y - 1);
					chosenDir = Obstacle.LEFT;
					break;
				default:
					chosenCell = new Coordinate(x, y + 1);
					chosenDir = Obstacle.RIGHT;
					break;
			}

		} while (sensor.getCell(currentPos).blocked(chosenDir));

		return chosenCell;
	}
}
