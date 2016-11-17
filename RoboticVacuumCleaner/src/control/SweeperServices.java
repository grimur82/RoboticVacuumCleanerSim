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
		sweeper.reCharged();
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
			//setChargePosition(getChargingPath);
			setChargePosition(new Coordinate(getChargingPath.getX(), getChargingPath.getY()));
			ArrayList<Coordinate> shortestPath = new ArrayList<Coordinate>();
			// Go through path, the sweeper has found.
			while(toPath != getChargingPath){
				shortestPath.add(getChargingPath);
				getChargingPath = getChargingPath.getNeighbor();
			}
			// Let user know, the sweeper has reached its charging base.
			if(toPath == getChargingPath){
				shortestPath.add(getChargingPath);
				Collections.reverse(shortestPath);
				Debugger.log("Moving towards charging base:"); 
				for(Coordinate getPath : shortestPath){
					Debugger.log("x: " + getPath.getX() + " Y: " + getPath.getY());		
				}
				Debugger.log("Found Base");
			}
		}
	}
	public double powerNeededtoRechargeBase(){
		double power = 0;
		// path from current to startPosition
		MinPriorityQueue pqUnvisited = new MinPriorityQueue();
		MinPriorityQueue pqVisited = new MinPriorityQueue();
		MinPriorityQueue findClosestChargingBase = new MinPriorityQueue();
		Coordinate c = ControlSystemService.getInstance().getCurrentPos();
		
		c.setPowerDistance(0);
		c.setNeighbor(c);
		pqUnvisited.addPQ(c);
		// Calculate a path for sweeper to reach a charging base.
		while(pqUnvisited.checkSize() != 0){
			Coordinate temp = pqUnvisited.getMin();
			pqVisited.addPQ(temp);
			for(Coordinate parent : ControlSystemService.getInstance().getNeighbors(temp)){
				if(!pqVisited.getPq().contains(parent)){
					
						parent.setPowerDistance(Double.POSITIVE_INFINITY);
						parent.setNeighbor(temp);
						pqUnvisited.addPQ(parent);
					}
					
				}
			}
		
		power = 0;
		
		
		// path from current to startPosition
		pqUnvisited = new MinPriorityQueue();
		pqVisited = new MinPriorityQueue();
		findClosestChargingBase = new MinPriorityQueue();
		c = ControlSystemService.getInstance().getCurrentPos();
		
		c.setPowerDistance(0);
		c.setNeighbor(c);
		pqUnvisited.addPQ(c);
		// Calculate a path for sweeper to reach a charging base.
		while(pqUnvisited.checkSize() != 0){
			Coordinate temp = pqUnvisited.getMin();
			pqVisited.addPQ(temp);
			for(Coordinate parent : ControlSystemService.getInstance().getNeighbors(temp)){
				if(!pqVisited.getPq().contains(parent)){
					
					power = ControlSystemService.getInstance().calcCharge(SensorServices.getInstance()
							.getCell(c), SensorServices.getInstance()
									.getCell(parent));
					if(parent.getPowerDistance() > power){
						parent.setPowerDistance(power);
						parent.setNeighbor(temp);
						pqUnvisited.addPQ(parent);
					}
					
				}
			}
		}
		for(Coordinate cd : pqVisited.getPq()){
			if(SensorServices.getInstance().getCell(cd).typeChargingBase()){
				findClosestChargingBase.addPQ(cd);	
			}		
		}
		Coordinate getChargingPath = findClosestChargingBase.getMin();
		if(getChargingPath != null){
			power = 0;
			
			// Go through path, the sweeper has found.
			while(c != getChargingPath){
				power += getChargingPath.getPowerDistance();
				getChargingPath = getChargingPath.getNeighbor();
			}
			// Let user know, the sweeper has reached its charging base.
			if(c == getChargingPath){
				return power;
			}
		}
		return 0;
		
	}
	public void setChargePosition(Coordinate c){
		chargePosition = c;
	}
	public Coordinate getChargePosition(){
		return chargePosition;
	}
	}
