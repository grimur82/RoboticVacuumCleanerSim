package sensor;

public class ObstacleDirtImpl implements Obstacle{
	private int x;
	private int y;
	public ObstacleDirtImpl(int x, int y){
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
		return "dirt";
	}

}
