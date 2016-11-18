package control;

import floor.Cell;
import floor.Coordinate;
import floor.DoorStatus;
import floor.Obstacle;
import sensor.SensorServices;
import util.Debugger;


import java.util.*;

class Navigator {

	private Coordinate currentPos;
	private Map<Coordinate, Cell> clean;
	private Map<Coordinate, Cell> dirty;
	private SensorServices sensorService;

	/**
	 * Store current sweeper coordinate info.
	 *
	 * @param currentPos Current position.
	 * @param clean Clean cells.
	 * @param dirty Dirty cells.
	 */
	Navigator(Coordinate currentPos,
			  Map<Coordinate, Cell> clean,
			  Map<Coordinate, Cell> dirty) {
		this.currentPos = currentPos;
		this.clean = clean;
		this.dirty = dirty;
		this.sensorService = SensorServices.getInstance();

		find();
	}

	private void find() {

		// List of surrounding obstacles
		Set<Obstacle> obstacles = detectObstacles(currentPos);

		// List of surrounding free cells
		EnumSet<Obstacle> free = EnumSet.allOf(Obstacle.class);
		free.removeAll(obstacles);

		// Register cells
		registerCells(free);

		// List of surrounding dirty cells
		int x = (int) currentPos.getX();
		int y = (int) currentPos.getY();
		ArrayList<Coordinate> dirt = new ArrayList<>();
		Coordinate coordinate;
		for (Obstacle o : free) {
			switch (o) {
				case TOP:
					coordinate = new Coordinate(x + 1, y);
					break;
				case BOTTOM:
					coordinate = new Coordinate(x - 1, y);
					break;
				case LEFT:
					coordinate = new Coordinate(x, y - 1);
					break;
				case RIGHT:
				default:
					coordinate = new Coordinate(x, y + 1);
					break;
			}
			if (dirty.containsKey(coordinate))
				dirt.add(coordinate);
		}

		// Favor a clean cell
		if (!dirt.isEmpty()) {
			Random r = new Random();
			int i = r.nextInt(dirt.size());
			this.currentPos = dirt.get(i);
			return;
		}

		// Check for the nearest dirty cell and head in that direction
		searchDirtyCell(free);
	}

	private void searchDirtyCell(EnumSet<Obstacle> free) {
		Coordinate c;

		// Search setup
		ArrayList<Coordinate> finderClean = new ArrayList<>();
		Queue<Coordinate> queue = new LinkedList<>();
		HashMap<Coordinate, Coordinate> origins = new HashMap<>();
		for (Obstacle o: free) {
			c = Cell.adjacent(o, currentPos);
			queue.add(c);
			origins.put(c, c);
		}

		// Search for nearest dirty cell
		while (!queue.isEmpty()) {
			c = queue.remove();
			finderClean.add(c);

			if (dirty.containsKey(c)) {
				currentPos = origins.get(c);
				return;
			}

			EnumSet<Obstacle> directions = EnumSet.allOf(Obstacle.class);

			for (Obstacle o: directions) {
				Coordinate coordinate = Cell.adjacent(o, c);
				if ((dirty.containsKey(coordinate) || clean.containsKey(coordinate))
						&& !finderClean.contains(coordinate) && !queue.contains(coordinate)) {
					queue.add(coordinate);
					origins.put(coordinate, origins.get(c));
				}
			}
		}
	}

	/**
	 * Get the current list of dirty cells.
	 *
	 * @return Dirty cells
	 */
	Map<Coordinate, Cell> getDirty() {
		return dirty;
	}

	/**
	 * Get the current list of clean cells.
	 *
	 * @return Clean cells
	 */
	Map<Coordinate, Cell> getClean() {
		return clean;
	}

	/**
	 * Get the updated current position.
	 *
	 * @return Current position
	 */
	Coordinate getCurrentPos() {
		return currentPos;
	}

	/**
	 * Dynamic detection of surrounding obstacles from the current position.
	 *
	 * @param currentPos Current position
	 * @return List of obstacles.
	 */
	private Set<Obstacle> detectObstacles(Coordinate currentPos) {

		SensorServices sensor = SensorServices.getInstance();
		Map<Coordinate, DoorStatus> doorList = sensor.getDoorList();

		double x = currentPos.getX();
		double y = currentPos.getY();

		Cell currentCell = sensor.getCell(currentPos);
		Set<Obstacle> obstacles = currentCell.getObstacles();

		// Checks for presence of stairs
		EnumSet<Obstacle> free = EnumSet.allOf(Obstacle.class);
		free.removeAll(obstacles);
		for (Obstacle o : free) {

			Coordinate c = Cell.adjacent(o, currentPos);

			if (sensor.getCell(c).hasStairs()) {
				Debugger.log("Stairs detected to the " + o);
				obstacles.add(o);
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

	/**
	 * Record surrounding cells.
	 *
	 * @param free List of non-obstructed cell dirs.
	 */
	private void registerCells(EnumSet<Obstacle> free) {
		// Register surrounding cells
		for (Obstacle o : free) {
			Coordinate coordinate = Cell.adjacent(o, currentPos);

			if (!clean.containsKey(coordinate))
				dirty.put(coordinate, sensorService.getCell(coordinate));
		}
	}
}
