package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

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
	public void reCharge(){
		sweeper.getInstance().reCharged();
	}
	public void backToBase() throws ParserConfigurationException, SAXException, IOException{
		// path from current to startPosition
		MinPriorityQueue pqUnvisited = new MinPriorityQueue();
		MinPriorityQueue pqVisited = new MinPriorityQueue();
		ArrayList<Coordinate> test = new ArrayList<Coordinate>();
		// Get floorplan coordinates.
		for(int i =0; i < SensorServices.getInstance().getFloorPlan().length-1; i++){
			for(int j =0; j < SensorServices.getInstance().getFloorPlan().length-1; j++){
				if(SensorServices.getInstance().getCell(i, j).getParents() == null){
					SensorServices.getInstance().getCell(i, j).setParents();
				}
				SensorServices.getInstance().getCell(i, j).getCoordinate().setDistance(-1);
				if(SensorServices.getInstance().getCell(i,j).getCoordinate().getNeighbor() == null){
					SensorServices.getInstance().getCell(i,j).getCoordinate().setNeighbor(null);
				}
				
			
		}
		// Set the neighbors which sweeper is going to pass through.
		SensorServices.getInstance().getCell((int)ControlSystemService.getInstance().getCurrentPos().getX(), 
		(int)ControlSystemService.getInstance().getCurrentPos().getY()).setParents();
		Coordinate c = SensorServices.getInstance().getCell((int)ControlSystemService.getInstance().getCurrentPos().getX(), 
				(int)ControlSystemService.getInstance().getCurrentPos().getY()).getCoordinate();
		c.setDistance(0);
		pqUnvisited.addPQ(c);
		// Calculate a path for sweeper to reach a charging base.
		while(pqUnvisited.checkSize() != 0){
			Coordinate temp = pqUnvisited.getMin();
			pqVisited.addPQ(temp);
			if(SensorServices.getInstance().getCell((int)temp.getX(), 
						(int)temp.getY()).getParents() == null){
				SensorServices.getInstance().getCell((int)temp.getX(), 
						(int)temp.getY()).setParents();
			}
			for(Coordinate parent : 
				SensorServices.getInstance().getCell((int)temp.getX(), 
						(int)temp.getY()).getParents()){
				if(!pqVisited.getPq().contains(parent)){
					parent.setDistance(0);
					parent.setNeighbor(temp);
					pqUnvisited.addPQ(parent);
				}
			}
		}
		Coordinate toPath = SensorServices.getInstance().getCell(0, 0).getCoordinate();
		Coordinate fromPath = SensorServices.getInstance().getCell((int)ControlSystemService.getInstance().getCurrentPos().getX(), 
				(int)ControlSystemService.getInstance().getCurrentPos().getY()).getCoordinate();
		// Go through path, the sweeper has found.
		while(toPath != fromPath){
			System.out.println("x: " + toPath.getX() + " Y: " + toPath.getY());
			toPath = toPath.getNeighbor();
		}
		// Let user know, the sweeper has reached its charging base.
		if(toPath == fromPath){
			System.out.println("Found Base");
		}
	}
	}
}