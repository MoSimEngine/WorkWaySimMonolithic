package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events;

import java.util.LinkedList;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents.HumanArriveByBusAtBusStopWorkEvent;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class UnloadingFinishedEvent extends AbstractSimEventDelegator<Bus> {

    private double unloadingTime;

    public UnloadingFinishedEvent(double unloadingTime,ISimulationModel model, String name) {
        super(model, name);

        this.unloadingTime = unloadingTime;
    }

    @Override
    public void eventRoutine(Bus bus) {
        
    	if(unloadingTime > 0.0){
    	Utils.log(bus, "Unloading finished. Took " + this.unloadingTime + " seconds.");
    	}
        // schedule load passengers event
        LoadPassengersEvent e = new LoadPassengersEvent(this.getModel(), "Load Passengers");
        e.schedule(bus, 0);
    }

}
