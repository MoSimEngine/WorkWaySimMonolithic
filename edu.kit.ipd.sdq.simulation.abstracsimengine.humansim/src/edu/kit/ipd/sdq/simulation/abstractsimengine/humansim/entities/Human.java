package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEntityDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Position.PositionType;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;



public class Human extends AbstractSimEntityDelegator {

	public enum HumanState {
		AT_HOME, 
		AT_WORK, 
		TRAVELLING,
		WALKING,
		AT_BUSSTOP,
	}
	
	
	public enum HumanBehaviour{
		DRIVING_BY_BUS,
		WALKING_DIRECTLY,
	}

	private HumanState state;
	private HumanBehaviour behaviour;

	private Position position;

	private Position destination;
	
	private volatile boolean collected;

	public Duration HOME_TO_STATION = Duration.minutes(new Random().nextInt(60) + 1);

	public Duration WORK_TO_STATION = Duration.minutes(new Random().nextInt(60) + 1);
	
	public Duration WALK_DIRECTLY = Duration.minutes(Duration.minutes(new Random().nextInt(200) + 1).value());
	
	public  final Duration WORKTIME = Duration.hours(8);
	private ArrayList<Position> workway = new ArrayList<Position>();
	private int positionIndex = 0;
	//Regulates the index direction for traversal of the workway list 
	// Values: 1 -> forward; -1 -> backward;
	private int direction = 1;
	
	
	public   Duration FREETIME = Duration.hours(0); 
	private Duration timeDriven = Duration.seconds(0);
	private ArrayList<Duration> awayFromHomeTimes;
	private ArrayList<Duration> busWaitingTimes;
	private ArrayList<Duration> drivingTimes;
	private ArrayList<Duration> freeTimes;	
	
	private Duration timeWaitedAtBusStop = Duration.seconds(0);
	
	private double timePointAtBusStop = 0.0;
	private double timePointCollected = 0.0;

	public Human(BusStop home, BusStop work, ISimulationModel model, String name) {
		super(model, name);

//		Utils.log(this, "HomeBS: " + home.getName() + " WorkBS: " + work.getName());
		// start at home
		
		state = HumanState.AT_HOME;
		
		if(HumanSimValues.WALKING_ENABLED){
		behaviour = HumanBehaviour.values()[new Random().nextInt(2)];
		} else {
			behaviour = HumanBehaviour.DRIVING_BY_BUS;
		}
		
		workway.add(new Position(model, "Home", PositionType.HOME));
		
		if(!behaviour.equals(HumanBehaviour.WALKING_DIRECTLY)) {
			workway.add(home);
			workway.add(work);
		}
		
		workway.add(new Position(model, "Work", PositionType.WORK));
		
//		for (int i = 0; i < workway.size(); i++) {
//			System.out.print(workway.get(i).getName() + "->");
//		}
		
		position = workway.get(positionIndex);
		destination = workway.get(positionIndex+1);
		
		if(HumanSimValues.RANDOMIZED_HUMAN_VALUES){
			HOME_TO_STATION = Duration.minutes(new Random().nextInt(60) + 1);
			WORK_TO_STATION = Duration.minutes(new Random().nextInt(60) + 1);
			WALK_DIRECTLY = Duration.minutes(Duration.minutes(new Random().nextInt(200) + 1).value());
		} else {
			HOME_TO_STATION = Duration.minutes(30);
			WORK_TO_STATION = Duration.minutes(30);
			WALK_DIRECTLY = Duration.minutes(90);
		}
		
		awayFromHomeTimes = new ArrayList<Duration>();
		busWaitingTimes = new ArrayList<Duration>();
		drivingTimes = new ArrayList<Duration>();
		freeTimes = new ArrayList<Duration>();	
		System.out.println("Person: " + this.getName() + "HomeBS: " + home.getName() + " WorkBS:" + work.getName());
	
	}

	public Human(ArrayList<Position> route, ISimulationModel model, String name){
		super(model, name);
	
		// start at home
		state = HumanState.AT_HOME;
		if(HumanSimValues.WALKING_ENABLED){
			behaviour = HumanBehaviour.values()[new Random().nextInt(2)];
			} else {
				behaviour = HumanBehaviour.DRIVING_BY_BUS;
			}
		
		this.workway = route;
		
		position = workway.get(positionIndex);
		destination = workway.get(positionIndex+1);
		
		if(HumanSimValues.RANDOMIZED_HUMAN_VALUES){
			HOME_TO_STATION = Duration.minutes(new Random().nextInt(60) + 1);
			WORK_TO_STATION = Duration.minutes(new Random().nextInt(60) + 1);
			WALK_DIRECTLY = Duration.minutes(Duration.minutes(new Random().nextInt(200) + 1).value());
		} else {
			HOME_TO_STATION = Duration.minutes(30);
			WORK_TO_STATION = Duration.minutes(30);
			WALK_DIRECTLY = Duration.minutes(90);
		}
		
		awayFromHomeTimes = new ArrayList<Duration>();
		busWaitingTimes = new ArrayList<Duration>();
		drivingTimes = new ArrayList<Duration>();
		freeTimes = new ArrayList<Duration>();	
		
	}

	
	//BusDriving state changes
	
	public void walkToNext() {
		if(state.equals(HumanState.AT_HOME) || state.equals(HumanState.AT_WORK) || state.equals(HumanState.AT_BUSSTOP)) {
			state = HumanState.WALKING;
		} else {
			throw new IllegalStateException("Human cannot walk!" + "CurrentState: " + this.state.toString());
		}
	}
	
