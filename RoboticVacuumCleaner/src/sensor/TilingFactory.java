package sensor;

public class TilingFactory {
	public static Tiling getTiling(String type, int fX, int fY, int toX, int toY){
		if(type.equals("closet")){
			return new TilingClosetImpl(fX,fY,toX,toY);
		}
		if(type.equals("bedroom")){
			return new TilingBedRoomImpl(fX,fY,toX,toY);
		}
		if(type.equals("hallway")){
			return new TilingHallwayImpl(fX,fY,toX,toY);
		}
		if(type.equals("bathroom")){
			return new TilingBathroomImpl(fX,fY,toX,toY);
		}
		return null;
	}
}
