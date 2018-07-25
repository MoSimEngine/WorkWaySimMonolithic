package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanEndsWorkingEvent extends AbstractSimEventDelegator<Human>{

	protected HumanEndsWorkingEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		
		Utils.log(human, "Finally its over..." + human.getName() + " stops working.");
		
		if(human.willWalk()){
			HumanWalksDirectlyHomeEvent e = new HumanWalksDirectlyHomeEvent(this.getModel(), "human walks home directly");
			e.schedule(human, 0);
		} else {
			HumanWalksFromWorkToBusStopWorkEvent e = new HumanWalksFromWorkToBusStopWorkEvent(this.getModel(), "human walks to bus stop work from work");
			e.schedule(human, 0);
		}
		
		
	}

}
