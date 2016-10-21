package navigation;

import floor.Cell;
import floor.Coordinate;
import floor.Obstacle;
import sensor.SensorServices;
import util.Debugger;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ControlSystemService {

    private Coordinate currentPos;

    private static ControlSystemService controlSystemService;
    private HashMap<Coordinate, Cell> visited = new HashMap<>();
    private HashMap<Coordinate, Cell> unvisited = new HashMap<>();
    private SensorServices sensorService;
    private Sweeper sweeper = Sweeper.getInstance(); 
    // initialise Sweeper.class
    // dirt capacity
    // dirt space available
    /**
     * The control system.
     *
     * @param sensorService Reference to sensor/simulator.
     * @throws IOException 
     * @throws SAXException 
     * @throws ParserConfigurationException 
     */
    private ControlSystemService(SensorServices sensorService) throws ParserConfigurationException, SAXException, IOException {
        Debugger.log("Starting control system");
        this.sensorService = sensorService;
        	clean();
    }

    /**
     * Singleton.
     *
     * @param sL Reference to sensor/simulator.
     * @return instance
     * @throws IOException 
     * @throws SAXException 
     * @throws ParserConfigurationException 
     */
    public static ControlSystemService getInstance(SensorServices sL) throws ParserConfigurationException, SAXException, IOException {
        if (controlSystemService == null)
            controlSystemService = new ControlSystemService(sL);

        return controlSystemService;
    }

    /**
     * Set current position of sweeper.
     *
     * @param currentPos Current position.
     */
    public void setPosition(Coordinate currentPos) {
        this.currentPos = currentPos;
    }
    public void decreaseSweepDirtCapacity(){
    	sweeper.decreaseDirtCapacity();
    }
    public int checkSweepDirtCapacity(){
    	return sweeper.checkDirtCapacity();
    }
    
    
    public double movementCharge(Cell cellA, Cell cellB){
    	double chargeA = cellA.getSurfaceType().getPowerUsed();
    	double chargeB = cellB.getSurfaceType().getPowerUsed();
    	return (chargeA+chargeB)/2;  	
    }
    
    public void decreasePowerMove(double total){
    	sweeper.decreasePowerCapacity(total);
    }
    
    
    public void decreasePowerClean(Cell cell){
    	double currentCellCharge = cell.getSurfaceType().getPowerUsed();
    	sweeper.decreasePowerCapacity(currentCellCharge);
    }
    
    public double checkPowerCapacity(){
    	return sweeper.checkPowerCapacity();
    }
    
    
    private void clean() throws ParserConfigurationException, SAXException, IOException {
        setPosition(sensorService.getStartPosition());
       
        do {

            int x = (int) currentPos.getX();
            int y = (int) currentPos.getY();
            Cell cell = sensorService.getCell(x, y);
           
            if(Sweeper.getInstance().checkDirtCapacity() == 0 || Sweeper.getInstance().checkPowerCapacity() <= 0.0){
            	break;
            }
           
            //shuts down when dirt and power capacity is 0 for now
            Debugger.log("Cleaning cell (" + x + ", " + y + ")");
            
            Debugger.log("Surface type: "+ cell.getSurfaceType());
            cell.checkDirt();
            
            
            // Clean if dirt
            // 
            
            cell.clean();
           
            

            // Update visited and unvisited cells
            registerCells();

            // Set next cell
            RandomPosition randomDirection = new RandomPosition(x, y);
            Coordinate randomNr = randomDirection.getRandomCoordinate();
            setPosition(randomNr);
            int a = (int) currentPos.getX();
            int b = (int) currentPos.getY();
            Cell nextCell = sensorService.getCell(a,b);
            
            decreasePowerMove(movementCharge(cell, nextCell));
            Debugger.log("Power for movement from current cell to next cell: "+ movementCharge(cell, nextCell));
        	Debugger.log("New power capacity will be: "+ checkPowerCapacity());
          
        

            // Delay for visualization
            if (Debugger.getMode()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        } while (!unvisited.isEmpty());
        Debugger.log("Cleaning done!");
    }
    private boolean checkDirt(int x, int y) throws ParserConfigurationException, SAXException, IOException{
    	return sensorService.getCell(x, y).checkDirt();
    }
    public void shutDownSweeper(){
    	System.out.println("Sweeper is full: Stopped");
    	return;
    }
    /**
     * Document this and surrounding cells as visited or unvisited.
     *
     * TODO: Conflict with dirt levels. Assumes visited = clean, unvisited = dirty.
     */
    private void registerCells() {
        int x = (int) currentPos.getX();
        int y = (int) currentPos.getY();

        // Current cell
        Cell cell = sensorService.getCell(x, y);
        visited.put(currentPos, cell);
        unvisited.remove(currentPos);

        // Top cell
        Coordinate topCoordinate = new Coordinate(x, y + 1);
        if (!visited.containsKey(topCoordinate) && !sensorService.senseObstacleTop(cell))
                unvisited.put(topCoordinate, sensorService.getCell(x, y + 1));

        // Bottom cell
        Coordinate bottomCoordinate = new Coordinate(x, y - 1);
        if (!visited.containsKey(bottomCoordinate) && !sensorService.senseObstacleBottom(cell))
                unvisited.put(bottomCoordinate, sensorService.getCell(x, y - 1));

        // Left cell
        Coordinate leftCoordinate = new Coordinate(x - 1, y);
        if (!visited.containsKey(leftCoordinate) && !sensorService.senseObstacleLeft(cell))
                unvisited.put(leftCoordinate, sensorService.getCell(x - 1, y));

        // Right cell
        Coordinate rightCoordinate = new Coordinate(x + 1, y);
        if (!visited.containsKey(rightCoordinate) && !sensorService.senseObstacleRight(cell))
                unvisited.put(rightCoordinate, sensorService.getCell(x + 1, y));

    }

}