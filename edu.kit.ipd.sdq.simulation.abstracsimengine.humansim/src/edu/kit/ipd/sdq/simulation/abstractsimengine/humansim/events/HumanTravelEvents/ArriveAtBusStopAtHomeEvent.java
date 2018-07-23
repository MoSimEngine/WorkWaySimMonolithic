package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class ArriveAtBusStopAtHomeEvent  extends AbstractSimEventDelegator<Human>{

	protected ArriveAtBusStopAtHomeEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		// TODO Auto-generated method stub
		
		human.arriveAtBusStopHome();
		Utils.log(human, human.getName() + " arrives at bus stop "+ human.getPosition().getName());
		BusStop position = human.getPosition();
		position.setPassenger(human);
		
		//WaitForBusEvent e = new WaitForBusEvent(this.getModel(), "WaitForBus");
		
		//e.schedule(human, 0);
		
		
	}

}
