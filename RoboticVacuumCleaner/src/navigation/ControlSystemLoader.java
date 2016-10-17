package navigation;

import floor.Coordinate;

public class ControlSystemLoader {
	private Navigation navigation;
	private static ControlSystemLoader controlSystemLoader;
	private ControlSystemLoader(){
		navigation = Navigation.getInstance();
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
}
