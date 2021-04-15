package it.polimi.ingsw.server.model.developmentcard;

import it.polimi.ingsw.server.model.utils.*;

import java.util.*;




public class DevelopmentCard implements VictoryPointsElement {
    private final Color type;
    private final Level level;
    private final int victoryPoints;
    private final Map<Resource, Integer> inputResources;
    private final Map<Resource, Integer> outputResources;
    private final Map<Resource, Integer> cost;
    private final int faithIncrement;


    public DevelopmentCard(Color type, Level level, int victoryPoints, Map<Resource, Integer> inputResources, Map<Resource, Integer> outputResources, Map<Resource, Integer> cost, int faithIncrement) {
        this.type = type;
        this.level = level;
        this.victoryPoints = victoryPoints;
        this.inputResources = inputResources;
        this.outputResources = outputResources;
        this.cost = cost;
        this.faithIncrement = faithIncrement;
    }

    public Color getColor() {
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
        return new ProductionOutput(faithIncrement, outputResources);
    }

}