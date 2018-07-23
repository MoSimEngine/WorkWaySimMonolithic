package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class LoadPassengersEvent extends AbstractSimEventDelegator<Bus> {

    public static final Duration LOADING_TIME_PER_PASSENGER = Duration.seconds(3);

    public LoadPassengersEvent(ISimulationModel model, String name) {
        super(model, name);
    }

    @Override
    public void eventRoutine(Bus bus) {
        BusStop position = bus.getPosition();
        int waitingPassengers = position.getPassengersInQueue();

        int servedPassengers = Math.min(waitingPassengers, bus.getTotalSeats());

        //Utils.log(bus, "Loading " + servedPassengers + " passengers at bus stop " + position + "...");
        bus.load(servedPassengers);

        int remainingPassengers = waitingPassengers - servedPassengers;
        
        double totalLoadingTime = 0;
        totalLoadingTime = servedPassengers * Bus.LOADING_TIME_PER_PASSENGER.toSeconds().value();
        for (int i = 0; i < servedPassengers; i++){
        	Human h = position.getPassenger();
        	bus.transportHuman(h);

        	Utils.log(bus, "Loading " + h.getName() + " at positio + " + position.getName());
//           	HumanPickupEvent e = new HumanPickupEvent(this.getModel(), "Human Pickup", bus);
//           	e.schedule(h, Bus.LOADING_TIME_PER_PASSENGER.toSeconds().value());
        	
        	//picks up human from home busstop
    		if(bus.getPosition().equals(h.getHomeBusStop())){
    			h.driveToBusStopAtWork();
    		} else if (bus.getPosition().equals(h.getWorkBusStop())){
    			h.driveToBusStopAtHome();
      		} else {
    			throw new IllegalStateException("Human is collected, but not at correct stop");
    		}
    		h.setCollected(true);
    		h.addTimeToTimeDriven(totalLoadingTime);
        }
        //position.setWaitingPassengers(remainingPassengers);

        // wait until all passengers have entered the bus
        //double loadingTime = servedPassengers * LOADING_TIME_PER_PASSENGER.toSeconds().value();

        // schedule load finished event
        LoadFinishedEvent e = new LoadFinishedEvent(totalLoadingTime, remainingPassengers, this.getModel(), "LoadFinished");
        e.schedule(bus, totalLoadingTime);

    }

}
