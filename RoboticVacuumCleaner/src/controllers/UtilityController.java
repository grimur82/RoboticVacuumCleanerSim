package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import driver.Driver;
import util.Debugger;

public class UtilityController {
	private static UtilityController utilController = new UtilityController();
	public static UtilityController getServices() {
		return utilController;
	}
	public void listFileNames() throws URISyntaxException {
		String directory = new File(Driver.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent() + "/test";
		File[] listOfFiles = new File(directory).listFiles();
		System.out.println(directory);
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.print(file.getName() + " ");
		    }
		}
		System.out.println();
	}
	public String getFloorPlanPath() throws URISyntaxException {
		return new File(Driver.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent() + "/test/";
	}
	//getting file from user input
	public File askUserFloorPlan() throws IOException, URISyntaxException {
	    	Scanner sc = new Scanner(System.in);
	    	boolean fileExists = false;
	    	File file = null;
	    	System.out.print("Type in file of floor plan: ");
	    	//prompts user until file is accepted
	    	while(!fileExists){
	        	String floorPlan = sc.next().replaceAll("\\s+","");
	        	if(floorPlan.equals("Exit")){
	        		System.exit(-1);
	        	}
	        	file = new File(getFloorPlanPath() + floorPlan);    	
	        	//checks if file path is valid or if file exists
	        	if(file.exists()){
	        		String ext = floorPlan.substring(floorPlan.lastIndexOf(".")+1);
	        		//checks file format
	        		if(ext.equals("xml")){
	        			Debugger.log("Loading floor plan");
	        			fileExists = true;
	        			sc.close();
	        			break;
	        		}else{
	        			Debugger.log("Invalid file format.");
	        		}    
	        	} else{
	        		Debugger.log("File does not exist.");
	        	}  
	    	}
	    	sc.close();
	    	return file;
	    }
	public boolean checkInBounds(int x, int y,int floor, int arr) {
		return (x >= 0 && x < floor) && (y >= 0 && y < arr);
	}
}