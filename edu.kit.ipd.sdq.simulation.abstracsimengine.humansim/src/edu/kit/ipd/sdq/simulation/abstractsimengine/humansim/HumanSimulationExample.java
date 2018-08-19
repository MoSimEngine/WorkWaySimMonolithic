package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationControl;

public class HumanSimulationExample implements IApplication {

	private HumanSimConfig config;
	private HumanModel model;
	private ISimulationControl simControl;
	
	 private static final Duration MAX_SIMULATION_TIME = HumanSimValues.MAX_SIM_TIME;
	
	
	public HumanSimulationExample() {
		this.config = new HumanSimConfig();
		this.model = HumanModel.create(config);
		this.simControl = model.getSimulationControl();
		this.simControl.setMaxSimTime((long) MAX_SIMULATION_TIME.toSeconds().value());
		
	}
	public Object start(IApplicationContext context) throws Exception {
		
		BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);

        // run the simulation
        model.getSimulationControl().start();

        return EXIT_OK;
	}


	public void stop() {
		// nothing to do;
		
	}

}
