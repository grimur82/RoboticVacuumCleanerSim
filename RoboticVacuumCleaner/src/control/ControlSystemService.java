package control;

import floor.Cell;
import floor.Coordinate;
import floor.Obstacle;
import sensor.SensorServices;
import util.Debugger;

import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import util.Visualizer;

public class ControlSystemService {

	// Current position of the sweeper
    private Coordinate currentPos;

	// Single instance
    private static ControlSystemService controlSystemService;

	// Visited cells
    private HashMap<Coordinate, Cell> visited = new HashMap<>();

	// Unvisited cells
    private HashMap<Coordinate, Cell> unvisited = new HashMap<>();

	// Sensor simulator to check data against
    private SensorServices sensorService;
    private Sweeper sweeper = Sweeper.getInstance(); 
    // initialise Sweeper.class
    // dirt capacity
    // dirt space available
    /**
     * The control system.
     */
	private ControlSystemService() {
        Debugger.log("Starting control system");
    }

    /**
     * Singleton.
     *
     * @return instance
     */
	public static ControlSystemService getInstance() {
        if (controlSystemService == null)
            controlSystemService = new ControlSystemService();

        return controlSystemService;
    }

	/**
	 * Sets sensor.
	 *
	 * @param sL Sensor.
	 */
	public void setSensor(SensorServices sL) {
		sensorService = sL;
	}

    /**
     * Set current position of sweeper.
     *
     * @param currentPos Current position.
     */
    public void setPosition(Coordinate currentPos) {
        this.currentPos = currentPos;
    }
    public void decreaseSweepDirtCapacity(){
    	sweeper.decreaseDirtCapacity();
    }
    public int checkSweepDirtCapacity(){
    	return sweeper.checkDirtCapacity();
    }
    
    
    public double movementCharge(Cell cellA, Cell cellB){
    	double chargeA = cellA.getSurfaceType().getPowerUsed();
    	double chargeB = cellB.getSurfaceType().getPowerUsed();
    	return (chargeA+chargeB)/2;  	
    }
    
    public void decreasePowerMove(double total){
    	sweeper.decreasePowerCapacity(total);
    }
    
    
    public void decreasePowerClean(Cell cell){
    	double currentCellCharge = cell.getSurfaceType().getPowerUsed();
    	sweeper.decreasePowerCapacity(currentCellCharge);
    }
    
    public double checkPowerCapacity(){
    	return sweeper.checkPowerCapacity();
    }
    
    public boolean setCleaningStat(boolean status){
    	return sweeper.setCleaningCycleStatus(status);
    }
    
    public boolean checkCleaningStat(){
    	return sweeper.checkCleaningCycle();
    }

	/**
	 * Cleaning engine.
	 *
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
    public void clean() throws ParserConfigurationException, SAXException, IOException {
        setPosition(sensorService.getStartPosition());
        
        if(!unvisited.isEmpty()){
        	setCleaningStat(false);
        }

        do {

            int x = (int) currentPos.getX();
            int y = (int) currentPos.getY();
            Cell cell = sensorService.getCell(x, y);
			Visualizer.getInstance().print(visited);

            if(Sweeper.getInstance().checkDirtCapacity() == 0 || Sweeper.getInstance().checkPowerCapacity() <= 0.0){
            	Debugger.log("Sweeper needs to go back to charge at base");
            	SweeperServices.getInstance().backToBase();
            	x = (int) sensorService.getCell(0,0).getCoordinate().getX(); // Base
                y = (int) sensorService.getCell(0,0).getCoordinate().getY(); // Base
                currentPos.setX(0);
            	currentPos.setY(0);
                cell = sensorService.getCell(x, y);
                SweeperServices.getInstance().reCharge();
                Debugger.log("Cleaning cycle done: "+ checkCleaningStat());
            }
           
            //shuts down when dirt and power capacity is 0 for now
            Debugger.log("Cleaning cell (" + x + ", " + y + ")");

            Debugger.log("Surface type: "+ cell.getSurfaceType());
            cell.checkDirt();

            
            // Clean if dirt
            //

            cell.clean();

			// Dynamically find obstacles
			EnumSet<Obstacle> obstacles = ObstacleDetector.generate(currentPos);

			// Get free directions
			EnumSet<Obstacle> free = EnumSet.allOf(Obstacle.class);
			free.removeAll(obstacles);

            // Update visited and unvisited cells
            registerCells(free);

            // Choose next random cell
			Coordinate chosenCoord = RandomPosition.generate(currentPos, free);

			// Set next cell
			setPosition(chosenCoord);
			Cell nextCell = sensorService.getCell(chosenCoord);
            
            decreasePowerMove(movementCharge(cell, nextCell));
            Debugger.log("Power for movement from current cell to next cell: "+ movementCharge(cell, nextCell));
        	Debugger.log("New power capacity will be: "+ checkPowerCapacity());
          
        

            // Delay for visualization
            if (Debugger.getMode()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        } while (checkCleaningStat() == false);
        setCleaningStat(true);
        Debugger.log("Cleaning done!");
    }
    private boolean checkDirt(int x, int y) throws ParserConfigurationException, SAXException, IOException{
    	return sensorService.getCell(x, y).checkDirt();
    }
    public void shutDownSweeper(){
    	System.out.println("Sweeper is full: Stopped");
    	return;
    }
    /**
     * Document this and surrounding cells as visited or unvisited.
     *
     * TODO: Conflict with dirt levels. Assumes visited = clean, unvisited = dirty.
     */

	/**
	 * Document this and surrounding cells as visited or unvisited.
	 *
	 * @param free Available directions.
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

	/**
	 * Get current position.
	 *
	 * @return Current position.
	 */
	public Coordinate getCurrentPos() {
		return currentPos;
	}

}