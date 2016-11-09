package control;

import floor.Cell;
import floor.Coordinate;
import floor.DoorStatus;
import floor.Obstacle;
import sensor.SensorServices;
import util.Debugger;


import java.util.*;

public class Navigator {

	private Coordinate currentPos;
	private HashMap<Coordinate, Cell> visited;
	private HashMap<Coordinate, Cell> unvisited;
	private SensorServices sensorService;

	/**
	 * Store current sweeper coordinate info.
	 *
	 * @param currentPos Current position.
	 * @param visited Visited cells.
	 * @param unvisited Unvisited cells.
	 */
	public Navigator(Coordinate currentPos,
					 HashMap<Coordinate, Cell> visited,
					 HashMap<Coordinate, Cell> unvisited) {
		this.currentPos = currentPos;
		this.visited = visited;
		this.unvisited = unvisited;
		this.sensorService = SensorServices.getInstance();

		find();
	}

	private void find() {

		// List of surrounding obstacles
		EnumSet<Obstacle> obstacles = detectObstacles(currentPos);

		// List of surrounding free cells
		EnumSet<Obstacle> free = EnumSet.allOf(Obstacle.class);
		free.removeAll(obstacles);

		// Register cells
		registerCells(free);

		// List of surrounding dirty cells
		int x = (int) currentPos.getX();
		int y = (int) currentPos.getY();
		ArrayList<Coordinate> dirty = new ArrayList<>();
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
			if (unvisited.containsKey(coordinate))
				dirty.add(coordinate);
		}

		// Favor a clean cell
		if (!dirty.isEmpty()) {
			Random r = new Random();
			int i = r.nextInt(dirty.size());
			this.currentPos = dirty.get(i);
			return;
		}

		// Check for the nearest unvisited or dirty cell and head in that direction
		searchDirtyCell(free);
	}

	private void searchDirtyCell(EnumSet<Obstacle> free) {

		int x = (int) currentPos.getX();
		int y = (int) currentPos.getY();

		Coordinate c;

		// Search setup
		ArrayList<Coordinate> finderVisited = new ArrayList<>();
		Queue<Coordinate> queue = new LinkedList<>();
		HashMap<Coordinate, Coordinate> origins = new HashMap<>();
		for (Obstacle o: free) {
			switch (o) {
				case TOP:
					c = new Coordinate(x + 1, y);
					break;
				case BOTTOM:
					c = new Coordinate(x - 1, y);
					break;
				case LEFT:
					c = new Coordinate(x, y - 1);
					break;
				case RIGHT:
				default:
					c = new Coordinate(x, y + 1);
					break;
			}
			queue.add(c);
			origins.put(c, c);
		}

		// Search for nearest dirty cell
		int cx;
		int cy;
		while (!queue.isEmpty()) {
			c = queue.remove();
			finderVisited.add(c);

			if (unvisited.containsKey(c)) {
				currentPos = origins.get(c);
				return;
			}

			cx = (int) c.getX();
			cy = (int) c.getY();

			EnumSet<Obstacle> directions = EnumSet.allOf(Obstacle.class);
			Coordinate coordinate;
			for (Obstacle o: directions) {
				switch (o) {
					case TOP:
						coordinate = new Coordinate(cx + 1, cy);
						break;
					case BOTTOM:
						coordinate = new Coordinate(cx - 1, cy);
						break;
					case LEFT:
						coordinate = new Coordinate(cx, cy - 1);
						break;
					case RIGHT:
					default:
						coordinate = new Coordinate(cx, cy + 1);
						break;
				}
				if ((unvisited.containsKey(coordinate) || visited.containsKey(coordinate))
						&& !finderVisited.contains(coordinate) && !queue.contains(coordinate)) {
					queue.add(coordinate);
					origins.put(coordinate, origins.get(c));
				}
			}
		}
	}

	/**
	 * Get the current list of unvisited cells.
	 *
	 * @return Unvisited cells
	 */
	public HashMap<Coordinate, Cell> getUnvisited() {
		return unvisited;
	}

	/**
	 * Get the current list of visited cells.
	 *
	 * @return Visited cells
	 */
	public HashMap<Coordinate, Cell> getVisited() {
		return visited;
	}

	/**
	 * Get the updated current position.
	 *
	 * @return Current position
	 */
	public Coordinate getCurrentPos() {
		return currentPos;
	}

	/**
	 * Dynamic detection of surrounding obstacles from the current position.
	 *
	 * @param currentPos Current position
	 * @return List of obstacles.
	 */
	private EnumSet<Obstacle> detectObstacles(Coordinate currentPos) {

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

	/**
	 * Record this and surrounding cells.
	 *
	 * @param free List of non-obstructed cell dirs.
	 */
	private void registerCells(EnumSet<Obstacle> free) {

		int x = (int) currentPos.getX();
		int y = (int) currentPos.getY();

		// Current cell
		Cell cell = sensorService.getCell(x, y);
		visited.put(currentPos, cell);
		unvisited.remove(currentPos);

		// Register surrounding cells
		for (Obstacle o : free) {

			switch (o) {

				case TOP:
					Coordinate topCoordinate = new Coordinate(x + 1, y);
					if (!visited.containsKey(topCoordinate))
						unvisited.put(topCoordinate, sensorService.getCell(x + 1, y));
					break;

				case BOTTOM:
					Coordinate bottomCoordinate = new Coordinate(x - 1, y);
					if (!visited.containsKey(bottomCoordinate))
						unvisited.put(bottomCoordinate, sensorService.getCell(x - 1, y));
					break;

				case LEFT:
					Coordinate leftCoordinate = new Coordinate(x, y - 1);
					if (!visited.containsKey(leftCoordinate))
						unvisited.put(leftCoordinate, sensorService.getCell(x, y - 1));
					break;

				case RIGHT:
				default:
					Coordinate rightCoordinate = new Coordinate(x, y + 1);
					if (!visited.containsKey(rightCoordinate))
						unvisited.put(rightCoordinate, sensorService.getCell(x, y + 1));
					break;

			}
		}
	}
}
