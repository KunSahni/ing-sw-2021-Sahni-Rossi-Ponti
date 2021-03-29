package it.polimi.ingsw.server.model.personalboardpackage;

import java.util.*;
import it.polimi.ingsw.server.model.ResourceBank;
import it.polimi.ingsw.server.model.Resource;


public class DefaultSlot implements DevelopmentSlot {
    private Resource output;

    /**
     * @param selected allows user to select which resource
     *                 to produce from the wildcard
     */
    public void setOutput(Resource selected) {
        output = selected;
    }

    /**
     * @param resources Given the production's required input
     *                  returns the related output
    */
    @Override
    public Map<Resource, Integer> produce(Map<Resource, Integer> resources) {
        return ResourceBank.getResource(output);
    }
}