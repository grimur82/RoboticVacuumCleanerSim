package navigation;

public class ControlSystemService {
	private ControlSystemLoader controlSystemLoader;
	private static ControlSystemService controlSystemService = new ControlSystemService();
	private ControlSystemService(){
		controlSystemLoader = controlSystemLoader.getInstance();
	}
	public static ControlSystemService getInstance(){
		return controlSystemService;
	}
	public void setNavigation(int x, int y){
		controlSystemLoader.setNavigation(x, y);
	}
	public void setSweeperDirection(String direction){
		controlSystemLoader.setSweeperDirection(direction);
	}
	public void shutdownSweeper(){
		controlSystemLoader.shutdownSweeper();
	}
}
