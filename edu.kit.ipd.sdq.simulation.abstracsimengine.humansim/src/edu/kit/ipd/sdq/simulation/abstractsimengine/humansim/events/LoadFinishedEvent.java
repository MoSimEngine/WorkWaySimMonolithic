package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.events;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class LoadFinishedEvent extends AbstractSimEventDelegator<Bus> {

    private int remainingPassengers;

    private double loadingTime;

    public LoadFinishedEvent(double loadingTime, int remainingPassengers, ISimulationModel model, String name) {
        super(model, name);
        this.loadingTime = loadingTime;
        this.remainingPassengers = remainingPassengers;
    }

    @Override
    public void eventRoutine(Bus bus) {
    	if(loadingTime > 0.0){
//        Utils.log(bus, "Loading finished. Took " + loadingTime + " seconds.");
    	}
        if (remainingPassengers > 0) {
//            Utils.log(bus, "Bus is full. Remaining passengers at bus station: "
//                    + bus.getPosition().getWaitingPassengers());
        }
      
       
        TravelEvent e = new TravelEvent(this.getModel(), "Travel");
        e.schedule(bus, 0);
    }

}
