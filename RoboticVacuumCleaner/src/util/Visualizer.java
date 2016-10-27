package util;

import control.ControlSystemService;
import floor.Cell;
import floor.Coordinate;
import floor.Obstacle;
import sensor.FloorPlan;

public class Visualizer {

	private static FloorPlan floorPlan = FloorPlan.getInstance();
	private static ControlSystemService control = ControlSystemService.getInstance();

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN = "\u001B[32m";

	public static void print() {
		Cell[][] plan = floorPlan.getFloorPlan();

		if (plan.length == 0 || plan[0].length == 0)
			return;

		for (int i = plan.length - 1; i >= 0; --i) {

			// Top
			for (int j = 0; j < plan[i].length; ++j) {
				System.out.print(
						plan[i][j].blocked(Obstacle.TOP)
								? "-----" : "     "
				);
			}
			System.out.println("");

			// Mid
			for (int j = 0; j < plan[i].length; ++j) {
				System.out.print(
						plan[i][j].blocked(Obstacle.LEFT)
								? "| " : "  "
				);

				Coordinate pos = control.getCurrentPos();
				System.out.print(
						(pos.getX() == i && pos.getY() == j)
								? ANSI_GREEN + "o" + ANSI_RESET : " "
				);

				System.out.print(
						plan[i][j].blocked(Obstacle.RIGHT)
								? " |" : "  "
				);
			}
			System.out.println("");

			// Bottom
			for (int j = 0; j < plan[i].length; ++j) {
				System.out.print(
						plan[i][j].blocked(Obstacle.BOTTOM)
								? "-----" : "     "
				);
			}
			System.out.println("");
		}
	}

}
