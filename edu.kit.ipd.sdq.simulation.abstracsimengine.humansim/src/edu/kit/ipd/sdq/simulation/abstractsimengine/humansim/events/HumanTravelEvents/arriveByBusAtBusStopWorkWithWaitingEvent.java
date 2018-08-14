package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class arriveByBusAtBusStopWorkWithWaitingEvent extends AbstractSimEventDelegator<Human>{

	protected arriveByBusAtBusStopWorkWithWaitingEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		// TODO Auto-generated method stub
		human.arriveAtBusStopWorkByDriving();
		Utils.log(human, human.getName() + "arrived at BusStop at Work - by bus");
		
		HumanWalkFromBusStopToWorkEvent e = new HumanWalkFromBusStopToWorkEvent(getModel(),"WalkFromBusStopWorkToWork");
		e.schedule(human, 0);
	}

}
