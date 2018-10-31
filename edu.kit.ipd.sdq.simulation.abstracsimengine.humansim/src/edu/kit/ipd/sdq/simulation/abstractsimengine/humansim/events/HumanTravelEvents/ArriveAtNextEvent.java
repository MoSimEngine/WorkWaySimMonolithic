package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Position;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Position.PositionType;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;


public class ArriveAtNextEvent extends AbstractSimEventDelegator<Human>{

	protected ArriveAtNextEvent(ISimulationModel model, String name) {
		super(model, name);

	}

	@Override
	public void eventRoutine(Human human) {

		
		human.nextPosition();
		PositionType posType = human.getPosition().getPositionType();
		PositionType destType = human.getDestination().getPositionType();
		
		switch (posType) {
		case BUSSTOP:
			human.arriveAtBusStop();
			
			destType = human.getDestination().getPositionType();
			if(destType.equals(PositionType.BUSSTOP)) {
				RegisterAtBusStopEvent e = new RegisterAtBusStopEvent(getModel(), "Register at BusStop");
				e.schedule(human, 0);
				break;
			} else {
				TravelToNextEvent e = new TravelToNextEvent(getModel(), "Travel to next position");
				e.schedule(human, 0);
				break;
			}
			
		case HOME:
			Utils.log(human, human.getName() + " arrives at home. Afterwork Party!");
			human.arriveAtHome();
			destType = human.getDestination().getPositionType();
			HumanLivingHisLifeEvent livingEvent = new HumanLivingHisLifeEvent(human.getModel(), "Human is living his life");
			livingEvent.schedule(human, 0);
			break;
			
		case WORK:
			human.arriveAtWork();
			destType = human.getDestination().getPositionType();
			Utils.log(human, human.getName() + " arrives at work.");
			HumanWorksEvent workingEvent = new HumanWorksEvent(this.getModel(), "Human Works");
			workingEvent.schedule(human, 0);
			break;

		default:
			Utils.log(human, "Arrived at no known position");
			break;
		}
		
	
	}
	
}

