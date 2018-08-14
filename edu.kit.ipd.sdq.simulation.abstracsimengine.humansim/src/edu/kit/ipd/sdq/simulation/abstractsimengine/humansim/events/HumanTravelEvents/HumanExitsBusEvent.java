package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;

public class HumanExitsBusEvent extends AbstractSimEventDelegator<Human>{

	public HumanExitsBusEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		// TODO Auto-generated method stub
		
		
		if(human.getDestination().equals(human.getHomeBusStop())){
			HumanArriveByBustBusStopHomeEvent e = new HumanArriveByBustBusStopHomeEvent(getModel(), "ArriveAtBSHomeByBus");
			e.schedule(human, 0);
		} else if (human.getDestination().equals(human.getWorkBusStop())){
			HumanArriveByBusAtBusStopWorkEvent e = new HumanArriveByBusAtBusStopWorkEvent(getModel(), "ArriveAtBSWorkbyBus");
			e.schedule(human, 0);
		}
		
	}

}
