package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities;

import java.util.ArrayList;
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
		WALKING_DIRECTLY_HOME,
		
		DRIVING_BY_TAXI_TO_WORK,
		DRIVING_BY_TAXI_HOME,
		WAITING_FOR_TAXI,
	}
	
	
	public enum HumanBehaviour{
		DRIVING_BY_BUS,
		WALKING,
		DRIVING_BY_TAXI
	}

	private HumanState state;
	private HumanBehaviour behaviour;

	private BusStop homeBusStop;

	private BusStop workBusStop;

	private BusStop position;

	private BusStop destination;
	
	private volatile boolean collected;

	public final Duration HOME_TO_STATION = Duration.minutes(new Random().nextInt(60) + 1);

	public final Duration WORK_TO_STATION = Duration.minutes(new Random().nextInt(60) + 1);
	
	public final Duration WALK_DIRECTLY = Duration.minutes(WORK_TO_STATION.toMinutes().value() + HOME_TO_STATION.toMinutes().value() + Duration.minutes(new Random().nextInt(200) + 1).value());
	
	public final Duration DRIVE_BY_TAXI_TO_WORK = Duration.minutes(0.5*WORK_TO_STATION.toMinutes().value() + (new Random().nextInt(50) + 1));
	public final Duration DRIVE_BY_TAXI_HOME = Duration.minutes(0.5*HOME_TO_STATION.toMinutes().value() + (new Random().nextInt(50) + 1));
	
	
	public  final Duration WORKTIME = Duration.hours(8);
	
	
	
	public   Duration FREETIME = Duration.hours(0); 
	private Duration timeDriven = Duration.seconds(0);
	private ArrayList<Duration> awayFromHomeTimes;
	private ArrayList<Duration> busWaitingTimes;
	private ArrayList<Duration> drivingTimes;
	
	
	private Duration timeWaitedAtBusStop = Duration.seconds(0);
	
	private double timePointAtBusStop = 0;
	
	private boolean drivesByTaxiToWork;
	private boolean drivesByTaxiHome;
	
	public Taxi registeredTaxi;
	


	public Human(BusStop home, BusStop work, ISimulationModel model, String name) {
		super(model, name);
		homeBusStop = home;
		workBusStop = work;
		
		BusStop[] stops = {homeBusStop, workBusStop};
		
		// start at home
		position = home;
		state = HumanState.AT_HOME;
		
		behaviour = HumanBehaviour.values()[new Random().nextInt(2)];

		
		destination = workBusStop;
		
		drivesByTaxiToWork = new Random().nextBoolean();
		drivesByTaxiHome = new Random().nextBoolean();
		
		awayFromHomeTimes = new ArrayList<Duration>();
		busWaitingTimes = new ArrayList<Duration>();
		drivingTimes = new ArrayList<Duration>();
			
		System.out.println("Person: " + this.getName() + "HomeBS: " + home.getName() + " WorkBS:" + work.getName());
	
	}

	public void travel() {

	}

	//BusDriving state changes
	
	public void walkToBusStopAtHome() {
		if (behaviour.equals(HumanBehaviour.DRIVING_BY_BUS) && state.equals(HumanState.AT_HOME))
			state = HumanState.GO_TO_BUSSTOP_HOME;
		else
			throw new IllegalStateException("Human don't want to drive! He will walk!!!"+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveAtBusStopHome() {
		if (behaviour.equals(HumanBehaviour.DRIVING_BY_BUS) && state.equals(HumanState.GO_TO_BUSSTOP_HOME)){
			state = HumanState.AT_BUSSTOP_HOME;
			position = homeBusStop;
			arriveAtBusStopWalkingTimePointLog();
		}
		else
			throw new IllegalStateException("Human is lost! At least not at the Bus Stop at home"+ " CurrentState: " + this.state.toString());
	}

	public void driveToBusStopAtWork() {
		if(behaviour.equals(HumanBehaviour.DRIVING_BY_BUS) && state.equals(HumanState.AT_BUSSTOP_HOME)){
			state = HumanState.DRIVING_TO_WORK;
			position = null;
		}
		else
			throw new IllegalStateException("Human cannot drive to work!"+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveAtBusStopWorkByDriving(){
		if(behaviour.equals(HumanBehaviour.DRIVING_BY_BUS) && state.equals(HumanState.DRIVING_TO_WORK)){
			state = HumanState.AT_BUSSTOP_WORK;
			position = workBusStop;
		}
		else 
			throw new IllegalStateException("Human cannot arrive at work by car!"+ " CurrentState: " + this.state.toString());
	}
	
	public void walkToWorkFromBusStop() {
		if (behaviour.equals(HumanBehaviour.DRIVING_BY_BUS) && state.equals(HumanState.AT_BUSSTOP_WORK)){
			state = HumanState.WALK_TO_WORK_FROM_BUSSTOP;
			position = null;
		}
		else
			throw new IllegalStateException("Cannot walk from bus stop to work..."+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveAtWorkBus() {
		if(behaviour.equals(HumanBehaviour.DRIVING_BY_BUS) && state.equals(HumanState.WALK_TO_WORK_FROM_BUSSTOP)){
			state = HumanState.AT_WORK;
			this.setDestination(this.getHomeBusStop());
		}
		else 
			throw new IllegalStateException("Already at work"+ " CurrentState: " + this.state.toString());
	}
	
	
	public void walkToBusStopAtWork() {
		if (behaviour.equals(HumanBehaviour.DRIVING_BY_BUS) && state.equals(HumanState.AT_WORK))
			state = HumanState.GO_TO_BUSSTOP_WORK;
		else
			throw new IllegalStateException("Human don't want to drive! He will walk!!!"+ " CurrentState: " + this.state.toString());
	}

	public void arriveAtBusStopWork() {
		if (behaviour.equals(HumanBehaviour.DRIVING_BY_BUS) && state.equals(HumanState.GO_TO_BUSSTOP_WORK)){
			state = HumanState.AT_BUSSTOP_WORK;
			position = workBusStop;
			arriveAtBusStopWalkingTimePointLog();
			
		}
		else
			throw new IllegalStateException("Human is lost! At least not at the Bus Stop at work"+ " CurrentState: " + this.state.toString());
	}
	
	public void driveToBusStopAtHome() {
		if(behaviour.equals(HumanBehaviour.DRIVING_BY_BUS) && state.equals(HumanState.AT_BUSSTOP_WORK)){
			state = HumanState.DRIVING_HOME;
			position = null;
		}
		else
			throw new IllegalStateException("Human cannot drive home!"+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveAtBusStopHomeByDriving(){
		if(behaviour.equals(HumanBehaviour.DRIVING_BY_BUS) && state.equals(HumanState.DRIVING_HOME)){
			state = HumanState.AT_BUSSTOP_HOME;
			position = homeBusStop;
		}
		else 
			throw new IllegalStateException("Human cannot arrive at work by car!"+ " CurrentState: " + this.state.toString());
	}

	public void walkHomeFromBusStop() {
		if (behaviour.equals(HumanBehaviour.DRIVING_BY_BUS) && state.equals(HumanState.AT_BUSSTOP_HOME)){
			state = HumanState.WALK_HOME_FROM_BUSSTOP;
			position = null;
		}
		else
			throw new IllegalStateException("Cannot walk home from bus stop..."+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveHomeBus() {
		if(behaviour.equals(HumanBehaviour.DRIVING_BY_BUS) && state.equals(HumanState.WALK_HOME_FROM_BUSSTOP)){
			state = HumanState.AT_HOME;
			this.setDestination(workBusStop);
		}
		else 
			throw new IllegalStateException("Already at home"+ " CurrentState: " + this.state.toString());
	}
	
	
	
	//Walking state changes
	
	
	public void arriveAtWorkDirectlyWalking(){
		if(behaviour.equals(HumanBehaviour.WALKING) && state.equals(HumanState.WALKING_DIRECTLY_TO_WORK))
			state = HumanState.AT_WORK;
		else 
			throw new IllegalStateException("Human is not walking!!!"+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveAtHomeDirectlyWalking(){
		if(behaviour.equals(HumanBehaviour.WALKING) && state.equals(HumanState.WALKING_DIRECTLY_HOME))
			state = HumanState.AT_HOME;
		else 
			throw new IllegalStateException("Human is not walking!!!"+ " CurrentState: " + this.state.toString());
	}
	
	public void walkToWorkDirectly(){
		if(behaviour.equals(HumanBehaviour.WALKING) && state.equals(HumanState.AT_HOME))
			state = HumanState.WALKING_DIRECTLY_TO_WORK;
		else 
			throw new IllegalStateException("Human is lost, but not at home" + " CurrentState: " + this.state.toString());
	}
	
	public void walkHomeDirectly(){
		if(behaviour.equals(HumanBehaviour.WALKING) && state.equals(HumanState.AT_WORK))
			state = HumanState.WALKING_DIRECTLY_HOME;
		else 
			throw new IllegalStateException("Human is lost, but not at work" + " CurrentState: " + this.state.toString() );
	}
	
	//Taxi state change
	
	

	public void arriveAtWorkByTaxi(){
		if(behaviour.equals(HumanBehaviour.DRIVING_BY_TAXI) && state.equals(HumanState.DRIVING_BY_TAXI_TO_WORK))
			state = HumanState.AT_WORK;
		else 
			throw new IllegalStateException("Human is using a taxi!!!"+ " CurrentState: " + this.state.toString());
	}
	
	public void arriveAtHomeByTaxi(){
		if(behaviour.equals(HumanBehaviour.DRIVING_BY_TAXI) && state.equals(HumanState.DRIVING_BY_TAXI_HOME))
			state = HumanState.AT_HOME;
		else 
			throw new IllegalStateException("Human is using a taxi!!!"+ " CurrentState: " + this.state.toString());
	}
	
	public void driveToWorkByTaxi(){
		if(behaviour.equals(HumanBehaviour.DRIVING_BY_TAXI) && drivesByTaxiToWork && state.equals(HumanState.AT_BUSSTOP_HOME))
			state = HumanState.DRIVING_BY_TAXI_TO_WORK;
//		else 
//			throw new IllegalStateException("Human is lost, but not at home" + " CurrentState: " + this.state.toString());
	}
	
	public void driveHomeByTaxi(){
		if(behaviour.equals(HumanBehaviour.DRIVING_BY_TAXI) && drivesByTaxiHome && state.equals(HumanState.AT_BUSSTOP_WORK))
			state = HumanState.WALKING_DIRECTLY_HOME;
//		else 
//			throw new IllegalStateException("Human is lost, but not at work" + " CurrentState: " + this.state.toString() );
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

	public HumanBehaviour getBehaviour(){
		return behaviour;
	}
	
	public ArrayList<Duration> getBusWaitingTimes(){
		return busWaitingTimes;
	}
	
	public ArrayList<Duration> getDrivingTimes(){
		return drivingTimes;
	}
	
	public void setTimeDriven(Duration timeDriven) {
		this.timeDriven = timeDriven;
	}
	
	public boolean willWalk(){
		return behaviour.equals(HumanBehaviour.WALKING);
	}
	
	public ArrayList<Duration> getAwayFromHomeTimes(){
		return awayFromHomeTimes;
	}
	
	public void addTimeToTimeDriven(double additionalTimeInSeconds){
		double additional = this.timeDriven.toSeconds().value() + Duration.seconds(additionalTimeInSeconds).value();
		this.timeDriven = Duration.seconds(additional);
	}
	
	public void arriveAtBusStopWalkingTimePointLog(){
		
		if (timePointAtBusStop != 0.0)
			throw new IllegalStateException("time point arrived at bus stop was not zero");
		
		timePointAtBusStop = getModel().getSimulationControl().getCurrentSimulationTime();
	}
	
	public void calculateWaitedTime(){
		timeWaitedAtBusStop = Duration.seconds(timeWaitedAtBusStop.toSeconds().value() + Duration.seconds(getModel().getSimulationControl().getCurrentSimulationTime() - timePointAtBusStop).value());
		
		timePointAtBusStop = 0;
		Utils.log(this, "Caluclated New Waitingtime: " + timeWaitedAtBusStop.toSeconds().value() );
	}
	
	
	
	public void calculateFreeTime(){
		Duration onTheWay = Duration.seconds(0);
		if(behaviour.equals(HumanBehaviour.WALKING)){
			onTheWay = Duration.seconds(WORKTIME.toSeconds().value() + 2*WALK_DIRECTLY.toSeconds().value()); 
		} else if (behaviour.equals(HumanBehaviour.DRIVING_BY_BUS)){
			onTheWay = Duration.seconds(WORKTIME.toSeconds().value() + 2*HOME_TO_STATION.toSeconds().value() + 2* WORK_TO_STATION.toSeconds().value() + timeDriven.toSeconds().value() + timeWaitedAtBusStop.value());
		} else if (behaviour.equals(HumanBehaviour.DRIVING_BY_TAXI)){
		
			
			if(drivesByTaxiHome && drivesByTaxiToWork){
				onTheWay = Duration.seconds(
						// Home -> Station Home + Station_Home -> Work(Taxi)
						HOME_TO_STATION.toSeconds().value() + DRIVE_BY_TAXI_TO_WORK.toSeconds().value() 
						//WORKTIME
						+ WORKTIME.toSeconds().value() 
						// Work -> Station Work + Station Work -> Home(Taxi)
						+ WORK_TO_STATION.toSeconds().value() + DRIVE_BY_TAXI_HOME.toSeconds().value());
			} else if (drivesByTaxiToWork){
				onTheWay = Duration.seconds(
						// Home -> Station Home 
						HOME_TO_STATION.toSeconds().value() 
						// Drive Taxi Work
						+ DRIVE_BY_TAXI_TO_WORK.toSeconds().value()
						//WORKTIME
						+ WORKTIME.toSeconds().value()
						// Work -> Station Work
						+ WORK_TO_STATION.toSeconds().value()
						//Drive Bus
						+ timeDriven.toSeconds().value()
						//Way Home
						+ HOME_TO_STATION.toSeconds().value());
			} else if (drivesByTaxiHome){
				onTheWay = Duration.seconds(
						// Home -> Station Home + Station_Home -> Work(Taxi)
						HOME_TO_STATION.toSeconds().value() 
						//Drive Bus
						+ timeDriven.toSeconds().value()
						//Station Work -> Work
						+ WORK_TO_STATION.toSeconds().value()
						//WORKTIME
						+ WORKTIME.toSeconds().value()
						// Work -> Station Work
						+ WORK_TO_STATION.toSeconds().value()
						//Drive Taxi Home
						+ DRIVE_BY_TAXI_HOME.toSeconds().value());
			}	
			
			onTheWay = Duration.seconds(onTheWay.toSeconds().value() + timeWaitedAtBusStop.toSeconds().value());
		}
		
		
		
		//Utils.log(this, "On the way:" + onTheWay.toHours().value() + " hours. Waited " + timeWaitedAtBusStop.toMinutes().value() + " minutes at bus stops");
		
		
		
		awayFromHomeTimes.add(onTheWay);
		
		Duration newDrivingTime = Duration.seconds(timeDriven.toSeconds().value());
		Duration newWaitingTime = Duration.seconds(timePointAtBusStop);
		
		busWaitingTimes.add(timeWaitedAtBusStop);
		drivingTimes.add(timeDriven);
		double total= 24 - onTheWay.toHours().value();
		FREETIME = Duration.hours(total);
		this.timeDriven = Duration.seconds(0);
		timePointAtBusStop = 0;
		timeWaitedAtBusStop = Duration.seconds(0);
	}
	

}