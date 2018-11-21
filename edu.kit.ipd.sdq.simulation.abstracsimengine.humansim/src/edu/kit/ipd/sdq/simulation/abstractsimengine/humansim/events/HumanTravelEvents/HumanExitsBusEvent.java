package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanExitsBusEvent extends AbstractSimEventDelegator<Human>{

	public HumanExitsBusEvent(ISimulationModel model, String name) {
		super(model, name);
		
	}

	@Override
	public void eventRoutine(Human human) {

		
//		Utils.log(human, human.getName() + " left bus at " + human.getDestination().getName() );
		human.calculateDrivingTime();
		ArriveAtNextEvent e = new ArriveAtNextEvent(getModel(), "Arrive after driving");
		e.schedule(human, 0);
		
	}

}
