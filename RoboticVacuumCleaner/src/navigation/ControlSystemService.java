package navigation;

import floor.Cell;
import floor.Coordinate;
import sensor.SensorServices;
import util.Debugger;

import java.util.HashMap;

public class ControlSystemService {

    private Coordinate currentPos;

    private static ControlSystemService controlSystemService;
    private HashMap<Coordinate, Cell> visited = new HashMap<Coordinate, Cell>();
    private HashMap<Coordinate, Cell> unvisited = new HashMap<Coordinate, Cell>();
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

            Debugger.log("Cleaning cell (" + x + ", " + y + ")");

            Cell cell = sensorService.getCell(x, y);
            visited.put(currentPos, cell);

            registerCells();

        } while (!unvisited.isEmpty());

    }

    /**
     *
     */
    private void registerCells() {

        int x = (int) currentPos.getX();
        int y = (int) currentPos.getY();

        Cell cell = sensorService.getCell(x, y);
        visited.put(currentPos, cell);

        Coordinate topCoordinate = new Coordinate(x, y + 1);
        if (!visited.containsKey(topCoordinate))
            unvisited.put(topCoordinate, sensorService.getCell(x, y + 1));

        Coordinate bottomCoordinate = new Coordinate(x, y - 1);
        if (!visited.containsKey(bottomCoordinate))
            unvisited.put(bottomCoordinate, sensorService.getCell(x, y - 1));

        Coordinate leftCoordinate = new Coordinate(x - 1, y);
        if (!visited.containsKey(leftCoordinate))
            unvisited.put(leftCoordinate, sensorService.getCell(x - 1, y));

        Coordinate rightCoordinate = new Coordinate(x + 1, y);
        if (!visited.containsKey(rightCoordinate))
            unvisited.put(rightCoordinate, sensorService.getCell(x + 1, y));

    }

}