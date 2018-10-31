package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Position.PositionType;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class TravelToNextEvent extends AbstractSimEventDelegator<Human>{

	public TravelToNextEvent(ISimulationModel model, String name) {
		super(model, name);

	}

	@Override
	public void eventRoutine(Human human) {

		PositionType posType = human.getPosition().getPositionType();
		PositionType destType = human.getDestination().getPositionType();
//		Utils.log(human, "In Travel Event: Pos:" + posType.toString() + " DestType: " + destType.toString());
		Duration travelTime;
		String eventName = "";
		
		switch (posType) {
		case BUSSTOP:
			
			switch (destType) {
			case HOME:
				travelTime = human.HOME_TO_STATION.toSeconds();
				eventName = "Walk from station to home";
				human.walkToNext();
				break;
			case WORK: 
				travelTime = human.WORK_TO_STATION.toSeconds();
				eventName = "Walk from station to work";
				human.walkToNext();
				break;
			default:
				throw new IllegalStateException("No way from busStop to destination");
			}
			
			break;
			
		case HOME:
			
			switch(destType) {
			case BUSSTOP:
				travelTime = human.HOME_TO_STATION.toSeconds();
				eventName = "Walk from home to station";
				human.walkToNext();
				break;
				
			case WORK:
				travelTime = human.WALK_DIRECTLY.toSeconds();
				eventName = "Walk from home directly to work";
				human.walkToNext();
				break;
			default:
				throw new IllegalStateException("No way from home to destination");
			}
		
			break;
		
		case WORK: 
			
			switch(destType) {
			case BUSSTOP:
				travelTime = human.HOME_TO_STATION.toSeconds();
				eventName = "Walk from home to station";
				human.walkToNext();
				break;
				
			case HOME:
				travelTime = human.WALK_DIRECTLY.toSeconds();
				eventName = "Walk from work directly to home";
				human.walkToNext();
				break;
			default:
				throw new IllegalStateException("No way from work to destination");
			}
			
			break;
			
		default:
			throw new IllegalStateException("No valid position  to travel to");
		}
		
		ArriveAtNextEvent e = new ArriveAtNextEvent(getModel(), eventName);
		e.schedule(human, travelTime.toSeconds().value());
		
		
	}

}
