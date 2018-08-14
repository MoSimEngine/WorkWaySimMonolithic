package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;

public class HumanEntersBusEvent extends AbstractSimEventDelegator<Human>{

	public HumanEntersBusEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		// TODO Auto-generated method stub
		
		human.setCollected(true);
		human.calculateWaitedTime();
		human.humanIsCollected();
		if(human.getDestination().equals(human.getHomeBusStop())){
			human.driveToBusStopAtHome();
		} else if(human.getDestination().equals(human.getWorkBusStop())){
			human.driveToBusStopAtWork();
		} else {
			throw new IllegalStateException("Human is collected, but not at correct stop");
		}
		
	}

}
