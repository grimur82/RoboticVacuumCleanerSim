package floor;

import util.Debugger;

import java.net.URISyntaxException;
import java.util.*;

import controllers.UtilityController;
import sensor.FloorPlan;

/**
 * A cell object.
 */
public class Cell {

    private SurfaceType surfaceType;
    private EnumSet<Obstacle> obstacles;

	// Dirt level
    private int dirt;

	private boolean stairs;
	private boolean chargingBase;
	private Coordinate c;

    public Cell(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
        this.obstacles = EnumSet.noneOf(Obstacle.class);
		this.stairs = false;
		this.chargingBase = false;

		// Randomly set the dirt level
		Random r = new Random();
		this.dirt = r.nextInt(2) + 1;
    }
    public void setCoordinate(double x, double y){
    	c = new Coordinate(x,y);
    }
    public Coordinate getCoordinate(){
    	return c;
    }

	/**
	 * Sets whether this cell has stairs.
	 *
	 */
    public void setChargingBase(){
    	this.chargingBase = true;
    }
    public boolean typeChargingBase(){
    	return chargingBase;
    }
	public void setStairs(boolean stairs) {
		if (stairs) {
			Debugger.log("stairs set");
		}
		this.stairs = stairs;
	}

	/**
	 * Whether this cell has stairs.
	 *
	 * @return Presence of stairs.
	 */
	public boolean hasStairs() {
		return stairs;
	}

	/**
	 * Clean one unit of dirt.
	 */
    public void clean() {
        if (dirt > 0)
			dirt--;
    }

	/**
	 * Get level of dirt.
	 *
	 * @return Dirt level
	 */
	public int getDirt() {
		return dirt;
	}

    public SurfaceType getSurfaceType() {
        return surfaceType;
    }

    public void setName(String name) {
    }

    public void setObstacle(String obstacle) {
        obstacles.add(Obstacle.valueOf(obstacle));
    }

    public boolean blocked(Obstacle... obs) {
        for (Obstacle o : obs)
            if (!obstacles.contains(o))
                return false;

        return true;
    }
    
    public Set<Obstacle> getObstacles(){
    	return obstacles;
    }

	/**
	 * Coordinate representation of a cell adjacent to a given cell.
	 *
	 * @param o Direction/obstacle
	 * @param c Coordinate
	 * @return Coordinate in the direction of c
	 * @throws URISyntaxException 
	 */
    public static Coordinate adjacent(Obstacle o, Coordinate c) throws URISyntaxException {

		int x = (int) c.getX();
		int y = (int) c.getY();
		int xAxes = FloorPlan.getInstance().getFloorPlan().length;
		int currentX = FloorPlan.getInstance().getFloorPlan()[x].length;
		switch (o) {
			case TOP:
				if(UtilityController.getServices().checkInBounds(x,y-1, xAxes,currentX))
					return new Coordinate(x, y-1);
			case BOTTOM:
				if(UtilityController.getServices().checkInBounds(x,y+1, xAxes,currentX))
					return new Coordinate(x, y+1);
			case LEFT:
				if(UtilityController.getServices().checkInBounds(x-1,y, xAxes,currentX))
					return new Coordinate(x-1, y);
			case RIGHT:
				if(UtilityController.getServices().checkInBounds(x+1,y, xAxes,currentX))
					return new Coordinate(x+1,y);
			default:
				return null;
		}
	}
}