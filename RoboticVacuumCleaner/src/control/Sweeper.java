package control;

import util.Debugger;

public class Sweeper{
	public static Sweeper sweeper = new Sweeper();
	private int dirtCapacity;
	private double powerCapacity;
	private boolean cleaningCycle;
	private Sweeper(){
		dirtCapacity = 50;
		powerCapacity = 100.0;
		cleaningCycle = false;
	}
	public static Sweeper getInstance(){
		return sweeper;
	}
	// Recharge Sweeper.
	public void reCharged(){
		dirtCapacity = 50;
		powerCapacity = 100.0;
		Debugger.log("Sweeper is recharged");
	}
	// Check how much dirt capacity sweeper has left.
	public int checkDirtCapacity(){
		return dirtCapacity;
	}

	// Decrement sweeper dirt capacity.
	public void decreaseDirtCapacity(){
		if (dirtCapacity > 0)
			dirtCapacity--;
	}

	// Power capacity left on Sweeper.
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
