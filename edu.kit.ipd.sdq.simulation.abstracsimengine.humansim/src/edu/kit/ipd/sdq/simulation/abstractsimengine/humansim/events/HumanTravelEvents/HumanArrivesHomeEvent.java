package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanArrivesHomeEvent extends AbstractSimEventDelegator<Human>{

	protected HumanArrivesHomeEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		// TODO Auto-generated method stub
		if(human.willWalk()){
			human.arriveAtHomeDirectlyWalking();
		} else {
			human.arriveHomeBus();
		}
	
//		Utils.log(human, human.getName() + " arrives at home. Afterwork Party!");
		HumanLivingHisLifeEvent e = new HumanLivingHisLifeEvent(human.getModel(), "Human is living his life");
		e.schedule(human, 0);
	}

}
