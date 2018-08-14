package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class WaitForBusAtHomeEvent extends AbstractSimEventDelegator<Human> {

	protected WaitForBusAtHomeEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		
	
		if(!human.isCollected()){
			WaitForBusAtHomeEvent e = new WaitForBusAtHomeEvent(getModel(), "redscheduled wait for bus at home event");
			e.schedule(human, HumanSimValues.BUSY_WAITING_TIME_STEP.toSeconds().value());
			return;
		}
		
		human.calculateWaitedTime();
		
		
		Utils.log(human, human.getName() + " entered bus at " + human.getHomeBusStop().getName() );
		human.driveToBusStopAtWork();
		human.humanIsCollected();
		DrivingBusToWorkEvent e = new DrivingBusToWorkEvent(getModel(), "DrivingBusToWorkEvent");
		e.schedule(human, 0);
		
	}

}
