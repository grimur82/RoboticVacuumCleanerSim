package sensor;

import floor.Cell;
import floor.Coordinate;
import util.Debugger;

/**
 * Read floor plan object.
 */
public class FloorPlan {

	// Singleton instance
	private static FloorPlan instance = null;

	// Known floor plan
	private static Cell[][] floorPlan;

	// Current position
	private static Coordinate startPosition;

	/**
	 * Initialize.
	 */
	private FloorPlan() {
		FloorPlanLoader loader = new FloorPlanLoader();
		floorPlan = loader.getFloorPlan();
		startPosition = loader.getStartPosition();
	}

	/**
	 * Singleton.
	 *
	 * @return Instance.
	 */
	public static FloorPlan getInstance() {
		if (instance == null)
			instance = new FloorPlan();
		return instance;
	}

	/**
	 * Get floor plan.
	 *
	 * @return Floor plan.
	 */
	public static Cell[][] getFloorPlan() {
		return floorPlan;
	}

	/**
	 * Get current position.
	 *
	 * @return Current position.
	 */
	public static Coordinate getStartPosition() {
		return startPosition;
	}

	/**
	 * Get cell.
	 *
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return Cell.
	 */
	public static Cell getCell(int x, int y) {
		return (floorPlan.length <= x || floorPlan[0].length <= y)
				? null : floorPlan[x][y];
	}

}
