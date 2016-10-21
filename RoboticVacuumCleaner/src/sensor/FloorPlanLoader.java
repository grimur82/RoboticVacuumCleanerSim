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
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import floor.Coordinate;
import floor.Cell;
import floor.SurfaceType;
import util.Debugger;

/**
 * Preliminary sensor loading prep: retrieve and load the floor plan.
 */
public class FloorPlanLoader {

    private ArrayList<Coordinate> doors;
    private Cell[][] floorPlan;

    Coordinate startPos = new Coordinate();

    public FloorPlanLoader() throws ParserConfigurationException, SAXException, IOException {
        doors = new ArrayList<>();

        loadFloorPlan();
    }

    public Cell[][] getFloorPlan() {
        return floorPlan;
    }

    public void loadFloorPlan() throws ParserConfigurationException, SAXException, IOException {
        File file = new File("../test/PlanA.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document parsing = db.parse(file);
        parsing.getDocumentElement().normalize();

        setDim(parsing);
        setStart(parsing);
        setDoors(parsing);
        setCells(parsing);
    }

    public void setDim(Document doc) {
        NodeList start = doc.getElementsByTagName("dimensions");
        Element s = (Element) start.item(0);

        int x = Integer.parseInt(s.getAttribute("x"));
        int y = Integer.parseInt(s.getAttribute("y"));

        floorPlan = new Cell[x + 1][y + 1];
    }

    public void setStart(Document doc) {
        NodeList start = doc.getElementsByTagName("start");
        Element s = (Element) start.item(0);

        startPos.setX(Integer.parseInt(s.getAttribute("x")));
        startPos.setY(Integer.parseInt(s.getAttribute("y")));
    }

    public void setDoors(Document doc) {
        NodeList d = doc.getElementsByTagName("doors");
        Element doorList = (Element) d.item(0);
        NodeList eachDoor = doorList.getElementsByTagName("door");

        for (int i = 0; i < eachDoor.getLength(); ++i) {
            Element door = (Element) eachDoor.item(i);

            Double x = Double.valueOf(door.getAttribute("x"));
            Double y = Double.valueOf(door.getAttribute("y"));

            Coordinate coordinate = new Coordinate(x.doubleValue(), y.doubleValue());
            doors.add(coordinate);
        }
    }
    
    public ArrayList<Coordinate> getDoors(){
    	return doors;
    }

    public void setCells(Document doc) {
        NodeList cells = doc.getElementsByTagName("cells");
        Element cellList = (Element) cells.item(0);
        NodeList eachCell = cellList.getElementsByTagName("cell");

        for (int i = 0; i < eachCell.getLength(); ++i) {
            Element cell = (Element) eachCell.item(i);

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
            NodeList obs = cell.getElementsByTagName("obstacles");
            if (obs.getLength() > 0) {
                Node obList = obs.item(0);
                Element obsList = (Element) obList;
                NodeList eachOb = obsList.getElementsByTagName("obstacle");
                for (int j = 0; j < eachOb.getLength(); ++j) {
                    String obstacle = ((Element) eachOb.item(j)).getTextContent();
                    c.setObstacle(obstacle);
                }
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
}