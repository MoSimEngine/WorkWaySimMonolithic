package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim;

import java.util.LinkedList;
import java.util.Random;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimulationModel;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimEngineFactory;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationConfig;
import de.uka.ipd.sdq.simulation.preferences.SimulationPreferencesHelper;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.LoadPassengersEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.PassengerArrivalEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents.HumanWalksDirectlyToWorkEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents.WalkToBusStopAtHomeEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.processes.BusProcess;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.processes.HumanProcess;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.processes.PassengerArrivalProcess;

public class HumanModel extends AbstractSimulationModel{

	 private boolean PROCESS_ORIENTED = false;
	 
	 private BusStop stop1;
	 private BusStop stop2;
	 private BusStop stop3;
	 
	 private LinkedList<Human> humans;
	 
	 private final int numHumans = 20;
	 
	public HumanModel(ISimulationConfig config, ISimEngineFactory factory) {
		super(config, factory);
		humans = new LinkedList<Human>();
	}
	
	public void init() {
		
		
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
        
        
        System.out.println("Lets go!");
        if (PROCESS_ORIENTED) {
            // schedule a process for each bus
            new BusProcess(bus).scheduleAt(0);
        	
        	// schedule a process for each human
        	for(int i = 0; i < numHumans; i++){
        		//new HumanProcess(new Human(stop1, stop3, this, "Bob" + i), bus).scheduleAt(0);
        		int homeBS = 0;
        		int workBS = 0;
        		
        		while(homeBS == workBS){
        			homeBS = new Random().nextInt(3);
        			workBS = new Random().nextInt(3);
        		}
        		Human hu = new Human(stops[homeBS], stops[workBS], this, "Bob" + i);
        		humans.add(hu);
        		new HumanProcess(hu, bus).scheduleAt(0);
        	}
            
        } else { // event-oriented
            // schedule intitial event for the bus
        	System.out.println("Starting to drive");
            new LoadPassengersEvent(this, "Load Passengers").schedule(bus, 0);
            
         // schedule a process for each human
        	for(int i = 0; i < numHumans; i++){
        		//new HumanProcess(new Human(stop1, stop3, this, "Bob" + i), bus).scheduleAt(0);
        		int homeBS = 0;
        		int workBS = 0;
        		
        		while(homeBS == workBS){
        			homeBS = new Random().nextInt(3);
        			workBS = new Random().nextInt(3);
        		}
        		Human hu = new Human(stops[homeBS], stops[workBS], this, "Bob" + i);
        		humans.add(hu);
        		
        		if(hu.getWillWalk()){
        			new HumanWalksDirectlyToWorkEvent(this, hu.getName() + "walks directly").schedule(hu,0);
        		} else {
        			new WalkToBusStopAtHomeEvent(this, hu.getName() + "walks to bus station").schedule(hu ,0);
        		}
        		
        		
        	}
            
            
            //new PassengerArrivalEvent(Duration.seconds(2.0), this, "BS").schedule(stop1, 0);
        }
	}
	public void finalise() {
		// TODO Auto-generated method stub
		 System.out.println("-----------------------------");
		 
		 for (Human human : this.humans) {
			 	System.out.println("Human " + human.getName() + " is in State " + human.getState() + " and is waling directly : " + human.getWillWalk()); 
		        System.out.println("-----------------------------");
		}
	       	System.out.println("Waiting passengers at " + stop1.getName() + ":" + stop1.getPassengersInQueue());
	       	System.out.println("Waiting passengers at " + stop2.getName() + ":" + stop2.getPassengersInQueue());	
	       	System.out.println("Waiting passengers at " + stop3.getName() + ":" + stop3.getPassengersInQueue());
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
