package it.polimi.ingsw.server.model.personalboardpackage;

import it.polimi.ingsw.server.model.ProductionOutput;
import it.polimi.ingsw.server.model.ResourceBank;
import it.polimi.ingsw.server.model.Resource;

import java.util.Map;


public class DefaultSlot {

    private DefaultSlot(){}

    /**
     * Returns a ProductionOutput object which contains a single element resource map
     * @param output defines which product gets produced
     */
    public static ProductionOutput produce(Map<Resource, Integer> output) {
        return new ProductionOutput(ResourceBank.getResources(output));
    }
}