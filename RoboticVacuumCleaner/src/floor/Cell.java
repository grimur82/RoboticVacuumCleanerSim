package floor;

import util.Debugger;

import java.io.IOException;
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
    // TODO: flag if charging station
    // TODO: flag if stairs?*
    // (doc says to treat it as an obstacle. do we even need to differentiate
    // stairs from an obstacle?)

    public Cell(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
        this.obstacles = EnumSet.noneOf(Obstacle.class);
        this.dirt = true;
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