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
		WALKING_DIRECTLY_TO_WORK,
		WALKING_DIRECTLY_HOME
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
	
	public static final Duration BUS_DRIVING_TIME = Duration.minutes(new Random().nextInt(60) + 1);
	
	public static final Duration WAITING_TIME_BUSSTOP = Duration.minutes(new Random().nextInt(60) + 1);
	
	public static final Duration WORKTIME = Duration.hours(8);
	
	public static final Duration FREETIME = Duration.hours(24 - WORKTIME.value() - HOME_TO_STATION.value() - WORK_TO_STATION.value() - (2*BUS_DRIVING_TIME.value()) - (2*WAITING_TIME_BUSSTOP.value()));
	


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

	//BusDriving state changes
	
	public void walkToBusStopAtHome() {
		if (!willWalk && state.equals(HumanState.AT_HOME))
			state = HumanState.GO_TO_BUSSTOP_HOME;
		else
			throw new IllegalStateException("Human don't want to drive! He will walk!!!");
	}
	
	public void arriveAtBusStopHome() {
		if (!willWalk && state.equals(HumanState.GO_TO_BUSSTOP_HOME)){
			state = HumanState.AT_BUSSTOP_HOME;
			setDestination(getWorkBusStop());
		}
		else
			throw new IllegalStateException("Human is lost! At least not at the Bus Stop at home");
	}

	public void driveToBusStopAtWork() {
		if(!willWalk && state.equals(HumanState.AT_BUSSTOP_HOME))
			state = HumanState.DRIVING_TO_WORK;
		else
			throw new IllegalStateException("Human cannot drive to work!");
	}
	
	public void arriveAtBusStopWorkByDriving(){
		if(!willWalk && state.equals(HumanState.DRIVING_TO_WORK))
			state = HumanState.AT_BUSSTOP_WORK;
		else 
			throw new IllegalStateException("Human cannot arrive at work by car!");
	}
	
	public void walkToWorkFromBusStop() {
		if (!willWalk && state.equals(HumanState.AT_BUSSTOP_WORK))
			state = HumanState.WALK_TO_WORK_FROM_BUSSTOP;
		else
			throw new IllegalStateException("Cannot walk from bus stop to work...");
	}
	
	public void arriveAtWork() {
		if(!state.equals(HumanState.WALK_TO_WORK_FROM_BUSSTOP)){
			state = HumanState.AT_WORK;
			this.setDestination(this.getWorkBusStop());
		}
		else 
			throw new IllegalStateException("Already at work");
	}
	
	public void walkToBusStopAtWork() {
		if (!willWalk && state.equals(HumanState.AT_WORK))
			state = HumanState.GO_TO_BUSSTOP_WORK;
		else
			throw new IllegalStateException("Human don't want to drive! He will walk!!!");
	}

	public void arriveAtBusStopWork() {
		if (!willWalk && state.equals(HumanState.GO_TO_BUSSTOP_WORK)){
			state = HumanState.AT_BUSSTOP_WORK;
			this.setDestination(this.getHomeBusStop());
		}
		else
			throw new IllegalStateException("Human is lost! At least not at the Bus Stop at work");
	}
	
	public void driveToBusStopAtHome() {
		if(!willWalk && state.equals(HumanState.AT_BUSSTOP_WORK))
			state = HumanState.DRIVING_HOME;
		else
			throw new IllegalStateException("Human cannot drive home!");
	}
	
	public void arriveAtBusStopHomeByDriving(){
		if(!willWalk && state.equals(HumanState.DRIVING_HOME))
			state = HumanState.AT_BUSSTOP_HOME;
		else 
			throw new IllegalStateException("Human cannot arrive at work by car!");
	}

	public void walkHomeFromBusStop() {
		if (!willWalk && state.equals(HumanState.AT_BUSSTOP_HOME))
			state = HumanState.WALK_HOME_FROM_BUSSTOP;
		else
			throw new IllegalStateException("Cannot walk home from bus stop...");
	}
	
	public void arriveHome() {
		if(!state.equals(HumanState.WALK_HOME_FROM_BUSSTOP)){
			state = HumanState.AT_HOME;
			this.setDestination(getHomeBusStop());
		}
		else 
			throw new IllegalStateException("Already at home");
	}
	
	
	
	//Walking state changes
	
	
	public void walkToWork(){
		if(!state.equals(HumanState.AT_HOME))
			state = HumanState.AT_WORK;
		else 
			throw new IllegalStateException("Human is lost, but not at home");
	}
	
	public void walkHome(){
		if(!state.equals(HumanState.AT_WORK))
			state = HumanState.AT_HOME;
		else 
			throw new IllegalStateException("Human is lost, but not at work");
	}
	
	
	
	public BusStop getPosition(){
		return this.position;
	}
	
	public void setPosition(BusStop Position){
		this.destination = Position;
	}
	
	public BusStop getDestination(){
		return this.position;
	}
	
	public void setDestination(BusStop destination){
		this.destination = destination;
	}
	
	public BusStop getHomeBusStop(){
		return this.homeBusStop;
	}
	
	public BusStop getWorkBusStop(){
		return this.workBusStop;
	}
	
	public boolean getWillWalk(){
		return this.willWalk;
	}
	

}