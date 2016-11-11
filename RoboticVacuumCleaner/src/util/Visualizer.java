package util;

import control.ControlSystemService;
import floor.Cell;
import floor.Coordinate;
import floor.DoorStatus;
import floor.Obstacle;
import sensor.FloorPlan;

import java.util.HashMap;

public class Visualizer {

	private FloorPlan floorPlan;
	private ControlSystemService control;

	private static Visualizer instance = null;
	public final String ANSI_RESET = "\u001B[0m";
	public final String ANSI_SWEEPER = "\u001B[32m";
	public final String ANSI_DIRTY = "\u001B[31m";
	public final String ANSI_STAIRS = "\u001B[35m";
	public final String ANSI_SEMIDIRTY = "\u001B[33m";

	private Visualizer() {
		floorPlan = FloorPlan.getInstance();
		control = ControlSystemService.getInstance();
	}

	public static Visualizer getInstance() {
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
					System.out.print(ANSI_STAIRS + "S" + ANSI_RESET);
				} else if (pos.getX() == i && pos.getY() == j) {
					System.out.print(ANSI_SWEEPER + "O" + ANSI_RESET);
				} else if (plan[i][j].getDirt() == 2){
					System.out.print(ANSI_DIRTY + "x" + ANSI_RESET);
				} else if (plan[i][j].getDirt() == 1) {
					System.out.print(ANSI_SEMIDIRTY + "x" + ANSI_RESET);
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
