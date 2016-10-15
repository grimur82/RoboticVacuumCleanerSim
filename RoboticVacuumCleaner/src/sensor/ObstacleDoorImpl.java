package sensor;

public class ObstacleDoorImpl implements Obstacle{
	private int x;
	private int y;
	public ObstacleDoorImpl(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getType() {
		return "door";
	}

}
