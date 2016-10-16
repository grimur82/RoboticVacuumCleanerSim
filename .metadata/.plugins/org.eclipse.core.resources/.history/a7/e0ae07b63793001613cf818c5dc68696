package navigation;

public class ControlSystemLoader {
	private Navigation navigation;
	private CleanSweeper cleanSweeper;
	private static ControlSystemLoader controlSystemLoader = new ControlSystemLoader();
	private ControlSystemLoader(){
		navigation = Navigation.getInstance();
		cleanSweeper = CleanSweeper.getInstance();
	}
	public static ControlSystemLoader getInstance(){
		return controlSystemLoader;
	}
	public void setNavigation(int x, int y){
		navigation.setX(x);
		navigation.setY(y);
	}
	public void setSweeperDirection(String direction){
		cleanSweeper.setDirection(direction);
	}
	public void shutdownSweeper(){
		cleanSweeper.shutDown();
	}
}
