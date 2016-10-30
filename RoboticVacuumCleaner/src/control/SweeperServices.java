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
import util.Debugger;

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
		//ControlSystemService.getInstance().getCurrentPos().setDistance(0);
		q.add(ControlSystemService.getInstance().getCurrentPos());
		while(!q.isEmpty()){
			Coordinate temp = q.remove(0);
			System.out.println("X: " + temp.getX() + " Y: " + temp.getY());
			if(temp.getX() == SensorServices.getInstance().getStartPosition().getX()
					&& temp.getY() == SensorServices.getInstance().getStartPosition().getY()){
				Debugger.log("Found the base: Recharging");
				Sweeper.getInstance().reCharged();
				break;
			}
			for(Coordinate s : temp.getParents()){
				
				if(s.getDistance() == -1){
					s.setDistance(temp.getDistance() + 1);
					s.setNeighbor(temp);
					//s.setParentsFromOther(temp.getParents());
					q.add(s);
				}
			}
		}
		
		
		
		//ControlSystemService.getInstance().currentPosition();
	}
	
}
