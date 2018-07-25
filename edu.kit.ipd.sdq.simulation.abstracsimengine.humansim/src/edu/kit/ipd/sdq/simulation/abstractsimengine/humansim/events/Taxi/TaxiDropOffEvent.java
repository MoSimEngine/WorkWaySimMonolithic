package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.Taxi;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Taxi;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class TaxiDropOffEvent extends AbstractSimEventDelegator<Taxi>{

	protected TaxiDropOffEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Taxi taxi) {
		
		double dropOffDuration = taxi.DROPOFF_DURATION.toSeconds().value();
		taxi.dropOffPassenger();
		Utils.log(taxi, taxi.getName() + " drops of passenger: " + taxi.getPassenger());
		
		
		
	}

}
