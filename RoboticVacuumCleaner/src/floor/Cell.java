package floor;

import util.Debugger;

import java.util.EnumSet;

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
    public boolean checkDirt(){
    	if(dirt){
    		dirt = false;
    		Debugger.log("Floor Dirty: CleanSweeper cleans.");
    	}
    	else{
    		Debugger.log("Floor Clean: CleanSweeper moves on.");
    	}
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

    public boolean isOpen(Obstacle... obs) {
        for (Obstacle o : obs) {
            if (obstacles.contains(o))
                return false;
        }

        return true;
    }

}