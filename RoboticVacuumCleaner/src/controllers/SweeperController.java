package controllers;

import util.Debugger;

public class SweeperController {
	private static SweeperController sweeperController = new SweeperController();
	private int dirtCapacity;
	private double powerCapacity;
	private boolean cleaningCycle;
	private SweeperController(){
		dirtCapacity = 50;
		powerCapacity = 100.0;
		cleaningCycle = false;
	}
	public static SweeperController getServices(){
		return sweeperController;
	}
	// Recharge SweeperController.
	public void reCharged(){
		dirtCapacity = 50;
		powerCapacity = 100.0;
		Debugger.log("SweeperController is recharged");
	}
	// Check how much dirt capacity sweeperController has left.
	public int checkDirtCapacity(){
		return dirtCapacity;
	}

	// Decrement sweeperController dirt capacity.
	public void decreaseDirtCapacity(){
		if (dirtCapacity > 0)
			dirtCapacity--;
	}

	// Power capacity left on SweeperController.
	public double checkPowerCapacity(){
		return powerCapacity;
	}
	// Calculate current powerCapacity.
	public double decreasePowerCapacity(double powerConsumed){
		powerCapacity =  powerCapacity - powerConsumed;
		return powerCapacity;
	}
	
	// Sets the status of cleaning cycle
	public boolean setCleaningCycleStatus(boolean status){
		cleaningCycle = status;
		return cleaningCycle;
	}
	
	// Check if cleaning cycle is done
	public boolean checkCleaningCycle(){
		return cleaningCycle;
	}
	
	
}
