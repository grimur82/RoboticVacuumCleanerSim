package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import floor.Cell;
import floor.Coordinate;
import sensor.FloorPlan;
import sensor.SensorServices;

public class SweeperServices {
	private static SweeperServices sweeperServices = new SweeperServices();
	private Sweeper sweeper = Sweeper.getInstance();
	private PriorityQueue<Coordinate> pq;
	private SweeperServices(){
	}
	public static SweeperServices getInstance(){
		return sweeperServices;
	}
	public void backToBase() throws ParserConfigurationException, SAXException, IOException{
		// path from current to startPosition
		
		Cell[][] floor = SensorServices.getInstance().getFloorPlan();
		
		ArrayList<Coordinate> q = new ArrayList<Coordinate>();
		SensorServices.getInstance().getStartPosition().setDistance(0);
		q.add(SensorServices.getInstance().getStartPosition());
		while(!q.isEmpty()){
			Coordinate temp = q.remove(0);
			for(Coordinate s : temp.getParents()){
				System.out.println("X: " + s.getX() + " Y: " + s.getY());
				if(s.getDistance() == -1){
					s.setDistance(s.getDistance() + 1);
					s.setParents(temp.getParents());
					q.add(s);
				}
			}
		}
		
		
		
		//ControlSystemService.getInstance().currentPosition();
	}
	
}
