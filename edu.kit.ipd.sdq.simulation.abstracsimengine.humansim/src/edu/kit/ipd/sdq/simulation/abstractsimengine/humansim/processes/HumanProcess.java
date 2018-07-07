package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.processes;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimProcessDelegator;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Route.RouteSegment;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanProcess extends AbstractSimProcessDelegator {

    private Human human;

    public HumanProcess(Human human) {
        super(human.getModel(), human.getName());
        this.human = human;
    }

    @Override
    public void lifeCycle() {
        // goto Wörk ;)
        while (getModel().getSimulationControl().isRunning()) {
        	walkToBusStopAtHome();
        	waitForBus();
        	driveToBusStopAtWork();
        	walkFromBusStopToWork();
        	work();
        	walkToBusStopFromWork();
        	waitForBus();
        	driveToBusStopAtHome();
        	walkFromBusStopHome();
        	live();
        }
    }


	private void walkFromBusStopHome() {
		
		
	}

	private void live() {
		Utils.log(human, human.getName() + " lives his life. Black Jack and Hookers baby.");
		double livingHisLife = Human.FREETIME.toSeconds().value();
		passivate(livingHisLife);
		Utils.log(human, "Oh boy, time flies by... " + human.getName() + " stops living his life.");
	}

	private void loadPassengers() {
        BusStop position = bus.getPosition();
        int waitingPassengers = position.getWaitingPassengers();

        int servedPassengers = Math.min(waitingPassengers, bus.getTotalSeats());

        Utils.log(bus, "Loading " + servedPassengers + " passengers at bus stop " + position + "...");
        bus.load(servedPassengers);

        int remainingPassengers = waitingPassengers - servedPassengers;
        position.setWaitingPassengers(remainingPassengers);

        // wait until all passengers have entered the bus
        double loadingTime = servedPassengers * Bus.LOADING_TIME_PER_PASSENGER.toSeconds().value();
        passivate(loadingTime);

        Utils.log(bus, "Loading finished. Took " + loadingTime + " seconds.");

        if (remainingPassengers > 0) {
            Utils.log(bus, "Bus is full. Remaining passengers at bus station: " + position.getWaitingPassengers());
        }
    }

    private void travelToNextStation() {
        RouteSegment segment = bus.travel();

        Utils.log(bus, "Travelling to station " + segment.getTo());

        double drivingTime = Duration.hours(segment.getDistance() / (double) segment.getAverageSpeed()).toSeconds()
                .value();

        // wait for the bus to arrive at the next station
        passivate(drivingTime);

        // arrive at the target station
        BusStop currentStation = bus.arrive();
        Utils.log(bus, "Arrived at station " + currentStation + ". Travelling took " + drivingTime / 60.0 + " minutes.");
    }

    private void unloadPassengers() {
        BusStop position = bus.getPosition();
        int occupiedSeats = bus.getOccupiedSeats();

        Utils.log(bus, "Unloading " + occupiedSeats + " passengers at station " + position + "...");
        bus.unload();

        // wait for the passengers to leave the bus
        double unloadingTime = occupiedSeats * Bus.UNLOADING_TIME_PER_PASSENGER.toSeconds().value();
        passivate(unloadingTime);

        Utils.log(bus, "Unloading finished. Took " + unloadingTime + " seconds.");
    }

}