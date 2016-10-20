package sensor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.EnumSet;
import java.util.Scanner;

import floor.Coordinate;
import floor.Cell;
import floor.SurfaceType;
import floor.Obstacle;

/**
 * Preliminary sensor loading prep: retrieve and load the floor plan.
 */
public class SensorLoader {

    private ArrayList<Coordinate> doors;
    private Cell[][] floorPlan;

    private Coordinate startPos = new Coordinate();

    public SensorLoader() throws ParserConfigurationException, SAXException, IOException {
        doors = new ArrayList<Coordinate>();
        floorPlan = new Cell[1000][1000];
	}

    public void loadFloorPlan() throws ParserConfigurationException, SAXException, IOException {
        File file = new File("../test/PlanA.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document parsing = db.parse(file);
        parsing.getDocumentElement().normalize();

        setStart(parsing);
        setDoors(parsing);
        setCells(parsing);
    }

    public void setStart(Document doc) {
        NodeList dimensions = doc.getElementsByTagName("start");
        Element dimension = (Element) dimensions.item(0);

        startPos.setX(Integer.parseInt(dimension.getAttribute("x")));
        startPos.setY(Integer.parseInt(dimension.getAttribute("y")));
    }

    public void setDoors(Document doc) {
        NodeList d = doc.getElementsByTagName("door");

        for (int i = 0; i < d.getLength(); ++i) {
            Element door = (Element) d.item(i);
            Double x = Double.valueOf(door.getAttribute("x"));
            Double y = Double.valueOf(door.getAttribute("y"));
            Coordinate coordinate = new Coordinate(x.doubleValue(), y.doubleValue());
            doors.add(coordinate);
        }
    }

    public void setCells(Document doc) {
        NodeList cells = doc.getElementsByTagName("cell");

        for (int i = 0; i < cells.getLength(); ++i) {
            Element cell = (Element) cells.item(i);

            // Extract data
            int x = Integer.parseInt(cell.getElementsByTagName("x").item(0).getTextContent());
            int y = Integer.parseInt(cell.getElementsByTagName("y").item(0).getTextContent());
            String surface = cell.getElementsByTagName("surface").item(0).getTextContent();
            String name = cell.getAttribute("name");

            // Create cell
            Cell c = new Cell(SurfaceType.valueOf(surface));
        
            // Set name
            c.setName(name);

            // Set obstacles
            EnumSet<Obstacle> obstacles = EnumSet.noneOf(Obstacle.class);
            NodeList obs = cell.getElementsByTagName("obstacles");
            for (int j = 0; j < obs.getLength(); ++j) {
            	Element o = (Element) obs.item(j);
            	String obstacle = o.getElementsByTagName("obstacle").item(j).getTextContent();
            	c.setObstacle(obstacle);
            }
            // Record cell 
            floorPlan[x][y] = c;
        }
    }




	public Cell getCell(int x, int y){
		return floorPlan[x][y];
	}
	public Coordinate getStartPosition() {
        return startPos;
	}
	public Coordinate getLeftPosition(Coordinate s){
		if(s.getX() - 1.0 < 0.0){
			return startPos;
		}
		else{
			startPos.setX(s.getX() - 1.0);
			startPos.setY(s.getY());
			return startPos;
		}
		
	}
	public Coordinate getRightPosition(Coordinate s){
		// Hardcoded for now.
		if(s.getX() + 1.0 > 9.0){
			return startPos;
		}
		else{
			startPos.setX(s.getX() + 1.0);
			startPos.setY(s.getY());
			return startPos;
		}
	}
	public Coordinate getDownPosition(Coordinate s){
		if(s.getY() - 1.0 < 0){
			return startPos;
		}
		else{
			startPos.setX(s.getX());
			startPos.setY(s.getY()-1);
			return startPos;
		}
		
	}
	public Coordinate getUpPosition(Coordinate s){
		// Hardcoded for now.
		if(s.getY() + 1.0 > 9.0){
			return startPos;
		}
		else{
			startPos.setX(s.getX());
			startPos.setY(s.getY()+ 1.0);
			return startPos;
		}
	}
}