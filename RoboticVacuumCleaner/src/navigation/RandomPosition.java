package navigation;

import java.util.ArrayList;
import java.util.Random;

import floor.Coordinate;

public class RandomPosition {
	
	private Coordinate left;
	private Coordinate right;
	private Coordinate top;
	private Coordinate bottom;
	private ArrayList<Coordinate> direction;
	private int x;
	private int y;
	public RandomPosition(int x, int y){
		direction = new ArrayList<Coordinate>();
		this.x = x;
		this.y = y;
		this.left = new Coordinate(x-1, y);
		this.right = new Coordinate(x+1, y);
		this.top = new Coordinate(x, y + 1);
		this.bottom = new Coordinate(x, y - 1);
		direction.add(left);
		direction.add(right);
		direction.add(top);
		direction.add(bottom);
	}
	public Coordinate getRandomCoordinate(){
		Random r = new Random();
		int getNumber = r.nextInt(4);
		return direction.get(getNumber);
	}
}
