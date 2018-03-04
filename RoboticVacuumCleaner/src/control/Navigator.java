package control;

import controllers.UtilityController;
import floor.Cell;
import floor.Coordinate;
import floor.DoorStatus;
import floor.Obstacle;
import sensor.FloorPlan;
import util.Debugger;

import java.net.URISyntaxException;
import java.util.*;

import controllers.SensorController;

class Navigator {

	private Coordinate currentPos;
	private Map<Coordinate, Cell> clean;
	private Map<Coordinate, Cell> dirty;
	private SensorController sensorService;

	/**
	 * Store current sweeper coordinate info.
	 *
	 * @param currentPos Current position.
	 * @param clean Clean cells.
	 * @param dirty Dirty cells.
	 * @throws URISyntaxException 
	 */
	Navigator(Coordinate currentPos,
			  Map<Coordinate, Cell> clean,
			  Map<Coordinate, Cell> dirty) throws URISyntaxException {
		this.currentPos = currentPos;
		this.clean = clean;
		this.dirty = dirty;
		this.sensorService = SensorController.getServices();

		findNextPosition();
	}

	private void findNextPosition() throws URISyntaxException {

		// List of surrounding obstacles
		Set<Obstacle> obstacles = this.detectObstacles(currentPos);

		// List of surrounding freeCells cells
		EnumSet<Obstacle> freeCells = EnumSet.allOf(Obstacle.class);
		freeCells.removeAll(obstacles);

		// Register cells
		this.registerCells(freeCells);

		// List of surrounding dirty cells
		int x = (int) currentPos.getX();
		int y = (int) currentPos.getY();
		int xAxes = FloorPlan.getInstance().getFloorPlan().length;
		int currentX = FloorPlan.getInstance().getFloorPlan()[x].length;
		ArrayList<Coordinate> dirt = new ArrayList<>();
		Coordinate coordinate = null;
		for (Obstacle o : freeCells) {
			switch (o) {
				case TOP:
					if(UtilityController.getServices().checkInBounds(x,y-1, xAxes,currentX))
						coordinate = new Coordinate(x, y-1);
						if (dirty.containsKey(coordinate))
							dirt.add(coordinate);
				case BOTTOM:
					if(UtilityController.getServices().checkInBounds(x,y+1, xAxes,currentX))
						coordinate = new Coordinate(x, y+1);
						if (dirty.containsKey(coordinate))
							dirt.add(coordinate);
				case LEFT:
					if(UtilityController.getServices().checkInBounds(x-1,y, xAxes,currentX))
						coordinate = new Coordinate(x-1, y);
						if (dirty.containsKey(coordinate))
							dirt.add(coordinate);
				case RIGHT:
					if(UtilityController.getServices().checkInBounds(x+1,y, xAxes,currentX))
						coordinate = new Coordinate(x+1,y);
						if (dirty.containsKey(coordinate))
							dirt.add(coordinate);
			}
		}

		// Favor a clean cell
		if (!dirt.isEmpty()) {
			Random r = new Random();
			int i = r.nextInt(dirt.size());
			this.currentPos = dirt.get(i);
			return;
		}

		// Random selection
		Random r = new Random();
		int n = r.nextInt(2);
		if (n == 1) {
			List<Obstacle> dirs = new ArrayList<>(freeCells);
			Collections.shuffle(dirs);
			this.currentPos = Cell.adjacent(dirs.get(0), currentPos);
			return;
		}

		// Check for the nearest dirty cell and head in that direction
		searchDirtyCell(freeCells);
	}

	private void searchDirtyCell(EnumSet<Obstacle> free) throws URISyntaxException {
		Coordinate c;

		// Search setup
		ArrayList<Coordinate> finderClean = new ArrayList<>();
		Queue<Coordinate> queue = new LinkedList<>();
		HashMap<Coordinate, Coordinate> origins = new HashMap<>();
		for (Obstacle o: free) {
			if(Cell.adjacent(o,currentPos) != null){
				c = Cell.adjacent(o, currentPos);
				queue.add(c);
				origins.put(c, c);
			}
		}

		// Search for nearest dirty cell
		while (!queue.isEmpty()) {
			c = queue.remove();
			finderClean.add(c);

			if (dirty.containsKey(c)) {
				currentPos = origins.get(c);
				break;
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
	 * @throws URISyntaxException 
	 */
	private Set<Obstacle> detectObstacles(Coordinate currentPos) throws URISyntaxException {

		Map<Coordinate, DoorStatus> doorList = SensorController.getServices().getDoorList();

		double x = currentPos.getX();
		double y = currentPos.getY();

		Cell currentCell = SensorController.getServices().getCell(currentPos);
		Set<Obstacle> obstacles = currentCell.getObstacles(); // Get enum some of top, right, left, bottom

		// Checks for presence of stairs
		EnumSet<Obstacle> enumCells = EnumSet.allOf(Obstacle.class);
		enumCells.removeAll(obstacles);
		for (Obstacle neighbourObstacle : enumCells) {
			if(Cell.adjacent(neighbourObstacle, currentPos) != null){
				if (SensorController.getServices().getCell(Cell.adjacent(neighbourObstacle, currentPos)).hasStairs()) {
					Debugger.log("Stairs detected to the " + neighbourObstacle);
					obstacles.add(neighbourObstacle);
				}
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
	 * @throws URISyntaxException 
	 */
	private void registerCells(EnumSet<Obstacle> free) throws URISyntaxException {
		// Register surrounding cells
		for (Obstacle o : free) {
			Coordinate coordinate = Cell.adjacent(o, currentPos);

			if (!clean.containsKey(coordinate) && coordinate != null)
				dirty.put(coordinate, sensorService.getCell(coordinate));
		}
	}
}
