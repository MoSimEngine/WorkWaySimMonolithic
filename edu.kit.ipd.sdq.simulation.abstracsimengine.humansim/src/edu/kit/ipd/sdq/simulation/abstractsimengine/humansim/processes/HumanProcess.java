package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.processes;

import java.io.Console;
import java.util.Random;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimProcessDelegator;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Duration;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.Route.RouteSegment;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Bus;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.BusStop;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.entities.Human;
import edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util.Utils;

public class HumanProcess extends AbstractSimProcessDelegator {

    private Human human;
    private Bus bus;

    public HumanProcess(Human human, Bus bus) {
        super(human.getModel(), human.getName());
        this.human = human;
        this.bus = bus;
    }

    public HumanProcess(Human human) {
        super(human.getModel(), human.getName());
        this.human = human;
    }
    
    @Override
    public void lifeCycle() {
        // goto Wörk ;)
        while (getModel().getSimulationControl().isRunning()) {
        	if(human.getWillWalk()){
        		walkToWorkDirectly();
        		work();
        		walkHomeDirectly();
        		live();
        	} else {
        	walkToBusStopAtHome();
        	waitForBus();
        	driveToBusStopAtWork();
        	walkFromBusStopToWork();
        	work();
        	walkToBusStopFromWork();
        	waitForBus();
        	driveToBusStopAtHome();
        	walkFromBusStopHome();
        	live();
        	}
        }
    }

    
    

	// Lifecycle with bus
    private void walkToBusStopAtHome() {
		human.walkToBusStopAtHome();
		Utils.log(human, human.getName() + " walks to home busstop. I don't like workdays ...");
		double walkToBusStopHomeDuration = human.HOME_TO_STATION.toSeconds().value();
		passivate(walkToBusStopHomeDuration);
		Utils.log(human, human.getName() + " arrives at home. Afterwork Party!");
		human.arriveAtBusStopHome();
	}
    
    
	private void waitForBus() {
		Utils.log(human, human.getName() + " waits for bus. Why does it take so long?!");
		// TODO implement waiting until bus arrives at busstop - i hope simulation engine is parallel xD
//		while(!bus.getPosition().equals(human.getPosition())){
//			
//		}
		
		passivate(human.WAITING_TIME_BUSSTOP.toSeconds().value());
	}
    
	private void driveToBusStopAtWork() {
		human.driveToBusStopAtWork();
		Utils.log(human, human.getName() + " sits in bus on the way to work");
//		while(!bus.getPosition().equals(human.getHomeBusStop())){
//			
//		}
		
		double driveDuration =  human.BUS_DRIVING_TIME.toSeconds().value();
		passivate(driveDuration);
		human.arriveAtBusStopWorkByDriving();
		Utils.log(human, human.getName() + "arrived at BusStop at work - by bus");

	}
	
	private void walkFromBusStopToWork() {
		human.walkToWorkFromBusStop();
		Utils.log(human, human.getName() + " is walking to work.");
		double walkingToBusStopHome = human.HOME_TO_STATION.toSeconds().value();
		passivate(walkingToBusStopHome);
		human.arriveAtWork();
		Utils.log(human, human.getName() + " starts to work.");
	}
	
	
	private void walkToBusStopFromWork() {
		human.walkToBusStopAtWork();
		Utils.log(human, human.getName() + " is walking from work to busstop");
		double walkingToBusStopWork = human.WORK_TO_STATION.toSeconds().value();
		passivate(walkingToBusStopWork);
		human.arriveAtBusStopWork();
		Utils.log(human, human.getName() + "is at bus stop and halfway home!");
	}

	

	private void driveToBusStopAtHome() {
		human.driveToBusStopAtHome();
		Utils.log(human, human.getName() + "Is driving home for... free time - by bus");
//		while(!bus.getPosition().equals(human.getHomeBusStop())){
//			
//		}
		double driveDuration =  human.BUS_DRIVING_TIME.toSeconds().value();
		passivate(driveDuration);
		human.arriveAtBusStopHomeByDriving();
		Utils.log(human, human.getName() + "arrived at BusStop at home - by bus");
	}

	
	private void walkFromBusStopHome() {
		human.walkHomeFromBusStop();
		Utils.log(human, human.getName() + " walks home. Only a few steps now!");
		double walkHomeDuration = human.HOME_TO_STATION.toSeconds().value();
		passivate(walkHomeDuration);
		human.arriveHome();
		Utils.log(human, human.getName() + " arrives at home. Afterwork Party!");
		
	}
	
	
	//Lifecycle with walking
	
	private void walkToWorkDirectly() {
		human.walkToWork();
		Utils.log(human, human.getName() + " walking to work. Wow, its a long way.");
		
		double walkingTime = human.WALKING_DURATION_WITHOUT_BUS.toSeconds().value();
		passivate(walkingTime);
		Utils.log(human, human.getName() + " walked to work. That was a long walk!");
	}

	private void walkHomeDirectly() {
		human.walkHome();
		Utils.log(human, human.getName() + " walking home. Dubidu he is walking");
		double walkingTime = human.WALKING_DURATION_WITHOUT_BUS.value();
		passivate(walkingTime);
		Utils.log(human, human.getName() + " walked home. That was a long walk!");
	}
	
	//General living and Working
	private void live() {
		Utils.log(human, human.getName() + " lives his life. Black Jack and Hookers baby.");
		double livingHisLife = human.FREETIME.toSeconds().value();
		System.out.println(human.FREETIME.toHours().value());
		passivate(livingHisLife);
		Utils.log(human, "Oh boy, time flies by... " + human.getName() + " stops living his life.");
	}
	
	private void work() {
		Utils.log(human, "Finally its over..." + human.getName() + " works and works.");
		double working = human.WORKTIME.toSeconds().value();
		passivate(working);
		Utils.log(human, "Finally its over..." + human.getName() + " stops working.");
	}
}