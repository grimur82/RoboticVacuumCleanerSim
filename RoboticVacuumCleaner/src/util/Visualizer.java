package util;

import control.ControlSystemService;
import floor.Cell;
import floor.Coordinate;
import floor.Obstacle;
import sensor.FloorPlan;

import java.net.URISyntaxException;

public class Visualizer {

	private FloorPlan floorPlan;
	private ControlSystemService control;

	private static Visualizer instance = null;
	private final static String ANSIRESET = "\u001B[0m";
	private final static String ANSISWEEPER = "\u001B[32m";
	private final static String ANSIDIRTY = "\u001B[31m";
	private final static String ANSISTAIRS = "\u001B[35m";
	private final static String ANSISEMIDIRTY = "\u001B[33m";

	private Visualizer() throws URISyntaxException {
		floorPlan = FloorPlan.getInstance();
		control = ControlSystemService.getServices();
	}

	public static Visualizer getInstance() throws URISyntaxException {
		if (instance == null)
			instance = new Visualizer();
		return instance;
	}

	public void print() {

		Cell[][] plan = floorPlan.getFloorPlan();
		Coordinate pos = control.getCurrentPos();

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

				if (plan[i][j].hasStairs()) {
					System.out.print(ANSISTAIRS + "S" + ANSIRESET);
				} else if (pos.getX() == i && pos.getY() == j) {
					System.out.print(ANSISWEEPER + "O" + ANSIRESET);
				} else if (plan[i][j].getDirt() == 2){
					System.out.print(ANSIDIRTY + "x" + ANSIRESET);
				} else if (plan[i][j].getDirt() == 1) {
					System.out.print(ANSISEMIDIRTY + "x" + ANSIRESET);
				} else {
					System.out.print(" ");
				}

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

		// Delay for visualization
		if (Debugger.getMode()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
