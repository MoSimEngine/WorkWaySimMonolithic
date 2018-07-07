package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.processes;

import de.uka.ipd.sdq.probfunction.math.apache.impl.PoissonDistribution;
import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimProcessDelegator;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;

public class PassengerArrivalProcess extends AbstractSimProcessDelegator {

    private BusStop busStop;

    private Duration interarrivalTime;

    private PoissonDistribution d;

    public PassengerArrivalProcess(BusStop busStop, Duration interarrivalTime) {
        super(busStop.getModel(), busStop.getName() + "_arrivalProcess");
        this.busStop = busStop;

        this.d = new PoissonDistribution(interarrivalTime.toSeconds().value());

        this.interarrivalTime = interarrivalTime;
    }

    @Override
    public void lifeCycle() {
        while (getModel().getSimulationControl().isRunning()) {
            // One passenger arrives at the associated bus stop
            int waiting = busStop.getWaitingPassengers();
            busStop.setWaitingPassengers(waiting + 1);

            double s = d.drawSample();
            // System.out.println(s);
            passivate(s);
        }
    }

}
