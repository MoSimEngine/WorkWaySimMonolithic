package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEntityDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;

public class BusStop extends AbstractSimEntityDelegator {

    private volatile int waitingPassengers;
    
    private ConcurrentLinkedQueue<Human> passengers;
    

    
    public BusStop(ISimulationModel model, String name) {
        super(model, name);
        
       passengers = new ConcurrentLinkedQueue<Human>();
    }

    public int getWaitingPassengers() {
        return waitingPassengers;
    }

    public void setWaitingPassengers(int waitingPassengers) {
        this.waitingPassengers = waitingPassengers;
    }
    
    public synchronized void setHuman(Human human){
    	passengers.add(human);
    }
    
    public synchronized Human getPassenger(){
    	return passengers.remove();
    }
    
    public int getPassengersInQueue(){
    	if(passengers.isEmpty()){
    		return 0;
    	} else {
    		return passengers.size();
    	}	
    }
    
    @Override
    public String toString() {
        return getName();
    }

}
