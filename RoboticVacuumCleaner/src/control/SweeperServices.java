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
		Coordinate c = ControlSystemService.getInstance().getCurrentPos();
		c.setDistance(0);
		c.setNeighbor(c);
		pqUnvisited.addPQ(c);
		// Calculate a path for sweeper to reach a charging base.
		while(pqUnvisited.checkSize() != 0){
			Coordinate temp = pqUnvisited.getMin();
			pqVisited.addPQ(temp);
			for(Coordinate parent : ControlSystemService.getInstance().getNeighbors(temp)){
				if(!pqVisited.getPq().contains(parent)){
					parent.setDistance(0);
					parent.setNeighbor(temp);
					pqUnvisited.addPQ(parent);
				}
			}
		}
		Coordinate toPath = null;
		Coordinate fromPath = null;
		for(Coordinate cd : pqVisited.getPq()){
			if(cd == ControlSystemService.getInstance().getCurrentPos()){
				toPath = cd;
			}
			if(SensorServices.getInstance().getCell(cd).typeChargingBase()){
				fromPath = cd; 
			}		
		}
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
