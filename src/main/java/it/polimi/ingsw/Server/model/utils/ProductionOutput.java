package it.polimi.ingsw.server.model.utils;

import java.util.Map;
import java.util.Objects;

public class ProductionOutput {
    private int faithIncrement;
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
