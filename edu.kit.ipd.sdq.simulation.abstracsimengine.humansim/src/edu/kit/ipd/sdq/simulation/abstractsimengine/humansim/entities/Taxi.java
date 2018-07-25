package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEntityDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;

public class Taxi extends AbstractSimEntityDelegator{

	
	public enum TaxiState{
		PICKING_UP_PASSANGER,
		DRIVING_PASSANGER,
		DROPPING_OFF_PASSANGER,
		EMPTY,
		DRIVING_TO_PASSENGER
	}
	
	
	public static final Duration DRVING_DURATION = Duration.minutes(new Random().nextInt(80) + 1);
	public static final Duration DROPOFF_DURATION = Duration.seconds(8);
	public static final Duration PICKUP_DURATION = Duration.seconds(6);
	
	
	private Human transportedHuman;
	private TaxiState state;
	private ConcurrentLinkedQueue<BusStop> nextStops;
	
	public Taxi(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
		state = TaxiState.EMPTY;
		nextStops = new ConcurrentLinkedQueue<BusStop>();
		
	}
	
	public void registerTaxiCall(Human human, BusStop nextStop){
		nextStops.add(nextStop);
		human.registeredTaxi = this;
	}
	
	
	
	public void driveToPassenger(){
		if(state.equals(TaxiState.EMPTY)){
			state = TaxiState.PICKING_UP_PASSANGER;
		} else {
			new IllegalStateException("Taxi is working");
		}
	}
	
	public void pickUpPassenger(Human human){
		if(transportedHuman == null && state.equals(TaxiState.DRIVING_TO_PASSENGER)){
			state = TaxiState.PICKING_UP_PASSANGER;
		} else {
			new IllegalStateException("Taxi already full!");
		}
	}
	
	public void dropOffPassenger(){
		if(transportedHuman != null && state.equals(TaxiState.DRIVING_PASSANGER)){
			state = TaxiState.DROPPING_OFF_PASSANGER;
		} else {
			new IllegalStateException("Taxi has no passenger to drop off");
		}
	}
	
	public void finishedDroppingOffPassenger(){
		if(state.equals(TaxiState.DROPPING_OFF_PASSANGER)){
			state = TaxiState.EMPTY;
			transportedHuman = null;
		} else {
			new IllegalStateException("Currently not dropping off");
		}
	}
	
	
	public Human getPassenger(){
		return transportedHuman;
	}
	
	public void setPassenger(Human human){
		transportedHuman = human;
	}
	
	
	

}
