package it.polimi.ingsw.server.model;

import java.util.*;
import java.time.*;




public class DevelopmentCard implements VictoryPointsElement {
    private Color type;
    private Level level;
    private int victoryPoints;
    private Map<Resource, Integer> inputResources;
    private Map<Resource, Integer> outputResources;
    private Map<Resource, Integer> cost;
    private int faithIncrement;


    public Color getType() {
        return type;
    }

    public Level getLevel() {
        return level;
    }

    public Map<Resource, Integer> getInputResources() {
        return inputResources;
    }

    public Map<Resource, Integer> getOutputResources() {
        return outputResources;
    }

    public Map<Resource, Integer> getCost() {
        return cost;
    }

    public int getFaithIncrement() {
        return faithIncrement;
    }

    @Override
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * use the production of a development card calling the StorageElement methods and
     * move the marker on the Faith Track
     * @return the resources it produces
     */
    public ProductionOutput produce() {
        ProductionOutput productionOutput = new ProductionOutput(faithIncrement, outputResources);
        return productionOutput;
    }

}