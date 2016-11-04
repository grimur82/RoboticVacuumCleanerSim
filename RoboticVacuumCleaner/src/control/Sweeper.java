package control;

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
	}
	// Check how much dirt capacity sweeper has left.
	public int checkDirtCapacity(){
		return dirtCapacity;
	}
	// Decrement sweeper dirt capacity.
	public void decreaseDirtCapacity(){
		dirtCapacity--;
	}
	// Check how much power. Sweeper has left.
	public double checkPowerCapacity(){
		return powerCapacity;
	}
	// Calculate current powerCapacity.
	public double decreasePowerCapacity(double powerConsumed){
		powerCapacity =  powerCapacity - powerConsumed;
		return powerCapacity;
	}
	
	public boolean setCleaningCycleStatus(boolean status){
		cleaningCycle = status;
		return cleaningCycle;
	}
	
	public boolean checkCleaningCycle(){
		return cleaningCycle;
	}
	
	
}
