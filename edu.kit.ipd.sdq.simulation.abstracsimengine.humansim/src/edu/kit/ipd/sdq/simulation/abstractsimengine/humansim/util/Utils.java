package edu.kit.ipd.sdq.simulation.abstractsimengine.humansim.util;

import org.apache.log4j.Logger;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEntityDelegator;

public class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class);

    public static void log(AbstractSimEntityDelegator entity, String msg, boolean cmd) {
        StringBuilder s = new StringBuilder();
        s.append("[" + entity.getName() + "] ");
        s.append("(tSim=" + entity.getModel().getSimulationControl().getCurrentSimulationTime() + ")");
        s.append(msg);
        if(cmd)
        	System.out.println(s.toString());
        else 
        	LOGGER.info(s.toString());
    }
    

    
    public static void log(AbstractSimEntityDelegator entity, String msg) {
    	Utils.log(entity, msg, true);
    }
}
