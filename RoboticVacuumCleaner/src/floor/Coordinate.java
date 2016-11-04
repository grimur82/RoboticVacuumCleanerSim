package floor;

import java.util.ArrayList;

import sensor.SensorServices;
import util.Debugger;

public class Coordinate {

    private double x = 0;
    private double y = 0;
    private int distance = -1;
    private Coordinate neighbor;
    private ArrayList<Coordinate> parents;
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
        distance = -1;
        parents = new ArrayList<Coordinate>();
        neighbor = null;
    }
    public void setDistance(int distance){
    	this.distance = distance;
    }
    public int getDistance(){
    	return distance;
    }
    public void setNeighbor(Coordinate c){
    	neighbor = c;
    }
    public Coordinate getNeighbor(){
    	return neighbor;
    }
    public ArrayList<Coordinate> getParents(){
    	if(parents == null){
    		parents = new ArrayList<Coordinate>();
    	}
    	return parents;
    }
    public boolean checkObstacle(double x, double y, String check){
    	if(check.equals("bottom")){
    		Cell cell = SensorServices.getInstance().getCell((int)x, (int)y);
    		return SensorServices.getInstance().senseObstacleBottom(cell);
    	}
    	if(check.equals("left")){
    		Cell cell = SensorServices.getInstance().getCell((int)x, (int)y);
    		return SensorServices.getInstance().senseObstacleLeft(cell);
    	}
    	if(check.equals("right")){
    		Cell cell = SensorServices.getInstance().getCell((int)x, (int)y);
    		return SensorServices.getInstance().senseObstacleRight(cell);
    	}
    	if(check.equals("top")){
    		Cell cell = SensorServices.getInstance().getCell((int)x, (int)y);
    		return SensorServices.getInstance().senseObstacleTop(cell);
    	}
		return false;
    	
    }
    public void setParents(){
    	if(parents == null){
    		parents = new ArrayList<Coordinate>();
    	}
    	// Corners:
    	if(x == 0 && y ==0){
    		if(checkObstacle(x,y+1,"top") == false){
    			parents.add(new Coordinate(x,y+1));
    		}
    		if(checkObstacle(x,y-1,"right") == false){
    			parents.add(new Coordinate(x+1,y));
    		}
    		return;
    	}
    	if(x == SensorServices.getInstance().getFloorPlan().length-1 && y == 0){
    		if(checkObstacle(x,y+1,"top") == false){
    			parents.add(new Coordinate(x,y+1));
    		}
    		if(checkObstacle(x-1,y,"left") == false){
    			parents.add(new Coordinate(x-1,y));
    		}
    		return;
    	}
    	if(x == 0 && y ==SensorServices.getInstance().getFloorPlan().length-1){
    		if(checkObstacle(x+1,y,"right") == false){
    			parents.add(new Coordinate(x+1,y));
    		}
    		if(checkObstacle(x,y-1,"bottom") == false){
    			parents.add(new Coordinate(x+1,y));
    		}	
    		return;
    	}
    	if(x == SensorServices.getInstance().getFloorPlan().length-1 && y ==SensorServices.getInstance().getFloorPlan().length-1){
    		if(checkObstacle(x-1,y,"left") == false){
    			parents.add(new Coordinate(x-1,y));
    		}
    		if(checkObstacle(x,y-1,"bottom") == false){
    			parents.add(new Coordinate(x,y-1));
    		}
    	  	return;
    	}
    	// Sides:
    	if(x >0 && x < SensorServices.getInstance().getFloorPlan().length-1 && y ==0){
    		if(checkObstacle(x+1,y,"right") == false){
    			parents.add(new Coordinate(x+1,y));
    		}
    		if(checkObstacle(x-1,y,"left") == false){
    			parents.add(new Coordinate(x-1,y));
    		}
    		if(checkObstacle(x+1,y,"right") == false){
    			parents.add(new Coordinate(x+1,y));
    		}
    		return;
    	}
    	if(x ==0 && x > 0 && y < SensorServices.getInstance().getFloorPlan().length-1){
    		if(checkObstacle(x,y+1,"top") == false){
    			parents.add(new Coordinate(x,y+1));
    		}
    		if(checkObstacle(x,y-1,"bottom") == false){
    			parents.add(new Coordinate(x,y-1));
    		}
    		if(checkObstacle(x+1,y,"right") == false){
    			parents.add(new Coordinate(x+1,y));
    		}
    		return;
    	}
    	if(x >0 && x < SensorServices.getInstance().getFloorPlan().length-1 && y == SensorServices.getInstance().getFloorPlan().length-1){
    		if(checkObstacle(x+1,y,"right") == false){
    			parents.add(new Coordinate(x+1,y));
    		}
    		if(checkObstacle(x,y-1,"bottom") == false){
    			parents.add(new Coordinate(x,y-1));
    		}
    		if(checkObstacle(x-1,y,"left") == false){
    			parents.add(new Coordinate(x-1,y));
    		}
    		return;
    	}
    	if(x == SensorServices.getInstance().getFloorPlan().length-1 && y > 0 && y < SensorServices.getInstance().getFloorPlan().length-1){
    		if(checkObstacle(x-1,y,"left") == false){
    			parents.add(new Coordinate(x-1,y));
    		}
    		if(checkObstacle(x,y+1,"top") == false){
    			parents.add(new Coordinate(x,y+1));
    		}
    		if(checkObstacle(x,y-1,"bottom") == false){
    			parents.add(new Coordinate(x,y-1));
    		}
    		return;
    	}
    	// Else:
    	else{
    		if(x > 0 && x < SensorServices.getInstance().getFloorPlan().length-1 && y >0 && y < SensorServices.getInstance().getFloorPlan().length-1){
    			if(checkObstacle(x-1,y,"left") == false){
        			parents.add(new Coordinate(x-1,y));
        		}
        		if(checkObstacle(x,y+1,"top") == false){
        			parents.add(new Coordinate(x,y+1));
        		}
        		if(checkObstacle(x,y-1,"bottom") == false){
        			parents.add(new Coordinate(x,y-1));
        		}
        		if(checkObstacle(x+1,y,"right") == false){
        			parents.add(new Coordinate(x+1,y));
        		}
        		return;	
    		}		 
    	}
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