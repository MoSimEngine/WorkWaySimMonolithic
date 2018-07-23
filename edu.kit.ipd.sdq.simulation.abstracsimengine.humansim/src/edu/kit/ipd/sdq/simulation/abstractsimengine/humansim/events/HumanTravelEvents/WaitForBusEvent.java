package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class WaitForBusEvent extends AbstractSimEventDelegator<Human> {

	protected WaitForBusEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		Utils.log(human, human.getName() + " waits at busstop:" + human.getHomeBusStop().getName());
		
		if(!human.isCollected()){
			WaitForBusEvent event = new WaitForBusEvent(this.getModel(), "Wait for bus event");
			event.schedule(human, 1);
		} 
		
		DriveToBusStopAtWorkEvent e = new DriveToBusStopAtWorkEvent(this.getModel(), "Drive to BusStop at Work");
		
		e.schedule(human, 0);
		
		
		
	}

}
