package it.polimi.ingsw.server.model.utils;

import java.util.Map;

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
        return resources;
    }
}
