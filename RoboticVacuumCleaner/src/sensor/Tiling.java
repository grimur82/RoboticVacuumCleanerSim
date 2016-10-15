package sensor;

public interface Tiling {
	int getFromX();
	int getToX();
	int getFromY();
	int getToY();
	String getType();
	Obstacle getDoors();
}
