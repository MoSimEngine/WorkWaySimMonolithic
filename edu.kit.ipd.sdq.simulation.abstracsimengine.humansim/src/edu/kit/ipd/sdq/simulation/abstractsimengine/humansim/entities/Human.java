package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities;

import java.util.Random;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEntityDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;

public class Human extends AbstractSimEntityDelegator {

	public enum HumanState {
		AT_HOME, 
		AT_WORK, 
		GO_TO_BUSSTOP_HOME, 
		GO_TO_BUSSTOP_WORK, 
		AT_BUSSTOP_HOME, 
		AT_BUSSTOP_WORK,
		DRIVING_HOME,
		DRIVING_TO_WORK,
		WALK_HOME_FROM_BUSSTOP,
		WALK_TO_WORK_FROM_BUSSTOP,
//		
//		
//		WALKING, 
//		DRIVING
	}

	private HumanState state;

	private boolean willWalk;

	private BusStop homeBusStop;

	private BusStop workBusStop;

	private BusStop position;

	private BusStop destination;

	public static final Duration HOME_TO_STATION = Duration.minutes(new Random().nextInt(60) + 1);

	public static final Duration WORK_TO_STATION = Duration.minutes(new Random().nextInt(60) + 1);
	
	public static final Duration WORKTIME = Duration.hours(8);
	
	public static final Duration FREETIME = Duration.hours(24 - WORKTIME.value() - HOME_TO_STATION.value() - WORK_TO_STATION.value());

	public Human(BusStop home, BusStop work, ISimulationModel model, String name) {
		super(model, name);
		homeBusStop = home;
		workBusStop = work;

		// start at home
		position = home;
		state = HumanState.AT_HOME;
		willWalk = new Random().nextBoolean();
	}

	public void travel() {

	}

	public void walkToBusStopAtHome() {
		if (!willWalk && state.equals(HumanState.AT_HOME))
			state = HumanState.GO_TO_BUSSTOP_HOME;
		else
			throw new IllegalStateException("Human don't want to drive! He will walk!!!");
	}

	public void walkToBusStopAtWork() {
		if (!willWalk && state.equals(HumanState.AT_WORK))
			state = HumanState.GO_TO_BUSSTOP_WORK;
		else
			throw new IllegalStateException("Human don't want to drive! He will walk!!!");
	}

	public void arriveAtBusStopHome() {
		if (!willWalk && state.equals(HumanState.GO_TO_BUSSTOP_HOME))
			state = HumanState.AT_BUSSTOP_HOME;
		else
			throw new IllegalStateException("Human is lost! At least not at the Bus Stop at home");
	}

	public void arrvieAtBusStopWork() {
		if (!willWalk && state.equals(HumanState.GO_TO_BUSSTOP_WORK))
			state = HumanState.AT_BUSSTOP_WORK;
		else
			throw new IllegalStateException("Human is lost! At least not at the Bus Stop at work");
	}
	
	public void driveToBusStopAtWork() {
		if(!willWalk && state.equals(HumanState.AT_BUSSTOP_HOME))
			state = HumanState.DRIVING_TO_WORK;
		else
			throw new IllegalStateException("Human cannot drive to work!");
	}
	
	public void driveToBusStopAtHome() {
		if(!willWalk && state.equals(HumanState.AT_BUSSTOP_WORK))
			state = HumanState.DRIVING_HOME;
		else
			throw new IllegalStateException("Human cannot drive home!");
	}

	public void walkToWorkFromBusStop() {
		if (!willWalk && state.equals(HumanState.AT_BUSSTOP_WORK))
			state = HumanState.WALK_TO_WORK_FROM_BUSSTOP;
		else
			throw new IllegalStateException("Cannot walk from bus stop to work...");
	}
	
	public void walkHomeFromBusStop() {
		if (!willWalk && state.equals(HumanState.AT_BUSSTOP_HOME))
			state = HumanState.WALK_HOME_FROM_BUSSTOP;
		else
			throw new IllegalStateException("Cannot walk home from bus stop...");
	}
	
	public void arriveHome() {
		if(!state.equals(HumanState.AT_HOME))
			state = HumanState.AT_HOME;
		else 
			throw new IllegalStateException("Already at home");
	}
	
	public void arriveAtWork() {
		if(!state.equals(HumanState.AT_WORK))
			state = HumanState.AT_WORK;
		else 
			throw new IllegalStateException("Already at work");
	}


}