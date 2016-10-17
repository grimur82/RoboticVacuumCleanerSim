package navigation;

import floor.Coordinate;

public class ControlSystemLoader {
	private Navigation navigation;
	private CleanSweeper cleanSweeper;
	private static ControlSystemLoader controlSystemLoader;
	private ControlSystemLoader(){
		navigation = Navigation.getInstance();
		cleanSweeper = CleanSweeper.getInstance();
	}
	public static ControlSystemLoader getInstance(){
		if (controlSystemLoader == null)
			controlSystemLoader = new ControlSystemLoader();

		return controlSystemLoader;
	}
	public void setNavigation(Coordinate s){
		navigation.setX(s.getX());
		navigation.setY(s.getY());
	}
	public void setSweeperDirection(String direction){
		cleanSweeper.setDirection(direction);
	}
	public void shutdownSweeper(){
		cleanSweeper.shutDown();
	}
}
