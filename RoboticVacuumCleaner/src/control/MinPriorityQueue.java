package control;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.lang.Math;

import floor.Coordinate;

public class MinPriorityQueue
{
  PriorityQueue<Coordinate> minPQ;  
  // Lowest Distance is on top of queue.
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
  // Add to Queue
  public void addPQ(Coordinate v){
    minPQ.add(v);
  }
  // Get the queue
  public PriorityQueue<Coordinate> getPq(){
    return minPQ;
  }
  // Look at what Coordinate is on top of Queue
  public Coordinate getMin(){
    return minPQ.poll();
  }
  // Return size of queue.
  public int checkSize(){
    return minPQ.size();
  }
}