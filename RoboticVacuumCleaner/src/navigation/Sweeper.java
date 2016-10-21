package navigation;

import util.Debugger;

public class Sweeper{
	public static Sweeper sweeper = new Sweeper();
	private int dirtCapacity;
	private int powerCapacity;
	private Sweeper(){
		dirtCapacity = 50;
		powerCapacity = 100;
	}
	public static Sweeper getInstance(){
		return sweeper;
	}
	public int checkDirtCapacity(){
		return dirtCapacity;
	}
	public void decreaseDirtCapacity(){
		dirtCapacity--;
	}
	public int checkPowerCapacity(){
		return powerCapacity;
	}
	
	public int decreasePowerCapacity(int powerConsumed){
		return powerCapacity - powerConsumed;
	}
	
	
}
