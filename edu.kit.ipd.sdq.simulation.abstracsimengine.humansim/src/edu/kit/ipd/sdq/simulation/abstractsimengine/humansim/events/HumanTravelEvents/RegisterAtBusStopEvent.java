package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;


public class RegisterAtBusStopEvent extends AbstractSimEventDelegator<Human>{

	protected RegisterAtBusStopEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		
		
		
		registerEvent registerEvent = new registerEvent(getModel(), "Register");
		registerEvent.schedule(human, 1.0);
		human.arriveAtBusStopWalkingTimePointLog();
		
//		Utils.log(human, "Registers at bus Stop:" + human.getPosition().getName() + " with Destination" + human.getDestination().getName());
		
	
		
//			PickUpTimeoutEvent e = new PickUpTimeoutEvent(getModel(), "PickUpTimeoutAtBSH");
////			e.schedule(human, Duration.minutes(20).toSeconds().value());
//			m.getComponent().synchronisedAdvancedTime(Duration.minutes(20).toSeconds().value(), e, human);
		
	
		
		
	
		
	}

}
