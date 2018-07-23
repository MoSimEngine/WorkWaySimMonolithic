package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanArrivesAtWorkEvent extends AbstractSimEventDelegator<Human>{

	protected HumanArrivesAtWorkEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		if(human.getWillWalk()){
			human.arriveAtWorkDirectlyWalking();
		} else {
			human.arriveAtWorkBus();
		}
	
		Utils.log(human, human.getName() + " starts to work.");
		HumanWorksEvent e = new HumanWorksEvent(this.getModel(), "Human Works");
		e.schedule(human, 0);
	}

}
