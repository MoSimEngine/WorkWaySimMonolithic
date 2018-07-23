package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities;

import java.util.Random;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEntityDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

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
	
	private volatile boolean collected;

	public final Duration HOME_TO_STATION = Duration.minutes(new Random().nextInt(60) + 1);

	public final Duration WORK_TO_STATION = Duration.minutes(new Random().nextInt(60) + 1);

	public  final Duration WALKING_DURATION_WITHOUT_BUS =  Duration.minutes(2*(HOME_TO_STATION.toMinutes().value() + WORK_TO_STATION.toMinutes().value()  + Duration.minutes(new Random().nextInt(150)).value()));
	
	public  final Duration WORKTIME = Duration.hours(8);
	
	public   Duration FREETIME = Duration.hours(0); 
	private Duration timeDriven = Duration.seconds(0);
	


	public Human(BusStop home, BusStop work, ISimulationModel model, String name) {
		super(model, name);
		homeBusStop = home;
		workBusStop = work;

		// start at home
		position = home;
		state = HumanState.AT_HOME;
		willWalk =  new Random().nextBoolean();
		//willWalk =  false;
		destination = workBusStop;
		
		
		System.out.println("Person: " + this.getName() + "HomeBS: " + home.getName() + " WorkBS:" + work.getName());
	
	}

	public void travel() {

	}

	//BusDriving state changes
	
	public void walkToBusStopAtHome() {
		if (!willWalk && state.equals(HumanState.AT_HOME))
			state = HumanState.GO_TO_BUSSTOP_HOME;
		else
			throw new IllegalStateException("Human don't want to drive! He will walk!!!"+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveAtBusStopHome() {
		if (!willWalk && state.equals(HumanState.GO_TO_BUSSTOP_HOME)){
			state = HumanState.AT_BUSSTOP_HOME;
			position = homeBusStop;
		}
		else
			throw new IllegalStateException("Human is lost! At least not at the Bus Stop at home"+ " CurrentState: " + this.state.toString());
	}

	public void driveToBusStopAtWork() {
		if(!willWalk && state.equals(HumanState.AT_BUSSTOP_HOME)){
			state = HumanState.DRIVING_TO_WORK;
			position = null;
		}
		else
			throw new IllegalStateException("Human cannot drive to work!"+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveAtBusStopWorkByDriving(){
		if(!willWalk && state.equals(HumanState.DRIVING_TO_WORK)){
			state = HumanState.AT_BUSSTOP_WORK;
			position = workBusStop;
		}
		else 
			throw new IllegalStateException("Human cannot arrive at work by car!"+ " CurrentState: " + this.state.toString());
	}
	
	public void walkToWorkFromBusStop() {
		if (!willWalk && state.equals(HumanState.AT_BUSSTOP_WORK)){
			state = HumanState.WALK_TO_WORK_FROM_BUSSTOP;
			position = null;
		}
		else
			throw new IllegalStateException("Cannot walk from bus stop to work..."+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveAtWorkBus() {
		if(!willWalk && state.equals(HumanState.WALK_TO_WORK_FROM_BUSSTOP)){
			state = HumanState.AT_WORK;
			this.setDestination(this.getHomeBusStop());
		}
		else 
			throw new IllegalStateException("Already at work"+ " CurrentState: " + this.state.toString());
	}
	
	
	public void walkToBusStopAtWork() {
		if (!willWalk && state.equals(HumanState.AT_WORK))
			state = HumanState.GO_TO_BUSSTOP_WORK;
		else
			throw new IllegalStateException("Human don't want to drive! He will walk!!!"+ " CurrentState: " + this.state.toString());
	}

	public void arriveAtBusStopWork() {
		if (!willWalk && state.equals(HumanState.GO_TO_BUSSTOP_WORK)){
			state = HumanState.AT_BUSSTOP_WORK;
			position = workBusStop;
			
		}
		else
			throw new IllegalStateException("Human is lost! At least not at the Bus Stop at work"+ " CurrentState: " + this.state.toString());
	}
	
	public void driveToBusStopAtHome() {
		if(!willWalk && state.equals(HumanState.AT_BUSSTOP_WORK)){
			state = HumanState.DRIVING_HOME;
			position = null;
		}
		else
			throw new IllegalStateException("Human cannot drive home!"+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveAtBusStopHomeByDriving(){
		if(!willWalk && state.equals(HumanState.DRIVING_HOME)){
			state = HumanState.AT_BUSSTOP_HOME;
			position = homeBusStop;
		}
		else 
			throw new IllegalStateException("Human cannot arrive at work by car!"+ " CurrentState: " + this.state.toString());
	}

	public void walkHomeFromBusStop() {
		if (!willWalk && state.equals(HumanState.AT_BUSSTOP_HOME)){
			state = HumanState.WALK_HOME_FROM_BUSSTOP;
			position = null;
		}
		else
			throw new IllegalStateException("Cannot walk home from bus stop..."+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveHomeBus() {
		if(!willWalk && state.equals(HumanState.WALK_HOME_FROM_BUSSTOP)){
			state = HumanState.AT_HOME;
			this.setDestination(workBusStop);
		}
		else 
			throw new IllegalStateException("Already at home"+ " CurrentState: " + this.state.toString());
	}
	
	
	
	//Walking state changes
	
	
	public void arriveAtWorkDirectlyWalking(){
		if(willWalk && state.equals(HumanState.WALKING_DIRECTLY_TO_WORK))
			state = HumanState.AT_WORK;
		else 
			throw new IllegalStateException("Human is not walking!!!"+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveAtHomeDirectlyWalking(){
		if(willWalk && state.equals(HumanState.WALKING_DIRECTLY_HOME))
			state = HumanState.AT_HOME;
		else 
			throw new IllegalStateException("Human is not walking!!!"+ " CurrentState: " + this.state.toString());
	}
	
	public void walkToWorkDirectly(){
		if(willWalk && state.equals(HumanState.AT_HOME))
			state = HumanState.WALKING_DIRECTLY_TO_WORK;
		else 
			throw new IllegalStateException("Human is lost, but not at home" + " CurrentState: " + this.state.toString());
	}
	
	public void walkHomeDirectly(){
		if(willWalk && state.equals(HumanState.AT_WORK))
			state = HumanState.WALKING_DIRECTLY_HOME;
		else 
			throw new IllegalStateException("Human is lost, but not at work" + " CurrentState: " + this.state.toString() );
	}
	
	
	
	public BusStop getPosition(){
		return this.position;
	}
	
	public void setPosition(BusStop position){
		this.position = position;
	}
	
	public BusStop getDestination(){
		return this.destination;
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

	public boolean isCollected() {
		return collected;
	}

	public void setCollected(boolean collected) {
		this.collected = collected;
	}
	
	public HumanState getState(){
		return this.state;
	}

	public Duration getTimeDriven() {
		return timeDriven;
	}

	public void setTimeDriven(Duration timeDriven) {
		this.timeDriven = timeDriven;
	}
	
	public void addTimeToTimeDriven(double additionalTimeInSeconds){
		double additional = this.timeDriven.toSeconds().value() + Duration.seconds(additionalTimeInSeconds).value();
		this.timeDriven = Duration.seconds(additional);
	}
	
	public void calculateFreeTime(){
		if(!willWalk){
			
			double freeTimeWithOutDriven = 24 - 2*(WORKTIME.toHours().value() - HOME_TO_STATION.toHours().value() - WORK_TO_STATION.toHours().value());
			double total= freeTimeWithOutDriven - timeDriven.toHours().value();
			FREETIME = Duration.hours(total);
			this.timeDriven = Duration.seconds(0);
		} else {	
			
			FREETIME = Duration.hours(24 - WORKTIME.toHours().value() - WALKING_DURATION_WITHOUT_BUS.toHours().value());
			if(FREETIME.value() < 0.00){
				FREETIME = Duration.hours(0);
				Utils.log(this, this.getName() + " wow... walking does not leave much free time");
			}
		}
	}
	

}