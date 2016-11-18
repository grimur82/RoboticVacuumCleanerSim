package control;

import util.Debugger;

class Sweeper{
	private static Sweeper sweeper = new Sweeper();
	private int dirtCapacity;
	private double powerCapacity;
	private boolean cleaningCycle;
	private Sweeper(){
		dirtCapacity = 50;
		powerCapacity = 100.0;
		cleaningCycle = false;
	}
	static Sweeper getInstance(){
		return sweeper;
	}
	// Recharge Sweeper.
	void reCharged(){
		dirtCapacity = 50;
		powerCapacity = 100.0;
		Debugger.log("Sweeper is recharged");
	}
	// Check how much dirt capacity sweeper has left.
	int checkDirtCapacity(){
		return dirtCapacity;
	}

	// Decrement sweeper dirt capacity.
	void decreaseDirtCapacity(){
		if (dirtCapacity > 0)
			dirtCapacity--;
	}

	// Power capacity left on Sweeper.
	double checkPowerCapacity(){
		return powerCapacity;
	}
	// Calculate current powerCapacity.
	double decreasePowerCapacity(double powerConsumed){
		powerCapacity =  powerCapacity - powerConsumed;
		return powerCapacity;
	}
	
	// Sets the status of cleaning cycle
	boolean setCleaningCycleStatus(boolean status){
		cleaningCycle = status;
		return cleaningCycle;
	}
	
	// Check if cleaning cycle is done
	boolean checkCleaningCycle(){
		return cleaningCycle;
	}
	
	
}
