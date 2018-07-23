package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events;

import java.util.LinkedList;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents.HumanArriveByBusAtBusStopWorkEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents.HumanArriveByBustBusStopHomeEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class UnloadPassengersEvent extends AbstractSimEventDelegator<Bus> {

    protected UnloadPassengersEvent(ISimulationModel model, String name) {
        super(model, name);
    }

    @Override
    public void eventRoutine(Bus bus) {
        BusStop position = bus.getPosition();
        int occupiedSeats = bus.getOccupiedSeats();
        LinkedList<Human> tmpHumans = new LinkedList<Human>();
        //Utils.log(bus, "Unloading " + occupiedSeats + " passengers at station " + position + "...");
        bus.unload();

        // wait for the passengers to leave the bus
        int numTransportedHumanSize = bus.getNumTransportedHumans();
        double totalUnloadingTime = 0.0;
        for(int i = 0; i < numTransportedHumanSize; i++){
        	
        	
        	
        	Human h = bus.unloadHuman();
        	if(h != null & h.getDestination().equals(bus.getPosition())){
        		Utils.log(bus, "Unloading " + h.getName() + " at position " + position.getName());
        		totalUnloadingTime += Bus.UNLOADING_TIME_PER_PASSENGER.toSeconds().value();
        		tmpHumans.add(h);
        		
        		if(bus.getPosition().equals(h.getHomeBusStop())){
        			HumanArriveByBustBusStopHomeEvent e = new HumanArriveByBustBusStopHomeEvent(this.getModel(), "Human arrived at busstop home by bus");
        			e.schedule(h, Bus.UNLOADING_TIME_PER_PASSENGER.toSeconds().value());
    			} else if (bus.getPosition().equals(h.getWorkBusStop())){
    				HumanArriveByBusAtBusStopWorkEvent e = new HumanArriveByBusAtBusStopWorkEvent(this.getModel(), "Human arrived at bustop work by bus");
    				e.schedule(h, Bus.UNLOADING_TIME_PER_PASSENGER.toSeconds().value());
    			} else {
    				throw new IllegalStateException("Human is thrown out, but not at correct stop");
    			}
        	} else {
        		bus.transportHuman(h);
        	}
        }
        
        for (Human human : tmpHumans) {
			human.addTimeToTimeDriven(totalUnloadingTime);
		}
        for (Human human : bus.getTransportedHumans()) {
			human.addTimeToTimeDriven(totalUnloadingTime);
		}
        
        
        UnloadingFinishedEvent e = new UnloadingFinishedEvent(totalUnloadingTime, this.getModel(), "Unload Finished");
        e.schedule(bus, totalUnloadingTime);

    
    }
}
