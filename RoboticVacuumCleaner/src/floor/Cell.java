package floor;

import java.util.EnumSet;

/**
 * A cell object.
 */
public class Cell {

    private String name;
    private SurfaceType surfaceType;
    private EnumSet<Obstacle> obstacles;

    // TODO: flag if charging station
    // TODO: flag if stairs?*
    // (doc says to treat it as an obstacle. do we even need to differentiate
    // stairs from an obstacle?)

    public Cell(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
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

    /**
     * Sets an obstacle relative to this cell.
     *
     * TODO: When used by the navigator, make sure to set the obstacle from the other end/adjacent cell!
     *
     * @param obstacle
     */
    public void setObstacle(String obstacle) {
        obstacles.add(Obstacle.valueOf(obstacle));
    }

    public boolean isOpen(String dir) {
        return obstacles.contains(Obstacle.valueOf(dir));
    }

}