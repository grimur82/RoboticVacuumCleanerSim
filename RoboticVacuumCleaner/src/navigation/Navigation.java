package navigation;

public class Navigation {
	private static Navigation navigation;
	private double x;
	private double y;
	private Navigation(){
	}
	public static Navigation getInstance(){
		if (navigation == null)
			navigation = new Navigation();

		return navigation;
	}
	public void setX(double x){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
}
