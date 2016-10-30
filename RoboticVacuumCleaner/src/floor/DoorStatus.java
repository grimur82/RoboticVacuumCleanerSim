package floor;

/**
 * Status of door.
 */
public enum DoorStatus {

    CLOSED(0),
	OPEN(1);

    private int doorStatus;

    DoorStatus(int doorStatus){
    	this.doorStatus = doorStatus;
    }
    
    public int getDoorStatus() {
        return doorStatus;
    }

}
