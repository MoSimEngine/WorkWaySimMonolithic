package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanWalkFromBusStopToWorkEvent extends AbstractSimEventDelegator<Human>{

	public HumanWalkFromBusStopToWorkEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		// TODO Auto-generated method stub
		human.walkToWorkFromBusStop();
		Utils.log(human, human.getName() + " is walking to work.");
		double walkingToWork = human.WORK_TO_STATION.toSeconds().value();
		HumanArrivesAtWorkEvent e = new HumanArrivesAtWorkEvent(this.getModel(), "Human arrives at work");
		e.schedule(human, walkingToWork);
	}

}