	public void travellingToNext() {
		if(state.equals(HumanState.AT_BUSSTOP)) {
			state = HumanState.TRAVELLING;
		} else {
			throw new IllegalStateException("How to drive by bus when not at BusStop???" + "CurrentState: " + this.state.toString());
		}
	}
	
	public void arriveAtWork() {
		if(state.equals(HumanState.WALKING)) {
			state = HumanState.AT_WORK;
		} else {
			throw new IllegalStateException("There is no teleportation to work!" + "CurrentState: " + this.state.toString());
		}
	}
	
	public void arriveAtHome() {
		if(state.equals(HumanState.WALKING)) {
			state = HumanState.AT_HOME;
			direction = 1;
		} else {
			throw new IllegalStateException("There is no teleportation to home!" + "CurrentState: " + this.state.toString());
		}
	}
	
	public void arriveAtBusStop() {
		if(state.equals(HumanState.WALKING) || state.equals(HumanState.TRAVELLING)) {
			state = HumanState.AT_BUSSTOP;
		} else {
			throw new IllegalStateException("There is no teleportation to a BusStop!" + "CurrentState: " + this.state.toString());
		}
	}
	




	
	public ArrayList<Duration> getFreeTimes(){
		return freeTimes;
	}
	
	public Position getPosition(){
		return this.position;
	}
	
	public Position getDestination(){
		return this.destination;
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

	public HumanBehaviour getBehaviour(){
		return behaviour;
	}
	
	public ArrayList<Duration> getBusWaitingTimes(){
		return busWaitingTimes;
	}
	
	public ArrayList<Duration> getDrivingTimes(){
		return drivingTimes;
	}
	
	public ArrayList<Duration> getAwayFromHomeTimes(){
		return awayFromHomeTimes;
	}
	
	public ArrayList<Position> getWorkway(){
		return workway;
	}
	

	public void arriveAtBusStopWalkingTimePointLog(){
		
		if (timePointAtBusStop != 0.0)
			throw new IllegalStateException("time point arrived at bus stop was not zero");
		
		timePointAtBusStop = getModel().getSimulationControl().getCurrentSimulationTime();
	}
	
	public void calculateWaitedTime(){
		timeWaitedAtBusStop = Duration.seconds(timeWaitedAtBusStop.toSeconds().value() + Duration.seconds(getModel().getSimulationControl().getCurrentSimulationTime() - timePointAtBusStop).value());
		
		timePointAtBusStop = 0.0;
		//Utils.log(this, "Caluclated New Waitingtime: " + timeWaitedAtBusStop.toSeconds().value() );
	}
	
	public void humanIsCollected(){
		if (timePointCollected != 0.0)
			throw new IllegalStateException("time point arrived at bus stop was not zero, was:" + timePointCollected);
		
		timePointCollected = this.getModel().getSimulationControl().getCurrentSimulationTime();
		
		//System.out.println("Human" + this.getName() + "collected at" + timePointCollected);
	}
	
	public void calculateDrivingTime(){
		timeDriven = Duration.seconds(timeDriven.toSeconds().value() + Duration.seconds(getModel().getSimulationControl().getCurrentSimulationTime() - timePointCollected).value());
		//System.out.println("New Time Driven" + timeDriven.value());
		timePointCollected = 0.0;
		//System.out.println("Human" + getName() + "New Time Driven" + timeDrivenEvent.toSeconds().value() + " at time " + getModel().getSimulationControl().getCurrentSimulationTime());
		//Utils.log(this, "Caluclated New Drivingtime: " + timeDrivenEvent.toSeconds().value() );
	}
	
	public void calculateFreeTime(){
		Duration onTheWay = Duration.seconds(0);
		if(behaviour.equals(HumanBehaviour.WALKING_DIRECTLY)){
			onTheWay = Duration.seconds(WORKTIME.toSeconds().value() + 2*WALK_DIRECTLY.toSeconds().value()); 
		} else if (behaviour.equals(HumanBehaviour.DRIVING_BY_BUS)){
			onTheWay = Duration.seconds(WORKTIME.toSeconds().value() + 2*HOME_TO_STATION.toSeconds().value() + 2* WORK_TO_STATION.toSeconds().value() + timeDriven.toSeconds().value() + timeWaitedAtBusStop.value());
		}


		
		//Utils.log(this, "On the way:" + onTheWay.toHours().value() + " hours. Waited " + timeWaitedAtBusStop.toMinutes().value() + " minutes at bus stops");

		awayFromHomeTimes.add(onTheWay);

		busWaitingTimes.add(timeWaitedAtBusStop);
		drivingTimes.add(timeDriven);
		double total= 24 - onTheWay.toHours().value();
		FREETIME = Duration.hours(total);
//		Utils.log(this, "Enjoys: " + FREETIME.toHours().value() + " of Freetime");
		freeTimes.add(FREETIME.toHours());
		this.timeDriven = Duration.seconds(0);
		timePointAtBusStop = 0;
		timeWaitedAtBusStop = Duration.seconds(0);
	}

		public Position nextPosition() {
		
		this.position = this.destination;
		this.positionIndex += direction;
		
		if(positionIndex + direction >= workway.size()){
			direction = (-1);
		} else if (positionIndex + direction < 0) {
			direction = 1;
		}
		
		this.destination = this.workway.get(positionIndex + direction);
		
		
		
		return this.position;
		
	}

}