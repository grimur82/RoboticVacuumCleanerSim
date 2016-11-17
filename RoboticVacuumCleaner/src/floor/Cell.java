package floor;

import sensor.FloorPlan;
import sensor.SensorServices;
import util.Debugger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import control.Sweeper;

/**
 * A cell object.
 */
public class Cell {

    private String name;
    private SurfaceType surfaceType;
    private EnumSet<Obstacle> obstacles;

	// Dirt level
    private int dirt;

	private boolean stairs;
	private boolean chargingBase;
	private Coordinate c;
    private ArrayList<Coordinate> parents;
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
    
    public ArrayList<Coordinate> getParents(){
    	if(parents == null){
    		parents = new ArrayList<Coordinate>();
    	}
    	return parents;
    }
    	

	/**
	 * Sets whether this cell has stairs.
	 *
	 * @param stairs Presence of stairs.
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
        this.name = name;
    }

    public String getName() {
        return name;
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
    
    public EnumSet<Obstacle> getObstacles(){
    	return obstacles;
    }

}