package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class DrivingBusToWorkEvent extends AbstractSimEventDelegator<Human>{

	protected DrivingBusToWorkEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		// TODO Auto-generated method stub
		
		
		
		if(human.isCollected()){
			DrivingBusToWorkEvent e = new DrivingBusToWorkEvent(getModel(), "RescheduledDrivingToBusWorkEvent");
			e.schedule(human, HumanSimValues.BUSY_WAITING_TIME_STEP.toSeconds().value());
			return;
		}
		
		Utils.log(human, human.getName() + " left bus at " + human.getWorkBusStop().getName() );
		human.calculateDrivingTime();
		
		arriveByBusAtBusStopWorkWithWaitingEvent e = new arriveByBusAtBusStopWorkWithWaitingEvent(getModel(), "ArriveAtBusStopWorkByBus");
		e.schedule(human, 0);
		
	}

}
