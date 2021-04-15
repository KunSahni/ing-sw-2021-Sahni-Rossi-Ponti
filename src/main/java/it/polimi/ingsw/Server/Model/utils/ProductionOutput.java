package it.polimi.ingsw.server.model.utils;

import java.util.Map;

public class ProductionOutput {
    public int faithIncrement;
    public Map<Resource, Integer> resources;

    public ProductionOutput (int faithIncrement, Map<Resource, Integer> resources){
        this.faithIncrement=faithIncrement;
        this.resources = resources;
    }

    public ProductionOutput(int faithIncrement) {
        this.faithIncrement = faithIncrement;
        this.resources = null;
    }

    public ProductionOutput(Map<Resource, Integer> resources) {
        this.resources = resources;
        this.faithIncrement = 0;
    }

    public int getFaithIncrement() {
        return faithIncrement;
    }

    public Map<Resource, Integer> getResources() {
        return resources;
    }
}
