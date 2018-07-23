package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class DriveToBusAtWorkEvent extends AbstractSimEventDelegator<Human>{

	protected DriveToBusAtWorkEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
		human.driveToBusStopAtWork();
		Utils.log(human, human.getName() + " sits in bus on the way to work");
		
		if(human.isCollected()){
			DriveToBusAtWorkEvent e = new DriveToBusAtWorkEvent(this.getModel(), "Drive to BusStop at work");
		}
		
	}

}
