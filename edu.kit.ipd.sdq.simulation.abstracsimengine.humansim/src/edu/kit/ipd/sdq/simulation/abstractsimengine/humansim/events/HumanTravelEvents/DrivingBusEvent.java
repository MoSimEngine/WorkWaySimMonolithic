package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class DrivingBusEvent extends AbstractSimEventDelegator<Human>{

	protected DrivingBusEvent(ISimulationModel model, String name) {
		super(model, name);
	}

	@Override
	public void eventRoutine(Human human) {

		if(human.isCollected()){
			
			DrivingBusEvent e = new DrivingBusEvent(getModel(), "reschedulingDrivingBusHome");
			e.schedule(human, HumanSimValues.BUSY_WAITING_TIME_STEP.toSeconds().value());
			return;
		}
		Utils.log(human, human.getName() + " left bus at " + human.getPosition().getName() );
		human.calculateDrivingTime();
		
		ArriveAtNextEvent e = new ArriveAtNextEvent(getModel(), "ArriveAtHomeByBusWaiting");
		e.schedule(human, 0);
	}

}
