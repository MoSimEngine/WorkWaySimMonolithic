package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimulationModel;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimEngineFactory;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationConfig;
import de.uka.ipd.sdq.simulation.preferences.SimulationPreferencesHelper;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.LoadPassengersEvent;

import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents.TravelToNextEvent;

import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.CSVHandler;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanModel extends AbstractSimulationModel {

	private double startTime;


	private LinkedList<Human> humans;
	private LinkedList<Bus> buses;
	private LinkedList<BusStop> stops;

	public HumanModel(ISimulationConfig config, ISimEngineFactory factory) {
		super(config, factory);
		humans = new LinkedList<Human>();
		buses = new LinkedList<Bus>();
		stops = new LinkedList<BusStop>(); 
	}

	public void init() {

		startTime = System.nanoTime();

		// define bus stops
		for (int i = 1; i <= HumanSimValues.NUM_BUSSTOPS ; i++) {
			stops.add(new BusStop(this, "Stop" + i));
		}

		// define route
        Route lineOne = new Route();
        lineOne.addSegment(stops.get(0), stops.get(1), 20, 50);
        lineOne.addSegment(stops.get(1), stops.get(2), 40, 50);
        lineOne.addSegment(stops.get(2), stops.get(0), 30, 50);
        
        Route lineTwo = new Route();
        lineTwo.addSegment(stops.get(3), stops.get(4), 20, 50);
        lineTwo.addSegment(stops.get(4), stops.get(5), 30, 50);
        lineTwo.addSegment(stops.get(5), stops.get(3), 40, 50);

	
        //define busses
        buses.add(new Bus(40, stops.get(0), lineOne, this, "Bus1"));
        buses.add(new Bus(40, stops.get(1), lineOne, this, "Bus2"));
        buses.add(new Bus(40, stops.get(2), lineOne, this, "Bus3"));
        buses.add(new Bus(40, stops.get(0), lineOne, this, "Bus4"));
     
        buses.add(new Bus(20, stops.get(3), lineTwo, this, "Bus5"));
        buses.add(new Bus(20, stops.get(4), lineTwo, this, "Bus6"));
        buses.add(new Bus(20, stops.get(5), lineTwo, this, "Bus7"));
        buses.add(new Bus(20, stops.get(4), lineTwo, this, "Bus8"));
        
        
//		for (Bus bus : buses) {
//			// schedule intitial event for the bus
//			new LoadPassengersEvent(this, "Load Passengers").schedule(bus, 2.0);
//		}
        
        for (int i = 0; i < buses.size(); i++) {
			double timestep = 2.0;
			if(i == 3 || i == 7) {
				timestep += Duration.minutes(10).toSeconds().value();
			}
			
			new LoadPassengersEvent(this, "Load Passengers").schedule(buses.get(i), timestep);
		}

		// schedule a process for each human
		for (int i = 0; i < HumanSimValues.NUM_HUMANS; i++) {
			
			int homeBS = 0;
			int workBS = 0;

			if (HumanSimValues.RANDOMIZED_HUMAN_STATIONS) {
				while (homeBS == workBS) {
					homeBS = new Random().nextInt(HumanSimValues.NUM_BUSSTOPS);
					workBS = new Random().nextInt(HumanSimValues.NUM_BUSSTOPS);
				}
			} else {
				int route = i % 2;

				homeBS = route * 3;
				workBS = (route * 3) + 1;
			}
			Human hu = new Human(stops.get(homeBS), stops.get(workBS), this, "Hugo" + i);
			humans.add(hu);

			new TravelToNextEvent(this, hu.getName() + "walks to bus station").schedule(hu, 2.0);
		}

	}

	public void finalise() {

		Double finalTime = (System.nanoTime() - startTime) / Math.pow(10, 9);
		String file_header = "";
		String csvAway = "";
		String csvWaitingAtStation = "";
		String csvDrivingTimes = "";
		String behaviourMarker = "";
		String csvFreeTimes = "";

		int getMaxNumValues = 0;

		for (Human human : humans) {

			file_header += human.getName() + CSVHandler.CSV_DELIMITER;
			behaviourMarker += human.getBehaviour().toString() + CSVHandler.CSV_DELIMITER;
			System.out.println("Human " + human.getName() + " is in State " + human.getState() + " and is "
					+ human.getBehaviour().toString());
			if (getMaxNumValues < human.getAwayFromHomeTimes().size()) {
				getMaxNumValues = human.getAwayFromHomeTimes().size();
			}
		}

		for (BusStop stop : stops) {
			System.out.println("Waiting passengers at " + stop.getName() + ":" + stop.getPassengersInQueue());
		}

		file_header += CSVHandler.NEWLINE;
		behaviourMarker += CSVHandler.NEWLINE;

		for (int i = 0; i < getMaxNumValues; i++) {

			for (int j = 0; j < humans.size(); j++) {

				Human human = humans.get(j);
				ArrayList<Duration> away = human.getAwayFromHomeTimes();
				ArrayList<Duration> driven = human.getDrivingTimes();
				ArrayList<Duration> waited = human.getBusWaitingTimes();
				ArrayList<Duration> free = human.getFreeTimes();

				if (away.size() > i) {
					csvAway += away.get(i).toSeconds().value();
					csvDrivingTimes += driven.get(i).toSeconds().value();
					csvWaitingAtStation += waited.get(i).toSeconds().value();
					csvFreeTimes += free.get(i).toSeconds().value();
				} else {
					csvAway += "-1";
					csvDrivingTimes += "-1";
					csvWaitingAtStation += "-1";
					csvFreeTimes += "-1";
				}

				if (j < humans.size() - 1) {
					csvAway += CSVHandler.CSV_DELIMITER;
					csvDrivingTimes += CSVHandler.CSV_DELIMITER;
					csvWaitingAtStation += CSVHandler.CSV_DELIMITER;
					csvFreeTimes += CSVHandler.CSV_DELIMITER;
				}

			}

			csvAway += CSVHandler.NEWLINE;
			csvDrivingTimes += CSVHandler.NEWLINE;
			csvWaitingAtStation += CSVHandler.NEWLINE;
			csvFreeTimes += CSVHandler.NEWLINE;
		}


		String[] csvs = { csvAway, csvDrivingTimes, csvWaitingAtStation, csvFreeTimes };

		for (int i = 0; i < csvs.length; i++) {
			String s = "";

			switch (i) {
			case 0:
				s = "AwayTimes";
				break;
			case 1:
				s = "DrivingTimes";
				break;
			case 2:
				s = "BusStationWaitingTimes";
				break;
			case 3:
				s = "FreeTimes";
				break;

			default:
				throw new IllegalStateException("More than three files");
			}

			csvs[i] = csvs[i].replace('.', ',');

			CSVHandler.writeCSVFile(s, file_header + csvs[i]);

		}
		CSVHandler.writeCSVFile("HumanBehaviour", behaviourMarker);

		Double d = Math.round(finalTime * 100.00) / 100.00;
		String s = d.toString();

		s = s.replace('.', ',');

		CSVHandler.readCSVAndAppend("ExecutionTimes", s + CSVHandler.CSV_DELIMITER);

	}

	/**
	 * Creates the simulation model for the specified configuration.
	 * 
	 * @param config the simulation configuration
	 * @return the created simulation model
	 */
	public static HumanModel create(final HumanSimConfig config) {
		// load factory for the preferred simulation engine
		ISimEngineFactory factory = SimulationPreferencesHelper.getPreferredSimulationEngine();
		if (factory == null) {
			throw new RuntimeException("There is no simulation engine available. Install at least one engine.");
		}

		// create and return simulation model
		final HumanModel model = new HumanModel(config, factory);

		return model;
	}
}
