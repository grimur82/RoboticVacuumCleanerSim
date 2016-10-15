package sensor;

public class SurfaceBareImpl implements Surface{
	private int x;
	private int y;
	private String type;
	public SurfaceBareImpl(String t, int x, int y){
		this.x = x;
		this.y = y;
		this.type = t;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public String getType() {
		return "a";
	}
}
