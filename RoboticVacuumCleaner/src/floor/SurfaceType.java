package floor;

/**
 * Maps surface type to the amount of power used.
 */
public enum SurfaceType {
    
    NONE(0),
    BARE(1),
    LOWPILE(2),
    HIGHPILE(3);
    private int type;
    private int powerUsed;
    SurfaceType(int type){
    	this.type = type;
    }
    public void Type(int powerUsed){
        this.powerUsed = powerUsed;
    }
    public int getSurfaceType() {
        return powerUsed;
    }

}
