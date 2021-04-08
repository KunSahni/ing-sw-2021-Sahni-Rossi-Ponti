package it.polimi.ingsw.server.model.personalboardpackage;

import java.util.*;

import it.polimi.ingsw.server.model.ProductionOutput;
import it.polimi.ingsw.server.model.ResourceBank;
import it.polimi.ingsw.server.model.Resource;


public class DefaultSlot {

    private DefaultSlot(){}

    /**
     * Returns a ProductionOutput object which contains a single element resource map
     * @param output defines which product gets produced
     */
    public static ProductionOutput produce(Resource output) {
        return new ProductionOutput(
                new HashMap<>(){{
                    put(output, 1);
                }}
        );
    }
}