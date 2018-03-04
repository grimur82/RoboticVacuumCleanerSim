package control;

import controllers.SweeperController;
import floor.Cell;
import floor.Coordinate;
import util.Debugger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import controllers.SensorController;

public class ControlSystemService {

	// Current position of the sweeperController
    private Coordinate currentPos;

	// Single instance
    private static ControlSystemService controlSystemService;

	// Clean cells
    private Map<Coordinate, Cell> clean = new HashMap<>();

	// Dirty cells
    private Map<Coordinate, Cell> dirty = new HashMap<>();
    /**
     * Singleton.
     *
     * @return instance
     */
	public static ControlSystemService getServices() {
        if (controlSystemService == null)
            controlSystemService = new ControlSystemService();

        return controlSystemService;
    }
    /**
     * Set current position of sweeperController.
     *
     * @param currentPos Current position.
     */
    private void setPosition(Coordinate currentPos) {
        this.currentPos = currentPos;
    }
    
    // Calculate power consumed by movement
    private double movementCharge(Cell cellA, Cell cellB){
    	double chargeA = cellA.getSurfaceType().getPowerUsed();
    	double chargeB = cellB.getSurfaceType().getPowerUsed();
    	return (chargeA+chargeB)/2;  	
    }
	double calcCharge(Cell cellA, Cell cellB){
    	double chargeA = cellA.getSurfaceType().getPowerUsed();
    	double chargeB = cellB.getSurfaceType().getPowerUsed();
    	return chargeA+chargeB;
    }
    
    // Decrease power capacity due to movement
    private void decreasePowerMove(double total){
    	SweeperController.getServices().decreasePowerCapacity(total);
    }

    // Check power capacity
    private double checkPowerCapacity(){
    	return SweeperController.getServices().checkPowerCapacity();
    }

    // Sets status of cleaning cycle
    private boolean setCleaningStat(boolean status){
    	return SweeperController.getServices().setCleaningCycleStatus(status);
    }
    
    // Checking status of cleaning cycle
    private boolean checkCleaningStat(){
    	return SweeperController.getServices().checkCleaningCycle();
    }

	/**
	 * Cleaning engine.
	 * @throws URISyntaxException 
	 */
    public void beginCleaning() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        ControlSystemService.getServices().setPosition(SensorController.getServices().getStartPosition());
        if (!dirty.isEmpty()) {
        	ControlSystemService.getServices().setCleaningStat(false);
        }
        do {
			int x = (int) Math.round(currentPos.getX());
			int y = (int) Math.round(currentPos.getY());
			Cell cell = SensorController.getServices().getCell(x, y);
			Debugger.log("Arrived at cell (" + x + ", " + y + ")");

            //gets surface type of current cell
            Debugger.log("Surface type: " + cell.getSurfaceType());

			// If dirt is present
			if (cell.getDirt() > 0) {
				// Clean
				cell.clean();
				Debugger.log("Floor Dirty: cleaning, dirt level " + cell.getDirt());
				// Mark if clean or dirty
				if (cell.getDirt() == 0) {
					dirty.remove(currentPos);
					clean.put(currentPos, cell);
				}
				// Decrease sweeperController's dirt capacity
				SweeperController.getServices().decreaseDirtCapacity();
				Debugger.log("Dirt capacity: " + SweeperController.getServices().checkDirtCapacity());

				// Decrease sweeperController's power capacity
				SweeperController.getServices().decreasePowerCapacity(cell.getSurfaceType().getPowerUsed());
				Debugger.log("Power capacity: " + SweeperController.getServices().checkPowerCapacity()
						+ " (" + cell.getSurfaceType().getPowerUsed() + " used");

			} else {
				// If dirt is not present
				Debugger.log("Floor Clean: CleanSweeper moves on.");
			}
			Coordinate nextPosition = moveToNextPosition();
			decreasePowerMovement(nextPosition,cell);
			ControlSystemService.getServices().setPosition(nextPosition);
	//		// Show map
		//	Visualizer.getServices().print();

        } while (!checkCleaningStat());
        //cleaning cycle done when all surfaces are clean
        setCleaningStat(true);
        Debugger.log("Cleaning done!");

    }
    public void decreasePowerMovement(Coordinate chosenCoord, Cell cell) throws URISyntaxException {
		if(chosenCoord != null){
			//Decreases power capacity due to movement
			Cell nextCell = SensorController.getServices().getCell(chosenCoord);
			decreasePowerMove(movementCharge(cell, nextCell));
			Debugger.log("Power for movement from current cell to next cell: " + movementCharge(cell, nextCell));
			Debugger.log("New power capacity will be: "+ checkPowerCapacity());
		}
	}
	public Coordinate moveToNextPosition() throws URISyntaxException {
		// Navigator and update to next position
		Navigator navigator = new Navigator(currentPos, clean, dirty);
		clean = navigator.getClean();
		dirty = navigator.getDirty();
		Coordinate chosenCoord = navigator.getCurrentPos();
		ControlSystemService.getServices().setPosition(chosenCoord);
		return navigator.getCurrentPos();
	}
	void shutDownSweeper(){
    	System.out.println("SweeperController is full: Stopped");
    	System.exit(1);
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