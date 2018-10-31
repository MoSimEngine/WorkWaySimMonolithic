package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanStopsLivingHisLifeEvent extends AbstractSimEventDelegator<Human>{

	protected HumanStopsLivingHisLifeEvent(ISimulationModel model, String name) {
		super(model, name);

	}

	@Override
	public void eventRoutine(Human human) {

//		Utils.log(human, "Oh boy, time flies by... " + human.getName() + " stops living his life.");
		
		TravelToNextEvent e = new TravelToNextEvent(human.getModel(), "Human walks to bus stop at home");
		e.schedule(human, 0);
		
	}

}
