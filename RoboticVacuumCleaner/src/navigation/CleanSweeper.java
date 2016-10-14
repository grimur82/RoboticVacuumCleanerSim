package navigation;

public class CleanSweeper{
	private String dir;
	private int capacity;
	private static CleanSweeper cleanSweeper = new CleanSweeper();
	private CleanSweeper(){		
	}
	public void setCapacity(int cap){
		capacity = cap;
	}
	public void setDirection(String direction){
		dir = direction;
	}
	public static CleanSweeper getInstance(){
		return cleanSweeper;
	}
	public int getCapacity() {
		return capacity;
	}
	public String getDirection(String dir) {
		return dir;
	}
	public void shutDown() {
		dir = "shutdown";
	}
}