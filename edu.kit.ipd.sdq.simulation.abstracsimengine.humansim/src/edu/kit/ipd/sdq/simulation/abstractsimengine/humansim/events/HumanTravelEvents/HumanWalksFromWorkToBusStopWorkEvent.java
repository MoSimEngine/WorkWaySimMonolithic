package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanWalksFromWorkToBusStopWorkEvent extends AbstractSimEventDelegator<Human>{

	protected HumanWalksFromWorkToBusStopWorkEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		human.walkToBusStopAtWork();
		
//		Utils.log(human, human.getName() + " walks to work busstop:" + human.getWorkBusStop().getName() + ".  Oh happy day!");

		double walkingToBusStopWork = human.WORK_TO_STATION.toSeconds().value();
		
		HumanArriveAtBusStopAtWorkEvent e = new HumanArriveAtBusStopAtWorkEvent(this.getModel(), "Arrives at busstop");
		e.schedule(human, walkingToBusStopWork);
	}

}
