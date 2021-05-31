package it.polimi.ingsw.server.model.developmentcard;

import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.server.model.utils.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;




public class DevelopmentCard implements VictoryPointsElement {
    private final Color color;
    private final Level level;
    private final int victoryPoints;
    private final Map<Resource, Integer> inputResources;
    private final Map<Resource, Integer> outputResources;
    private final Map<Resource, Integer> cost;
    private final int faithIncrement;


    public DevelopmentCard(Color color, Level level, int victoryPoints, Map<Resource, Integer> inputResources, Map<Resource, Integer> outputResources, Map<Resource, Integer> cost, int faithIncrement) {
        this.color = color;
        this.level = level;
        this.victoryPoints = victoryPoints;
        this.inputResources = inputResources;
        this.outputResources = outputResources;
        this.cost = cost;
        this.faithIncrement = faithIncrement;
    }

    public Color getColor() {
        return color;
    }

    public Level getLevel() {
        return level;
    }

    public Map<Resource, Integer> getInputResources() {
        return new HashMap<>(inputResources);
    }

    public Map<Resource, Integer> getOutputResources() {
        if (Optional.ofNullable(outputResources).isPresent()) {
            return new HashMap<>(outputResources);
        }
        else{
            return null;
        }
    }

    public Map<Resource, Integer> getCost() {
        return new HashMap<>(cost);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevelopmentCard that = (DevelopmentCard) o;
        return victoryPoints == that.victoryPoints && faithIncrement == that.faithIncrement && color == that.color && level == that.level && inputResources.equals(that.inputResources) && (Objects.equals(outputResources, that.outputResources)) && cost.equals(that.cost);
    }

}