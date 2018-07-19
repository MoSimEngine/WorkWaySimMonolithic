package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimulationModel;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimEngineFactory;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationConfig;
import de.uka.ipd.sdq.simulation.preferences.SimulationPreferencesHelper;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.LoadPassengersEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.PassengerArrivalEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.processes.HumanProcess;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.processes.PassengerArrivalProcess;

public class HumanModel extends AbstractSimulationModel{

	 private boolean PROCESS_ORIENTED = true;
	 
	 private BusStop stop1;
	 private BusStop stop2;
	 private BusStop stop3;
	 
	 private Human human; 
	 
	public HumanModel(ISimulationConfig config, ISimEngineFactory factory) {
		super(config, factory);
		
	}

	public void init() {
		stop1 = new BusStop(this, "Stop1");
        stop2 = new BusStop(this, "Stop2");
        stop3 = new BusStop(this, "Stop3");
		
        // define bus stops
        stop1 = new BusStop(this, "Stop1");
        stop2 = new BusStop(this, "Stop2");
        stop3 = new BusStop(this, "Stop3");

        // define route
        Route lineOne = new Route();
        lineOne.addSegment(stop1, stop2, 10, 35);
        lineOne.addSegment(stop2, stop3, 20, 50);
        lineOne.addSegment(stop3, stop1, 30, 50);

        // define buses
        //Bus bus = new Bus(40, stop1, lineOne, this, "Bus 1");
        Human human = new Human(stop1, stop3, this, "Bob");
        
        this.human = human;
        if (PROCESS_ORIENTED) {
            // schedule a process for each bus
            //new BusProcess(bus).scheduleAt(0);
        	
        	// schedule a process for each human
        	new HumanProcess(human).scheduleAt(0);

            
        } else { // event-oriented
            // schedule intitial event for the bus
            //new LoadPassengersEvent(this, "Load Passengers").schedule(bus, 0);
        }
	}

	public void finalise() {
		// TODO Auto-generated method stub
		 System.out.println("-----------------------------");
		 
		 
		 	System.out.println("Human " + this.human.getName() + " is at " + this.human.getPosition().getName());
//	        System.out.println("Waiting passengers at " + stop1.getName() + ":" + stop1.getWaitingPassengers());
//	        System.out.println("Waiting passengers at " + stop2.getName() + ":" + stop2.getWaitingPassengers());
//	        System.out.println("Waiting passengers at " + stop3.getName() + ":" + stop3.getWaitingPassengers());
	        System.out.println("-----------------------------");
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
