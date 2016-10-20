package navigation;

import floor.Cell;
import floor.Coordinate;
import floor.Obstacle;
import sensor.SensorServices;
import util.Debugger;

import java.util.HashMap;

public class ControlSystemService {

    private Coordinate currentPos;

    private static ControlSystemService controlSystemService;
    private HashMap<Coordinate, Cell> visited = new HashMap<>();
    private HashMap<Coordinate, Cell> unvisited = new HashMap<>();
    private SensorServices sensorService;

    /**
     * The control system.
     *
     * @param sensorService Reference to sensor/simulator.
     */
    private ControlSystemService(SensorServices sensorService) {
        Debugger.log("Starting control system");

        this.sensorService = sensorService;

        clean();
    }

    /**
     * Singleton.
     *
     * @param sL Reference to sensor/simulator.
     * @return instance
     */
    public static ControlSystemService getInstance(SensorServices sL) {
        if (controlSystemService == null)
            controlSystemService = new ControlSystemService(sL);

        return controlSystemService;
    }

    private int unv = 0;
    private int v = 0;

    /**
     * Set current position of sweeper.
     *
     * @param currentPos Current position.
     */
    public void setPosition(Coordinate currentPos) {
        this.currentPos = currentPos;
    }

    private void clean() {
        setPosition(sensorService.getStartPosition());

        do {

            int x = (int) currentPos.getX();
            int y = (int) currentPos.getY();
            Cell cell = sensorService.getCell(x, y);

            Debugger.log("VISITED: " + v + "\tUNVISITED: " + unv);

            Debugger.log("Cleaning cell (" + x + ", " + y + ")");
            Debugger.log("Dirt: " + cell.checkDirt());

            // Clean if dirt
            cell.clean();

            // Update visited and unvisited cells
            registerCells();

            // Set next cell
            RandomPosition randomDirection = new RandomPosition(x, y);
            Coordinate randomNr = randomDirection.getRandomCoordinate();
            currentPos.setX(randomNr.getX());
            currentPos.setY(randomNr.getY());

            // Delay for visualization
            if (Debugger.getMode()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        } while (!unvisited.isEmpty());

        Debugger.log("Cleaning done!");
    }
    private boolean checkDirt(int x, int y){
    	return sensorService.getCell(x, y).checkDirt();
    }

    /**
     * Document this and surrounding cells as visited or unvisited.
     *
     * TODO: Conflict with dirt levels. Assumes visited = clean, unvisited = dirty.
     */
    private void registerCells() {
        int x = (int) currentPos.getX();
        int y = (int) currentPos.getY();

        // Current cell
        Cell cell = sensorService.getCell(x, y);
        visited.put(currentPos, cell);
        unvisited.remove(currentPos);

        // Top cell
        Coordinate topCoordinate = new Coordinate(x, y + 1);
        if (!visited.containsKey(topCoordinate) && !cell.blocked(Obstacle.TOP))
                unvisited.put(topCoordinate, sensorService.getCell(x, y + 1));

        // Bottom cell
        Coordinate bottomCoordinate = new Coordinate(x, y - 1);
        if (!visited.containsKey(bottomCoordinate) && !cell.blocked(Obstacle.BOTTOM))
                unvisited.put(bottomCoordinate, sensorService.getCell(x, y - 1));

        // Left cell
        Coordinate leftCoordinate = new Coordinate(x - 1, y);
        if (!visited.containsKey(leftCoordinate) && !cell.blocked(Obstacle.LEFT))
                unvisited.put(leftCoordinate, sensorService.getCell(x - 1, y));

        // Right cell
        Coordinate rightCoordinate = new Coordinate(x + 1, y);
        if (!visited.containsKey(rightCoordinate) && !cell.blocked(Obstacle.RIGHT))
                unvisited.put(rightCoordinate, sensorService.getCell(x + 1, y));

    }

}