package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class ArriveByBusAtBusStopHomeWithWaitingEvent extends AbstractSimEventDelegator<Human>{

	protected ArriveByBusAtBusStopHomeWithWaitingEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		// TODO Auto-generated method stub
		Utils.log(human, human.getName() + "Arrived at Home BusStop at " + human.getHomeBusStop().getName() +" by bus");
		human.arriveAtBusStopHomeByDriving();
		HumanWalkFromBusStopHomeToHomeEvent e = new HumanWalkFromBusStopHomeToHomeEvent(getModel(), "WalkFromBusStopHomeToHome");
		e.schedule(human, 0);
		
	}

}
