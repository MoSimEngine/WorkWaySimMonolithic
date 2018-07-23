package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class WalkToBusStopAtHomeEvent extends AbstractSimEventDelegator<Human>{

	public WalkToBusStopAtHomeEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		
		human.walkToBusStopAtHome();
		Utils.log(human, human.getName() + " walks to home busstop:" + human.getHomeBusStop().getName() + ".  I don't like workdays ...");
		double walkToBusStopHomeDuration = human.HOME_TO_STATION.toSeconds().value();
		ArriveAtBusStopAtHomeEvent e = new ArriveAtBusStopAtHomeEvent(this.getModel(), "Arrive at BusStop Home");
		e.schedule(human, walkToBusStopHomeDuration);
		
	}

}
