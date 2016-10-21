package navigation;

import util.Debugger;

public class Sweeper{
	public static Sweeper sweeper = new Sweeper();
	private int dirtCapacity;
	private Sweeper(){
		dirtCapacity = 50;
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
	
}
