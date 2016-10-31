package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

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
		
		//ArrayList<Coordinate> q = new ArrayList<Coordinate>();
		LinkedList<Coordinate> q = new LinkedList<Coordinate>();
		ControlSystemService.getInstance().getCurrentPos().setDistance(0);
		q.add(ControlSystemService.getInstance().getCurrentPos());
		double startX = 0.0;
		double startY = 0.0;
		while(!q.isEmpty()){
			Coordinate temp = q.remove();
			if(temp.getParents().isEmpty()){
				temp.setParents();
			}
			
			System.out.println("X: " + temp.getX() + " Y: " + temp.getY());
			if(temp.getX() == startX
					&& temp.getY() == startY){
				System.out.println("Start Position " + " X: " + SensorServices.getInstance().getStartPosition().getX()
				+ " Y: " + SensorServices.getInstance().getStartPosition().getY());
				Debugger.log("Found the base: Recharging");
				Sweeper.getInstance().reCharged();
				break;
			}
			for(Coordinate s : temp.getParents()){
				
				if(s.getDistance() == -1){
					s.setDistance(temp.getDistance() + 1);
					s.setNeighbor(temp);
					q.add(s);
				}
			}
		}
		
		
		
		//ControlSystemService.getInstance().currentPosition();
	}
	
}
