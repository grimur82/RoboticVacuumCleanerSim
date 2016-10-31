package control;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.lang.Math;

import floor.Coordinate;

public class MinPriorityQueue
{
  PriorityQueue<Coordinate> minPQ;  
  public MinPriorityQueue() {
		minPQ = new PriorityQueue<Coordinate>(36, new Comparator<Coordinate>() {

			@Override 
			public int compare(Coordinate d, Coordinate d2) {
        if(d.getDistance() < d2.getDistance()){
          return -1;
        }
        if(d.getDistance() > d2.getDistance()){
          return 1;
        }
          return 0;        
			}
		});
  }
  public void addPQ(Coordinate v){
    minPQ.add(v);
  }
  public PriorityQueue<Coordinate> getPq(){
    return minPQ;
  }
  public Coordinate getMin(){
    return minPQ.poll();
  }
  public int checkSize(){
    return minPQ.size();
  }
}