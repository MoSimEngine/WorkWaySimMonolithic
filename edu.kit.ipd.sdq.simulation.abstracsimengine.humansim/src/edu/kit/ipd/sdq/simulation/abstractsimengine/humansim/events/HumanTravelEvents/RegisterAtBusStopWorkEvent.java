package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class RegisterAtBusStopWorkEvent extends AbstractSimEventDelegator<Human>{

	protected RegisterAtBusStopWorkEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		
		registerEvent regE = new registerEvent(getModel(), "Registering", human.getWorkBusStop());
		changeDestinationEvent chaE = new changeDestinationEvent(getModel(), "ChangeDest", human.getHomeBusStop());
		
		regE.schedule(human, 1.0);
		chaE.schedule(human, 1.0);

		human.arriveAtBusStopWalkingTimePointLog();
//		Utils.log(human, "Registers at bus Stop:" + human.getWorkBusStop().getName());
		if(HumanSimValues.USE_SPIN_WAIT){
		WaitForBusAtWorkEvent e = new WaitForBusAtWorkEvent(getModel(), "WaitForBusStopWork");
		e.schedule(human, 1.0);
		return;
		}
		
	}

}
