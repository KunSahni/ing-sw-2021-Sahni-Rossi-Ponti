package it.polimi.ingsw.server.model.personalboard;

import it.polimi.ingsw.server.model.utils.ProductionOutput;
import it.polimi.ingsw.server.model.utils.ResourceBank;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.Map;


public class DefaultSlot {

    private DefaultSlot(){}

    /**
     * Returns a ProductionOutput object which contains a single element resource map
     * @param output defines which product gets produced
     */
    public static ProductionOutput produce(Map<Resource, Integer> output) {
        return new ProductionOutput(0, ResourceBank.getResources(output));
    }
}