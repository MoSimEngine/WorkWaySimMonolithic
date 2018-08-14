package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.processes;

import java.util.concurrent.ConcurrentLinkedQueue;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimProcessDelegator;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Route.RouteSegment;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class BusProcess extends AbstractSimProcessDelegator {

    private Bus bus;

    public BusProcess(Bus bus) {
        super(bus.getModel(), bus.getName());
        this.bus = bus;
        
    }

    @Override
    public void lifeCycle() {
        // transport passengers between the different stations
        while (getModel().getSimulationControl().isRunning()) {
            loadPassengers();
            travelToNextStation();
            unloadPassengers();
        }
    }

    private void loadPassengers() {
        BusStop position = bus.getPosition();
        //int waitingPassengers = position.getWaitingPassengers();
        int waitingPassengers = position.getPassengersInQueue();
        
        int servedPassengers = Math.min(waitingPassengers, bus.getTotalSeats());

        //Utils.log(bus, "Loading " + servedPassengers + " passengers at bus stop " + position + "...");
        bus.load(servedPassengers);

        int remainingPassengers = waitingPassengers - servedPassengers;
        //position.setWaitingPassengers(remainingPassengers);

        // wait until all passengers have entered the bus
        double totalLoadingTime = 0;
        for (int i = 0; i < servedPassengers; i++){
        	Human h = position.getPassenger();
        	bus.transportHuman(h);
        	
        	
        	double loadingTime = Bus.LOADING_TIME_PER_PASSENGER.toSeconds().value();
        	
        	totalLoadingTime += loadingTime;
        	Utils.log(bus, "Loading " + h.getName() + " at positio + " + position.getName());
            
            h.setCollected(true);

            passivate(loadingTime);
        }
        
        
        //double loadingTime =  servedPassengers * Bus.LOADING_TIME_PER_PASSENGER.toSeconds().value();

        //Utils.log(bus, "Loading finished. Took " + loadingTime + " seconds.");
        //Utils.log(bus, "Loading finished. Took " + totalLoadingTime + " seconds.");

        if (remainingPassengers > 0) {
           // Utils.log(bus, "Bus is full. Remaining passengers at bus station: " + position.getWaitingPassengers());
        }
        
    }

    private void travelToNextStation() {
        RouteSegment segment = bus.travel();

        //Utils.log(bus, "Travelling to station " + segment.getTo());

        double drivingTime = Duration.hours(segment.getDistance() / (double) segment.getAverageSpeed()).toSeconds()
                .value();

        // wait for the bus to arrive at the next station
        passivate(drivingTime);

        // arrive at the target station
        BusStop currentStation = bus.arrive();
        // Utils.log(bus, "Arrived at station " + currentStation + ". Travelling took " + drivingTime / 60.0 + " minutes.");
    }

    private void unloadPassengers() {
        BusStop position = bus.getPosition();
        int occupiedSeats = bus.getOccupiedSeats();

        //Utils.log(bus, "Unloading " + occupiedSeats + " passengers at station " + position + "...");
        bus.unload();
        double totalUnloadingTime = 0.0;
        double unloadingTime = Bus.UNLOADING_TIME_PER_PASSENGER.toSeconds().value();
        for(int i = 0; i < bus.getTransportedHumans().size(); i++){
        	Human h = bus.unloadHuman();
        	if(h.getDestination().equals(bus.getPosition())){
        		Utils.log(bus, "Unloading " + h.getName() + " at positio + " + position.getName());
        		totalUnloadingTime += unloadingTime;
        		passivate(unloadingTime);
        		h.setCollected(false);
        	} else {
        		bus.transportHuman(h);
        	}
        }

        // wait for the passengers to leave the bus
//        double unloadingTime = 100 *  Bus.UNLOADING_TIME_PER_PASSENGER.toSeconds().value();
        //passivate(totalUnloadingTime);

       //Utils.log(bus, "Unloading finished. Took " + totalUnloadingTime + " seconds.");
    }

}