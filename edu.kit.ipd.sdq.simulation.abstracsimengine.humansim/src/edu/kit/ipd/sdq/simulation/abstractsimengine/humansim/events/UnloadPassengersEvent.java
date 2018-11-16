package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events;

import java.util.LinkedList;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents.HumanExitsBusEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents.HumanExitsBusSpinWait;
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

        
        double totalUnloadingTime = 0.0;
        double unloadingTime = Bus.UNLOADING_TIME_PER_PASSENGER.toSeconds().value();
        int transportedHumans = bus.getNumTransportedHumans();
        for(int i = 0; i < transportedHumans; i++){
        	Human h = bus.unloadHuman();
        	//System.out.println(h.getDestination().getName() + ":"  + bus.getPosition().getName());
        	if(h.getDestination().equals(bus.getPosition())){
        		
        		Utils.log(bus, "Unloading " + h.getName() + " at position " + position.getName());
        		totalUnloadingTime += unloadingTime;
        		
            			HumanExitsBusEvent e = new HumanExitsBusEvent(getModel(), "HumanExitsBus");
            			
            			e.schedule(h, Bus.UNLOADING_TIME_PER_PASSENGER.toSeconds().value());
            			
            		
        	} else {
        			bus.transportHuman(h);
        		}
        }
//        Utils.log(bus, "next time" + getModel().getSimulationControl().getCurrentSimulationTime() + totalUnloadingTime);
        UnloadingFinishedEvent e = new UnloadingFinishedEvent(totalUnloadingTime, this.getModel(), "Unload Finished");
        e.schedule(bus, totalUnloadingTime);

    
    }
}
