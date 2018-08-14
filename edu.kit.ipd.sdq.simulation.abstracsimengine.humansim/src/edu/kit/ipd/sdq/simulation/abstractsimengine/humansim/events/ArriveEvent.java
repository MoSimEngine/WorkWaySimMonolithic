package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class ArriveEvent extends AbstractSimEventDelegator<Bus> {

    private double travelingTime;

    public ArriveEvent(double travelingTime, ISimulationModel model, String name) {
        super(model, name);
        this.travelingTime = travelingTime;
    }

    @Override
    public void eventRoutine(Bus bus) {
        // arrive at the target station
        BusStop currentStation = bus.arrive();
        
//        Utils.log(bus, "Arrived at station " + currentStation + ". Travelling took " + travelingTime / 60.0
//                + " minutes.");

        // schedule unloading event
        UnloadPassengersEvent e = new UnloadPassengersEvent(this.getModel(), "Unload Passengers");
        e.schedule(bus, 0);
    }

}
