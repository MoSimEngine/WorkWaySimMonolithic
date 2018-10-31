package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanArrivesHomeEvent extends AbstractSimEventDelegator<Human>{

	protected HumanArrivesHomeEvent(ISimulationModel model, String name) {
		super(model, name);

	}

	@Override
	public void eventRoutine(Human human) {

		human.arriveAtWork();
//		Utils.log(human, human.getName() + " arrives at home. Afterwork Party!");
		HumanLivingHisLifeEvent e = new HumanLivingHisLifeEvent(human.getModel(), "Human is living his life");
		e.schedule(human, 0);
	}

}
