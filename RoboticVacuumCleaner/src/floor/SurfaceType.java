package floor;

/**
 * Maps surface type to the amount of power used.
 */
public enum SurfaceType {
    
    NONE(0),
    BARE(1),
    LOWPILE(2),
    HIGHPILE(3);
    
    private int powerUsed;
    
    SurfaceType(int powerUsed){
    	this.powerUsed = powerUsed;
    }
    
    public int getPowerUsed() {
        return powerUsed;
    }

}
