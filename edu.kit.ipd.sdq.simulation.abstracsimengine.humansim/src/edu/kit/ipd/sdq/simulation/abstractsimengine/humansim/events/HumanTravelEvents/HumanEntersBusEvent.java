package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanEntersBusEvent extends AbstractSimEventDelegator<Human>{

	public HumanEntersBusEvent(ISimulationModel model, String name) {
		super(model, name);
		
	}

	@Override
	public void eventRoutine(Human human) {

		human.setCollected(true);
		human.calculateWaitedTime();
		human.humanIsCollected();
		Utils.log(human, human.getName() + " enters bus at " + human.getPosition().getName() );
		human.travellingToNext();
		
		
	}

}
