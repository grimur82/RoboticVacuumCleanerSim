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
	public void reCharged(){
		dirtCapacity = 50;
		powerCapacity = 100.0;
	}
	public int checkDirtCapacity(){
		return dirtCapacity;
	}
	public void decreaseDirtCapacity(){
		dirtCapacity--;
	}
	public double checkPowerCapacity(){
		return powerCapacity;
	}
	
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
