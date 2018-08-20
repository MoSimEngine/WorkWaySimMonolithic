package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;

public class registerEvent extends AbstractSimEventDelegator<Human>{

	BusStop bs;
	
	protected registerEvent(ISimulationModel model, String name, BusStop b) {
		super(model, name);
		// TODO Auto-generated constructor stub
		this.bs = b;
	}

	@Override
	public void eventRoutine(Human human) {
		// TODO Auto-generated method stub
		bs.setHuman(human);
	}

}
