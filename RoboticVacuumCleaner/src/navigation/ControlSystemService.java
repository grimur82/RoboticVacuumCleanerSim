package navigation;

import sensor.Obstacle;

public class ControlSystemService {
	private ControlSystemLoader controlSystemLoader;
	private static ControlSystemService controlSystemService = new ControlSystemService();
	private ControlSystemService(){
		controlSystemLoader = controlSystemLoader.getInstance();
	}
	public static ControlSystemService getInstance(){
		return controlSystemService;
	}
	public void setNavigation(Obstacle s){
		controlSystemLoader.setNavigation(s);
	}
	public void setSweeperDirection(String direction){
		if(direction.equals("left")){
			
		}
		controlSystemLoader.setSweeperDirection(direction);
	}
	public void shutdownSweeper(){
		controlSystemLoader.shutdownSweeper();
	}
}
