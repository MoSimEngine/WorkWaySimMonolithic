package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.Taxi;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Taxi;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class TaxiDropOffEFinishedEvent extends AbstractSimEventDelegator<Taxi>{

	protected TaxiDropOffEFinishedEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Taxi taxi) {

		Utils.log(taxi, taxi.getName() + " dropped of passenger: " + taxi.getPassenger());
		
		taxi.getPassenger().setCollected(false);
		taxi.finishedDroppingOffPassenger();
		
		
	}

}
