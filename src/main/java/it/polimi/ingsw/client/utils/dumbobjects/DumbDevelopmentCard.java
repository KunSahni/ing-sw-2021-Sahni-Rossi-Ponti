package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This is a dumber version of a regular DevelopmentCard,
 * this class only contains the data stored in a DevelopmentCard but has none of its logic.
 */
public class DumbDevelopmentCard {
    private final Color color;
    private final Level level;
    private final int victoryPoints;
    private final Map<Resource, Integer> inputResources;
    private final Map<Resource, Integer> outputResources;
    private final Map<Resource, Integer> cost;
    private final int faithIncrement;


    public DumbDevelopmentCard(DevelopmentCard developmentCard) {
        this.color = developmentCard.getColor();
        this.level = developmentCard.getLevel();
        this.victoryPoints = developmentCard.getVictoryPoints();
        this.inputResources = new HashMap<>(developmentCard.getInputResources());
        if(developmentCard.getOutputResources() !=null)
            this.outputResources = new HashMap<>(developmentCard.getOutputResources());
        else
            this.outputResources = null;
        this.cost = new HashMap<>(developmentCard.getCost());
        this.faithIncrement = developmentCard.getFaithIncrement();
    }

    public Color getColor() {
        return color;
    }

    public Level getLevel() {
        return level;
    }

    public Map<Resource, Integer> getInputResources() {
        if (Optional.ofNullable(inputResources).isPresent()) {
            return new HashMap<>(inputResources);
        }
        else{
            return null;
        }
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
        if (Optional.ofNullable(cost).isPresent()) {
            return new HashMap<>(cost);
        }
        else{
            return null;
        }
    }

    public int getFaithIncrement() {
        return faithIncrement;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public DevelopmentCard convert(){
        return new DevelopmentCard(color, level, victoryPoints, inputResources, outputResources, cost, faithIncrement);
    }
}
