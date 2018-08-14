package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.HumanSimValues;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class WaitForBusAtWorkEvent extends AbstractSimEventDelegator<Human>{

	protected WaitForBusAtWorkEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		// TODO Auto-generated method stub
		
		
		
		
		if(!human.isCollected()){
			WaitForBusAtWorkEvent e = new WaitForBusAtWorkEvent(getModel(), "ReschedulingWaitForBusAtWork");
			e.schedule(human, HumanSimValues.BUSY_WAITING_TIME_STEP.toSeconds().value());
			return;
		} 
		
		human.calculateWaitedTime();
		Utils.log(human, human.getName() + " entered bus at " + human.getWorkBusStop().getName() );
		human.driveToBusStopAtHome();
		human.humanIsCollected();
		DrivingBusHomeEvent e = new DrivingBusHomeEvent(getModel(), "DrivingBusHome");
		e.schedule(human, 0);
		
	}
	
	

}
