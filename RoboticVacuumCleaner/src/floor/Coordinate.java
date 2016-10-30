package floor;

import java.util.ArrayList;

import util.Debugger;

public class Coordinate {

    private double x = 0;
    private double y = 0;
    private int distance;
    private ArrayList<Coordinate> parents;
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
        this.distance = -1;
        this.parents = new ArrayList<Coordinate>();
    }
    public void setDistance(int distance){
    	this.distance = distance;
    }
    public int getDistance(){
    	return distance;
    }
    public ArrayList<Coordinate> getParents(){
    	if(parents == null){
    		parents = new ArrayList<Coordinate>();
    	}
    	return parents;
    }
    public void setParents(ArrayList<Coordinate> c){
    	
    	parents = c;
    }
    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate() {}

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

}