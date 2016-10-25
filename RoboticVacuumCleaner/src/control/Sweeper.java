package control;

public class Sweeper{
	public static Sweeper sweeper = new Sweeper();
	private int dirtCapacity;
	private double powerCapacity;
	private Sweeper(){
		dirtCapacity = 50;
		powerCapacity = 100.0;
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
	public double checkPowerCapacity(){
		return powerCapacity;
	}
	
	public double decreasePowerCapacity(double powerConsumed){
		powerCapacity =  powerCapacity - powerConsumed;
		return powerCapacity;
	}
	
	
}
