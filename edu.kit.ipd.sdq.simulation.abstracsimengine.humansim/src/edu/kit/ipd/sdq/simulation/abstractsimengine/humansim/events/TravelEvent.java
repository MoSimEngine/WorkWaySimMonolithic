package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Route.RouteSegment;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class TravelEvent extends AbstractSimEventDelegator<Bus> {

    public TravelEvent(ISimulationModel model, String name) {
        super(model, name);
    }

    @Override
    public void eventRoutine(Bus bus) {
        RouteSegment segment = bus.travel();

        //Utils.log(bus, "Travelling to station " + segment.getTo());

        double drivingTime = Duration.hours(segment.getDistance() / (double) segment.getAverageSpeed()).toSeconds()
                .value();

//        Utils.log(bus, "next time" + getModel().getSimulationControl().getCurrentSimulationTime() + drivingTime);
        // wait for the bus to arrive at the next station^
        ArriveEvent e = new ArriveEvent(drivingTime, this.getModel(), "Arrive Event");
        e.schedule(bus, drivingTime);
    }

}
