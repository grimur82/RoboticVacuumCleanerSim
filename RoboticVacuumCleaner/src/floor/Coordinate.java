package floor;

public class Coordinate {

    private double x;
    private double y;
    private int distance = -1;
    private double powerDistance = 0;
    private Coordinate neighbor;

	public Coordinate() {
		this.x = 0;
		this.y = 0;
	}

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
        distance = -1;
        neighbor = null;
    }

	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}
    
 // Set shortest path distance from current sweeper position towards the charging base.
    public void setDistance(int distance){
    	this.distance = distance;
    }
    // Get shortest path distance from current sweeper position towards the charging base.
    public int getDistance(){
    	return distance;
    }
 // Set shortest path distance from current sweeper position towards the charging base.
    public void setPowerDistance(double distance){
    	this.powerDistance = distance;
    }
    // Get shortest path distance from current sweeper position towards the charging base.
    public double getPowerDistance(){
    	return powerDistance;
    }
    // Set neighbors for location Sweeper is on.
    public void setNeighbor(Coordinate c){
    	neighbor = c;
    }
    // Get the neighbors, Sweeper is currently on.
    public Coordinate getNeighbor(){
    	return neighbor;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Compares two coordinates.
     *
     * @param c Coordinate.
     * @return  1 if above c.x and c.y
     *         -1 if below c.x and c.y
     *          0 if equals to c.x and c.y
     *         -2 if none of the above
     */
    public int compareTo(Coordinate c) {
        if (x > c.getX() && y > c.getY())
            return 1;
        if (x < c.getX() && y < c.getY())
            return -1;
        if (x == c.getX() && y == c.getY())
            return 0;
        return -2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int)x;
        hash = 31 * hash + (int)y;
        return hash;
    }

    @Override
    public boolean equals(Object c) {
        if (c == null
                || getClass() != c.getClass()
                || !(c instanceof Coordinate))
            return false;

        return x == ((Coordinate)c).getX() && y == ((Coordinate)c).getY();
    }

	/**
	 * Conversion to string.
	 *
	 * @return String format.
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}