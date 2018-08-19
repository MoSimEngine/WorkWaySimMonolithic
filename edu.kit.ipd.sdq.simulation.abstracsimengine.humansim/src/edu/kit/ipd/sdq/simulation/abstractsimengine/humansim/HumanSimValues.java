package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim;

public class HumanSimValues {
	
	public final static int NUM_BUSSTOPS = 3;
	public final static int NUM_HUMANS = 100;
	public final static Duration MAX_SIM_TIME = Duration.hours(24);
	public final static Duration BUSY_WAITING_TIME_STEP = Duration.seconds(5);
	public final static boolean USE_SPIN_WAIT = true;
	public final static boolean PROCESS_ORIENTED = false;
	public static final boolean WALKING_ENABLED = false;
	public static final boolean RANDOMIZED_HUMAN_VALUES = true;
	public static final boolean RANDOMIZED_HUMAN_STATIONS = true;
	
}
