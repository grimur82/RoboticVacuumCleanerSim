package floor;

/**
 * Status of door.
 */
public enum DoorStatus {

    CLOSED(0),
	OPEN(1);

    private int status;

    DoorStatus(int doorStatus){
    	this.status = doorStatus;
    }

}
