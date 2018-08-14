package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanArriveByBustBusStopHomeEvent extends AbstractSimEventDelegator<Human>{

	public HumanArriveByBustBusStopHomeEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		// TODO Auto-generated method stub
		human.arriveAtBusStopHomeByDriving();
		human.setCollected(false);
		human.calculateDrivingTime();
		Utils.log(human, human.getName() + "arrived at BusStop at home - by bus");
		
		HumanWalkFromBusStopHomeToHomeEvent e = new HumanWalkFromBusStopHomeToHomeEvent(this.getModel(), "Human walks home");
		e.schedule(human, 0);
		
	}

}
