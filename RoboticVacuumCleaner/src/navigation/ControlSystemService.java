package navigation;

import floor.Coordinate;

public class ControlSystemService {
	private static ControlSystemLoader controlSystemLoader;
	private static ControlSystemService controlSystemService;
	private ControlSystemService() {
		controlSystemLoader = ControlSystemLoader.getInstance();
	}
	public static ControlSystemService getInstance(){
		if (controlSystemService == null)
			controlSystemService = new ControlSystemService();

		return controlSystemService;
	}
	public void setNavigation(Coordinate s){
		controlSystemLoader.setNavigation(s);
	}
}
