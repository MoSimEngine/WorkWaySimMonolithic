package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events.HumanTravelEvents;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanLivingHisLifeEvent extends AbstractSimEventDelegator<Human>{

	protected HumanLivingHisLifeEvent(ISimulationModel model, String name) {
		super(model, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventRoutine(Human human) {
//		Utils.log(human, human.getName() + " lives his life. Black Jack and Hookers baby.");
		human.calculateFreeTime();
		double livingHisLife = human.FREETIME.toSeconds().value();
		
		HumanStopsLivingHisLifeEvent e = new HumanStopsLivingHisLifeEvent(human.getModel(), "Human stops living his life");
		e.schedule(human, livingHisLife);
		
	}

}
