package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	private Coordinate chargePosition;
	private SweeperServices(){
	}
	public static SweeperServices getInstance(){
		return sweeperServices;
	}
	// Recharge power and dirt capacity on sweeper.
	public void reCharge(){
		sweeper.getInstance().reCharged();
	}
	// Run a path for sweeper to find back to a base.
	public void backToBase() throws ParserConfigurationException, SAXException, IOException{
		// path from current to startPosition
		MinPriorityQueue pqUnvisited = new MinPriorityQueue();
		MinPriorityQueue pqVisited = new MinPriorityQueue();
		MinPriorityQueue findClosestChargingBase = new MinPriorityQueue();
		Coordinate c = ControlSystemService.getInstance().getCurrentPos();
		c.setDistance(0);
		c.setNeighbor(c);
		pqUnvisited.addPQ(c);
		int sourceDistance = 1;
		// Calculate a path for sweeper to reach a charging base.
		while(pqUnvisited.checkSize() != 0){
			Coordinate temp = pqUnvisited.getMin();
			pqVisited.addPQ(temp);
			for(Coordinate parent : ControlSystemService.getInstance().getNeighbors(temp)){
				if(!pqVisited.getPq().contains(parent)){
					parent.setDistance(sourceDistance);
					parent.setNeighbor(temp);
					pqUnvisited.addPQ(parent);
					sourceDistance++;
				}
			}
		}
		Coordinate toPath = null;
		
		for(Coordinate cd : pqVisited.getPq()){
			if(cd == ControlSystemService.getInstance().getCurrentPos()){
				toPath = cd;
			}
			if(SensorServices.getInstance().getCell(cd).typeChargingBase()){
				findClosestChargingBase.addPQ(cd);
				
			}		
		}
		if(findClosestChargingBase.checkSize() <=0){
			System.out.println("There is no recharge base discoved. Shutting down.....");
			ControlSystemService.getInstance().shutDownSweeper();
		}
		else{
			Coordinate getChargingPath = findClosestChargingBase.getMin();
			
			ArrayList<Coordinate> shortestPath = new ArrayList<Coordinate>();
			// Go through path, the sweeper has found.
			while(toPath != getChargingPath){
				shortestPath.add(getChargingPath);
				getChargingPath = getChargingPath.getNeighbor();
			}
			// Let user know, the sweeper has reached its charging base.
			if(toPath == getChargingPath){
				SweeperServices.getInstance().setChargePosition(toPath);
				shortestPath.add(getChargingPath);
				Collections.reverse(shortestPath);
				System.out.println("Moving towards charging base:");
				for(Coordinate getPath : shortestPath){
					System.out.println("x: " + getPath.getX() + " Y: " + getPath.getY());
				}
				System.out.println("Found Base");
			}
		}
	}
	public void setChargePosition(Coordinate c){
		chargePosition = c;
	}
	public Coordinate getChargePosition(){
		return chargePosition;
		
	}
	}
