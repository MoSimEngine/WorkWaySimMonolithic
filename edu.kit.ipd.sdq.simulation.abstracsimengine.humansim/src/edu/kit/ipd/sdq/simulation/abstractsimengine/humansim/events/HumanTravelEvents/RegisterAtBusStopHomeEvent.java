package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class RegisterAtBusStopHomeEvent extends AbstractSimEventDelegator<Human>{

	protected RegisterAtBusStopHomeEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		
		human.getHomeBusStop().setHuman(human);
		human.setDestination(human.getWorkBusStop());
		human.arriveAtBusStopWalkingTimePointLog();
		
		Utils.log(human, "Registers at bus Stop:" + human.getHomeBusStop().getName());
		
		if(HumanSimValues.USE_SPIN_WAIT){
		WaitForBusAtHomeEvent e = new WaitForBusAtHomeEvent(this.getModel(), "Waiting for bus at home event");
		e.schedule(human, 0);
		return;
		}
		
	}

}
