package sensor;

public class ObstacleChargingStationImpl implements Obstacle{
	private int x;
	private int y;
	public ObstacleChargingStationImpl(int x, int y){
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
		return "chargingstation";
	}

}
