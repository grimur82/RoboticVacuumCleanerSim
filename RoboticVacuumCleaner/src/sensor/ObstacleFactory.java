package sensor;

public class ObstacleFactory {
	public static Obstacle getObstacle(String obstacle, int x, int y){
		if(obstacle.equals("door")){
			return new ObstacleDoorImpl(x,y);
		}
		if(obstacle.equals("dirt")){
			return new ObstacleDirtImpl(x,y);
		}
		if(obstacle.equals("stairs")){
			return new ObstacleStairsImpl(x,y);
		}
		if(obstacle.equals("charging")){
			return new ObstacleChargingStationImpl(x,y);
		}
		return null;
	}
}
