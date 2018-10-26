package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimulationModel;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimEngineFactory;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationConfig;
import de.uka.ipd.sdq.simulation.preferences.SimulationPreferencesHelper;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human.HumanBehaviour;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.LoadPassengersEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents.HumanWalksDirectlyToWorkEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents.WalkToBusStopAtHomeEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.processes.BusProcess;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.processes.HumanProcess;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.CSVHandler;




public class HumanModel extends AbstractSimulationModel{

	 
	 private BusStop stop1;
	 private BusStop stop2;
	 private BusStop stop3;
	 
	 private double startTime; 
	 
	 public int modelRun;
	 public LinkedList<Double> durations;
	 
	 private LinkedList<Human> humans;

	 
	 
	public HumanModel(ISimulationConfig config, ISimEngineFactory factory) {
		super(config, factory);
		humans = new LinkedList<Human>();
	}
	
	public void init() {
		
		startTime = System.nanoTime();

		
        // define bus stops
        stop1 = new BusStop(this, "Stop1");
        stop2 = new BusStop(this, "Stop2");
        stop3 = new BusStop(this, "Stop3");

        BusStop[] stops = {stop1, stop2, stop3};
        
        // define route
        Route lineOne = new Route();
        lineOne.addSegment(stop1, stop2, 10, 35);
        lineOne.addSegment(stop2, stop3, 20, 50);
        lineOne.addSegment(stop3, stop1, 30, 50);

        // define buses
        Bus bus = new Bus(40, stop1, lineOne, this, "Bus 1");
   
        if (HumanSimValues.PROCESS_ORIENTED) {
            // schedule a process for each bus
            new BusProcess(bus).scheduleAt(2.0);
        	
        	// schedule a process for each human
        	for(int i = 0; i < HumanSimValues.NUM_HUMANS; i++){
        		//new HumanProcess(new Human(stop1, stop3, this, "Bob" + i), bus).scheduleAt(0);
        		int homeBS = 0;
        		int workBS = 0;
        		if(HumanSimValues.RANDOMIZED_HUMAN_STATIONS){
            		while(homeBS == workBS){
            			homeBS = new Random().nextInt(HumanSimValues.NUM_BUSSTOPS);
            			workBS = new Random().nextInt(HumanSimValues.NUM_BUSSTOPS);
            		}
            		} else {
            			homeBS = i % 2;
            			workBS = (i % 2) + 1;
            		}
        		Human hu = new Human(stops[homeBS], stops[workBS], this, "Bob" + i);
        		humans.add(hu);
        		new HumanProcess(hu, bus).scheduleAt(2.0);
        	}
            
        } else { // event-oriented
            // schedule intitial event for the bus
            new LoadPassengersEvent(this, "Load Passengers").schedule(bus, 2.0);
            
            
         // schedule a process for each human
        	for(int i = 0; i < HumanSimValues.NUM_HUMANS; i++){
        		//new HumanProcess(new Human(stop1, stop3, this, "Bob" + i), bus).scheduleAt(0);
        		int homeBS = 0;
        		int workBS = 0;
        		
        		if(HumanSimValues.RANDOMIZED_HUMAN_STATIONS){
            		while(homeBS == workBS){
            			homeBS = new Random().nextInt(HumanSimValues.NUM_BUSSTOPS);
            			workBS = new Random().nextInt(HumanSimValues.NUM_BUSSTOPS);
            		}
            		} else {
            			homeBS = i % 2;
            			workBS = (i % 2) + 1;
            		}
        		Human hu = new Human(stops[homeBS], stops[workBS], this, "Bob" + i);
        		humans.add(hu);
        		
        		if(hu.willWalk()){
        			new HumanWalksDirectlyToWorkEvent(this, hu.getName() + "walks directly").schedule(hu,2.0);
        		} else {
        			new WalkToBusStopAtHomeEvent(this, hu.getName() + "walks to bus station").schedule(hu ,2.0);
        		}
        		
        		
        	}
            
            
            //new PassengerArrivalEvent(Duration.seconds(2.0), this, "BS").schedule(stop1, 0);
        }
	}
	public void finalise() {
		
	 	Double finalTime = (System.nanoTime() - startTime) / Math.pow(10, 9);
		String file_header = "";
		String csvAway = "";
		String csvWaitingAtStation = "";
		String csvDrivingTimes = "";
		String behaviourMarker = "";
		String csvFreeTimes = "";
		
		int getMaxNumValues = 0;
	 	
	 	for (Human human : humans) {
	 		
	 		file_header += human.getName() + CSVHandler.CSV_DELIMITER;
	 		behaviourMarker += human.getBehaviour().toString() + CSVHandler.CSV_DELIMITER;
//	 		System.out.println("Human " + human.getName() + " is in State " + human.getState() + " and is "+ human.getBehaviour().toString());
	 		if(getMaxNumValues < human.getAwayFromHomeTimes().size()){
	 			getMaxNumValues = human.getAwayFromHomeTimes().size();
	 		}
		}
	 	
	 	file_header += CSVHandler.NEWLINE;
	 	behaviourMarker += CSVHandler.NEWLINE;
	 	
	 	for(int i = 0; i < getMaxNumValues; i++){
	 		
	 		for(int j = 0; j < humans.size(); j++){
	 			ArrayList<Duration> away = humans.get(j).getAwayFromHomeTimes();
			 	ArrayList<Duration> driven = humans.get(j).getDrivingTimes();
			 	ArrayList<Duration> waited = humans.get(j).getBusWaitingTimes();
			 	ArrayList<Duration> free = humans.get(j).getFreeTimes();
			 	
			 	
			 	if(away.size() >= i){
			 		csvAway += away.get(i).toSeconds().value();
			 		csvDrivingTimes += driven.get(i).toSeconds().value();
			 		csvWaitingAtStation += waited.get(i).toSeconds().value();
			 		csvFreeTimes += free.get(i).toSeconds().value();
			 	} else {
			 		csvAway += "-1";
			 		csvDrivingTimes += "-1";
			 		csvWaitingAtStation += "-1";
			 		csvFreeTimes += "-1";
			 	}
			 	
			 	if(j < humans.size()-1){
			 		csvAway += CSVHandler.CSV_DELIMITER;
			 		csvDrivingTimes += CSVHandler.CSV_DELIMITER;
			 		csvWaitingAtStation += CSVHandler.CSV_DELIMITER;
			 		csvFreeTimes += CSVHandler.CSV_DELIMITER;
			 	}
			 	
	 		}
	 		
	 		csvAway += CSVHandler.NEWLINE;
	 		csvDrivingTimes += CSVHandler.NEWLINE;
	 		csvWaitingAtStation += CSVHandler.NEWLINE;
	 		csvFreeTimes += CSVHandler.NEWLINE;
	 	}
	 	
		
		 
//		 for (Human human : this.humans) {
//			 	
//			 	//Duration[] away = (Duration[]) human.getAwayFromHomeTimes().toArray();
//			 	
//			 	ArrayList<Duration> away = human.getAwayFromHomeTimes();
//			 	ArrayList<Duration> driven = human.getDrivingTimes();
//			 	ArrayList<Duration> waited = human.getBusWaitingTimes();
//			 	
//			 	
//			 	for (int i = 0; i < away.size(); i++){
//			 		
//			 		
//			 		System.out.println("Day " + i + ": Away From Home (Hours)" + Math.round(away.get(i).toHours().value()*100.00) /100.00 
//			 				+ "; Driven (Minutes): " + Math.round(driven.get(i).toMinutes().value() * 100.00)/100.00 
//			 				+ "; Waiting at Stations (Minutes): " + waited.get(i).toMinutes().value());
//				}
//			 	
//			 	
//		        System.out.println("-----------------------------");
//		}
	       	System.out.println("Waiting passengers at " + stop1.getName() + ":" + stop1.getPassengersInQueue());
	       	System.out.println("Waiting passengers at " + stop2.getName() + ":" + stop2.getPassengersInQueue());	
	       	System.out.println("Waiting passengers at " + stop3.getName() + ":" + stop3.getPassengersInQueue());
	       	
	       	
	       	String[] csvs = {csvAway, csvDrivingTimes, csvWaitingAtStation, csvFreeTimes};
	       	
	       	for(int i = 0; i < csvs.length; i++){
	       		String s = "";
	       		
	       		switch (i) {
				case 0:
					s = "AwayTimes";
					break;
				case 1:
					s = "DrivingTimes";
					break;
				case 2: 
					s = "BusStationWaitingTimes";
					break;
				case 3:
					s = "FreeTimes";
					break;

				default:
					throw new IllegalStateException("More than three files");
				}	
	       		
	       		csvs[i] = csvs[i].replace('.', ',');
	       		
	       		
	       		CSVHandler.writeCSVFile(s, file_header + csvs[i]);
	       		
	        }      	
	       	CSVHandler.writeCSVFile("HumanBehaviour", behaviourMarker);
	       	
	       	Double d =  Math.round(finalTime*100.00)/100.00;
	       	String s = d.toString();
	       	
	       	s = s.replace('.', ',');
	       	
	       	CSVHandler.readCSVAndAppend("ExecutionTimes", s + CSVHandler.CSV_DELIMITER);
	       	
	       	
	}

    /**
     * Creates the simulation model for the specified configuration.
     * 
     * @param config
     *            the simulation configuration
     * @return the created simulation model
     */
    public static HumanModel create(final HumanSimConfig config) {
        // load factory for the preferred simulation engine
        ISimEngineFactory factory = SimulationPreferencesHelper.getPreferredSimulationEngine();
        if (factory == null) {
            throw new RuntimeException("There is no simulation engine available. Install at least one engine.");
        }

        // create and return simulation model
        final HumanModel model = new HumanModel(config, factory);

        return model;
    }
}
