package navigation;

public class Navigation {
	private static Navigation navigation = new Navigation();
	private int x;
	private int y;
	private Navigation(){
	}
	public static Navigation getInstance(){
		return navigation;
	}
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
}
