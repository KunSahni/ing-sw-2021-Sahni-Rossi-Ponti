package it.polimi.ingsw.server.model.utils;

import java.util.Map;
import java.util.Objects;

/**
 * Class representing the output of any production.
 */
public class ProductionOutput {
    /**
     * Number of steps the faith marker of the player who activated the production
     * will take on the faith track. 0 if no step will be taken.
     */
    private int faithIncrement;
    /**
     * Output resources. Null if no resource is produced.
     */
    private Map<Resource, Integer> resources;

    public ProductionOutput (int faithIncrement, Map<Resource, Integer> resources){
        this.faithIncrement=faithIncrement;
        this.resources = resources;
    }

    public int getFaithIncrement() {
        return faithIncrement;
    }

    public Map<Resource, Integer> getResources() {
        if(resources!=null){
            return Map.copyOf(resources);
        }
        else{
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionOutput that = (ProductionOutput) o;
        return faithIncrement == that.faithIncrement && Objects.equals(resources, that.resources);
    }

}
