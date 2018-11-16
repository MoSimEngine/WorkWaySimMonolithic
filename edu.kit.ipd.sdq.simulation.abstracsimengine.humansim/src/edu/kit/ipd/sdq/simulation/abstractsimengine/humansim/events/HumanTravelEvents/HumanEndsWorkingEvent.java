package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanEndsWorkingEvent extends AbstractSimEventDelegator<Human>{

	protected HumanEndsWorkingEvent(ISimulationModel model, String name) {
		super(model, name);

	}

	@Override
	public void eventRoutine(Human human) {
		
		Utils.log(human, "Finally its over..." + human.getName() + " stops working.");
		
		TravelToNextEvent e = new TravelToNextEvent(getModel(), "Travel From Work");
		e.schedule(human, 0);
		
	}

}
