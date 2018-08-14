package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;

public class RegisterAtBusStopWorkEvent extends AbstractSimEventDelegator<Human>{

	protected RegisterAtBusStopWorkEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		
		human.getWorkBusStop().setHuman(human);
		human.setDestination(human.getHomeBusStop());
		human.arriveAtBusStopWalkingTimePointLog();
		
		if(HumanSimValues.USE_SPIN_WAIT){
		WaitForBusAtWorkEvent e = new WaitForBusAtWorkEvent(getModel(), "WaitForBusStopWork");
		e.schedule(human, 0);
		return;
		}
		
	}

}
