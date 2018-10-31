package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class WaitForBusEvent extends AbstractSimEventDelegator<Human>{

	protected WaitForBusEvent(ISimulationModel model, String name) {
		super(model, name);

	}

	@Override
	public void eventRoutine(Human human) {

		if(!human.isCollected()){
			WaitForBusEvent e = new WaitForBusEvent(getModel(), "redscheduled wait for bus event");
			e.schedule(human, HumanSimValues.BUSY_WAITING_TIME_STEP.toSeconds().value());
			return;
		}
		
		human.calculateWaitedTime();
		Utils.log(human, human.getName() + " entered bus at " + human.getPosition().getName());
		human.travellingToNext();
		human.humanIsCollected();
		DrivingBusEvent e = new DrivingBusEvent(getModel(), "DrivingBusToWorkEvent");
		e.schedule(human, 0);
	}

}
