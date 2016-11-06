package floor;

import sensor.FloorPlan;
import sensor.SensorServices;
import util.Debugger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

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
    private boolean dirt;
	private boolean stairs;
	private boolean chargingBase;
	private Coordinate c;
    private ArrayList<Coordinate> parents;
    public Cell(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
        this.obstacles = EnumSet.noneOf(Obstacle.class);
        this.dirt = true;
		this.stairs = false;
		this.chargingBase = false;
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
    // For neighbors of sweeper.
    public void setParents(){
    	if(parents == null){
    		parents = new ArrayList<Coordinate>();
    	}
    	// Corners:
    	if(c.getX() == 0 && c.getY() ==0){		
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX(), c.getY()+1));
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX()+1, c.getY()));
    		return;
    	}
    	if(c.getX() == SensorServices.getInstance().getFloorPlan().length-1 && c.getY() == 0){
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX(), c.getY()+1));
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX()-1, c.getY()));
    		return;
    	}
    	if(c.getX() == 0 && c.getY() ==SensorServices.getInstance().getFloorPlan().length-1){
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX()+1, c.getY()));
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX(), c.getY()-1));
    		return;
    	}
    	if(c.getX() == SensorServices.getInstance().getFloorPlan().length-1 && c.getY() ==SensorServices.getInstance().getFloorPlan().length-1){
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX()-1, c.getY()));
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX(), c.getY()-1));
    	  	return;
    	}
    	// Sides:
    	if(c.getX() >0 && c.getX() < SensorServices.getInstance().getFloorPlan().length-1 && c.getY() ==0){
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX(), c.getY()+1));
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX()-1, c.getY()));
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX()+1, c.getY()));
    		return;
    	}
    	if(c.getX() ==0 && c.getX() > 0 && c.getY() < SensorServices.getInstance().getFloorPlan().length-1){
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX(), c.getY()+1));
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX(), c.getY()-1));
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX()+1, c.getY()));
    		return;
    	}
    	if(c.getX() >0 && c.getX() < SensorServices.getInstance().getFloorPlan().length-1 && c.getY() == SensorServices.getInstance().getFloorPlan().length-1){
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX()+1, c.getY()));
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX(), c.getY()-1));
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX()-1, c.getY()));
    		return;
    	}
    	if(c.getX() == SensorServices.getInstance().getFloorPlan().length-1 && c.getY() > 0 && c.getY() < SensorServices.getInstance().getFloorPlan().length-1){
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX()-1, c.getY()));
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX(), c.getY()+1));
    		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX(), c.getY()-1));
    		return;
    	}
    	// Else:
    	else{
    		if(c.getX() > 0 && c.getX() < SensorServices.getInstance().getFloorPlan().length-1 && c.getY() >0 && c.getY() < SensorServices.getInstance().getFloorPlan().length-1){
    			parents.add(FloorPlan.getInstance().getCellCordinates(c.getX(), c.getY()-1));
        		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX(), c.getY()+1));
        		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX()+1, c.getY()));
        		parents.add(FloorPlan.getInstance().getCellCordinates(c.getX()-1, c.getY()));
        		return;	
    		}
    	}
    	}
    	
    public boolean checkDirt() {
    	if(dirt){
    		Debugger.log("Floor Dirty: CleanSweeper cleans.");
    		Sweeper.getInstance().decreaseDirtCapacity();
    		Debugger.log("Dirt Capacity: " + Sweeper.getInstance().checkDirtCapacity());
        	Debugger.log("Power consumed from cleaning: "+ getSurfaceType().getPowerUsed());
        	Sweeper.getInstance().decreasePowerCapacity(getSurfaceType().getPowerUsed());
        	Debugger.log("Power capacity: "+ Sweeper.getInstance().checkPowerCapacity());

    	}
    	else{
    		Debugger.log("Floor Clean: CleanSweeper moves on.");
    	}
        return dirt;
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

    public void clean() {
        dirt = false;
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