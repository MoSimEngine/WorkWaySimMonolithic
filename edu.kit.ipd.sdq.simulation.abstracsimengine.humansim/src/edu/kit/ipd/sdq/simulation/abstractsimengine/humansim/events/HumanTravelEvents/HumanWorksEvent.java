package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanWorksEvent extends AbstractSimEventDelegator<Human>{

	protected HumanWorksEvent(ISimulationModel model, String name) {
		super(model, name);

	}

	@Override
	public void eventRoutine(Human human) {
//		Utils.log(human, human.getName() + " works and works.");
		double working = human.WORKTIME.toSeconds().value();
		HumanEndsWorkingEvent e = new HumanEndsWorkingEvent(this.getModel(), "Human stops working");
		e.schedule(human, working);
	}

}
