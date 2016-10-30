package control;


import floor.Coordinate;
import floor.Obstacle;
import util.Debugger;

import java.util.EnumSet;
import java.util.Random;

public class RandomPosition {

	public static Coordinate generate(Coordinate currentPos, EnumSet<Obstacle> free) {

		int x = (int) currentPos.getX();
		int y = (int) currentPos.getY();

		Random r = new Random();
		int i = r.nextInt(free.size());
		Coordinate chosenCoord;

		switch ((Obstacle)free.toArray()[i]) {
			case TOP:
				chosenCoord = new Coordinate(x + 1, y);
				break;
			case BOTTOM:
				chosenCoord = new Coordinate(x - 1, y);
				break;
			case LEFT:
				chosenCoord = new Coordinate(x, y - 1);
				break;
			case RIGHT:
			default:
				chosenCoord = new Coordinate(x, y + 1);
				break;
		}

		return chosenCoord;

	}

}
