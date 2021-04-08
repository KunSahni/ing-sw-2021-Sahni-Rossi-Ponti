package it.polimi.ingsw.server.model.personalboardpackage;

import java.util.*;
import it.polimi.ingsw.server.model.ResourceBank;
import it.polimi.ingsw.server.model.Resource;


public class DefaultSlot {

    private DefaultSlot(){}

    /**
     * @param output will be requested to the bank and returned
    */
    public static Map<Resource, Integer> produce(Resource output) {
        return ResourceBank.getResources(output);
    }
}