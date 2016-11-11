package control;

import floor.Cell;
import floor.Coordinate;
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

	// Clean cells
    private HashMap<Coordinate, Cell> clean = new HashMap<>();

	// Dirty cells
    private HashMap<Coordinate, Cell> dirty = new HashMap<>();

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
    private void setPosition(Coordinate currentPos) {
        this.currentPos = currentPos;
    }
    
    // Calculate power consumed by movement
    public double movementCharge(Cell cellA, Cell cellB){
    	double chargeA = cellA.getSurfaceType().getPowerUsed();
    	double chargeB = cellB.getSurfaceType().getPowerUsed();
    	return (chargeA+chargeB)/2;  	
    }
    
    // Decrease power capacity due to movement
    public void decreasePowerMove(double total){
    	sweeper.decreasePowerCapacity(total);
    }


    // Decrease power capacity due to cleaning
    public void decreasePowerClean(Cell cell){
    	double currentCellCharge = cell.getSurfaceType().getPowerUsed();
    	sweeper.decreasePowerCapacity(currentCellCharge);
    }

    // Check power capacity
    public double checkPowerCapacity(){
    	return sweeper.checkPowerCapacity();
    }

    // Sets status of cleaning cycle
    public boolean setCleaningStat(boolean status){
    	return sweeper.setCleaningCycleStatus(status);
    }
    
    // Checking status of cleaning cycle
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
        if (!dirty.isEmpty()) {
        	setCleaningStat(false);
        }

        do {

			// Current cell data
			int x = (int) currentPos.getX();
			int y = (int) currentPos.getY();
			Cell cell = sensorService.getCell(x, y);
			Debugger.log("Arrived at cell (" + x + ", " + y + ")");

			// Checks if Sweeper is out of power or no dirt capacity left.
            if(Sweeper.getInstance().checkDirtCapacity() == 0 || Sweeper.getInstance().checkPowerCapacity() <= 0.0){
            	Debugger.log("Sweeper needs to go back to charge at base");
            	// Call sweeper to go back to base.
            	SweeperServices.getInstance().backToBase();
            	x = (int) sensorService.getCell(0,0).getCoordinate().getX(); // Base
                y = (int) sensorService.getCell(0,0).getCoordinate().getY(); // Base
                currentPos.setX(0);
            	currentPos.setY(0);
                cell = sensorService.getCell(x, y);
                // Sweeper is charged.
                SweeperServices.getInstance().reCharge();
                Debugger.log("Cleaning cycle done: "+ checkCleaningStat());
            }

            //gets surface type of current cell
            Debugger.log("Surface type: " + cell.getSurfaceType());

			// If dirt is present
			int oldDirt;
			if ((oldDirt = cell.getDirt()) > 0) {

				// Clean
				cell.clean();
				int currentDirt = cell.getDirt();
				Debugger.log("Floor Dirty: cleaning, dirt level " + oldDirt + " -> " + currentDirt);

				// Mark if clean or dirty
				if (currentDirt == 0) {
					dirty.remove(currentPos);
					clean.put(currentPos, cell);
				}

				// Decrease sweeper's dirt capacity
				int oldCapacity = sweeper.checkDirtCapacity();
				sweeper.decreaseDirtCapacity();
				Debugger.log("Dirt capacity: " + oldCapacity + " -> " + sweeper.checkDirtCapacity());

				// Decrease sweeper's power capacity
				double oldPower = sweeper.checkPowerCapacity();
				int powerUsed = cell.getSurfaceType().getPowerUsed();
				sweeper.decreasePowerCapacity(powerUsed);
				Debugger.log("Power capacity: " + oldPower + " -> " + sweeper.checkPowerCapacity()
						+ " (" + powerUsed + " used");

			} else {
				// If dirt is not present
				Debugger.log("Floor Clean: CleanSweeper moves on.");
			}

			// Navigator and update to next position
			Navigator navigator = new Navigator(currentPos, clean, dirty);
			clean = navigator.getClean();
			dirty = navigator.getDirty();
			Coordinate chosenCoord = navigator.getCurrentPos();
			setPosition(chosenCoord);

			//Decreases power capacity due to movement
			Cell nextCell = sensorService.getCell(chosenCoord);
            decreasePowerMove(movementCharge(cell, nextCell));
            Debugger.log("Power for movement from current cell to next cell: " + movementCharge(cell, nextCell));
			Debugger.log("New power capacity will be: "+ checkPowerCapacity());

			// Show map
			Visualizer.getInstance().print();

        } while (checkCleaningStat() == false);
        //cleaning cycle done when all surfaces are clean
        setCleaningStat(true);
        Debugger.log("Cleaning done!");
    }

    public void shutDownSweeper(){
    	System.out.println("Sweeper is full: Stopped");
    	return;
    }

	/**
	 * Document this and surrounding cells as clean or dirty.
	 *
	 * @param free Available directions.
	 */
    ArrayList<Coordinate> getNeighbors(Coordinate c){
    	int x = (int) c.getX();
		int y = (int) c.getY();
		ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();
		Cell cell = sensorService.getCell(x,y);
		try{
			if(clean.containsKey(sensorService.getInstance().getCell(x+1,y).getCoordinate())){
				neighbors.add(sensorService.getInstance().getCell(x+1,y).getCoordinate());
			}
			if(clean.containsKey(sensorService.getInstance().getCell(x-1,y).getCoordinate())){
				neighbors.add(sensorService.getInstance().getCell(x-1,y).getCoordinate());
			}
			if(clean.containsKey(sensorService.getInstance().getCell(x,y-1).getCoordinate())){
				neighbors.add(sensorService.getInstance().getCell(x,y-1).getCoordinate());
			}
			if(clean.containsKey(sensorService.getInstance().getCell(x,y+1).getCoordinate())){
				neighbors.add(sensorService.getInstance().getCell(x,y+1).getCoordinate());
			}
			
		}catch(ArrayIndexOutOfBoundsException ex){
			
		}
		catch(NullPointerException ex){
			
		}
		return neighbors;

    	
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