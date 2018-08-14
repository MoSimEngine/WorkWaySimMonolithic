package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents.HumanEntersBusEvent;
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
        
        for (int i = 0; i < servedPassengers; i++){
        	Human h = position.getPassenger();
        	bus.transportHuman(h);
        	double loadingTime = Bus.LOADING_TIME_PER_PASSENGER.toSeconds().value();
        	Utils.log(bus, "Loading " + h.getName() + " at positio + " + position.getName());
//           	HumanPickupEvent e = new HumanPickupEvent(this.getModel(), "Human Pickup", bus);
//           	e.schedule(h, Bus.LOADING_TIME_PER_PASSENGER.toSeconds().value());
        	
        	if(HumanSimValues.USE_SPIN_WAIT){
        		h.setCollected(true);
        	} else {
        		HumanEntersBusEvent e = new HumanEntersBusEvent(h.getModel(), "HumanEntersBus");
        		e.schedule(h, loadingTime);
        	}
        	//picks up human from home busstop
    		
    		
        }
        //position.setWaitingPassengers(remainingPassengers);

        // wait until all passengers have entered the bus
        //double loadingTime = servedPassengers * LOADING_TIME_PER_PASSENGER.toSeconds().value();

        // schedule load finished event
        //Utils.log(bus, "next time" + getModel().getSimulationControl().getCurrentSimulationTime() + totalLoadingTime);
        LoadFinishedEvent e = new LoadFinishedEvent(totalLoadingTime, remainingPassengers, this.getModel(), "LoadFinished");
        e.schedule(bus, totalLoadingTime);

    }

}
