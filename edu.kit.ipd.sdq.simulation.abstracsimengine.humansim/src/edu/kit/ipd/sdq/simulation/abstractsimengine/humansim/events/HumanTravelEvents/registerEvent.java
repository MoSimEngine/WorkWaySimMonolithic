package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;

public class registerEvent extends AbstractSimEventDelegator<Human>{


	protected registerEvent(ISimulationModel model, String name) {
		super(model, name);

	}

	@Override
	public void eventRoutine(Human human) {

		((BusStop)human.getPosition()).setHuman(human);
		
	}

}
