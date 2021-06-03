package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.utils.Resource;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is a dumber version of a regular DevelopmentCard,
 * this class only contains the data stored in a DevelopmentCard but has none of its logic.
 */
public class DumbDevelopmentCard implements Serializable {
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

    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    public String formatPrintableStringAt(int x, int y) {
        List<String> costList = convertMapToPrintableList(this.getCost(), 3);
        List<String> inputList = convertMapToPrintableList(this.getInputResources(), 2);
        List<String> outputList = convertMapToPrintableList(this.getOutputResources(), 3);
        List<String> levelList = convertLevelToPrintableList();

        return    "\033[" + x + ";" + y + "H╔══════════════╗"
                + "\033[" + (x+1) + ";" + y + "H║ " + costList.get(0) + "       " + levelList.get(0) + " ║"
                + "\033[" + (x+2) + ";" + y + "H║ " + costList.get(1) + "       " + levelList.get(1) + " ║"
                + "\033[" + (x+3) + ";" + y + "H║ " + costList.get(2) + "       " + levelList.get(2) + " ║"
                + "\033[" + (x+4) + ";" + y + "H║              ║"
                + "\033[" + (x+5) + ";" + y + "H║ " + inputList.get(0) + " -> " + outputList.get(0) + " ║"
                + "\033[" + (x+6) + ";" + y + "H║ " + inputList.get(1) + "    " + outputList.get(1) + " ║"
                + "\033[" + (x+7) + ";" + y + "H║         " + outputList.get(2) + " ║"
                + "\033[" + (x+8) + ";" + y + "H║              ║"
                + "\033[" + (x+9) + ";" + y + "H║    " + Constants.ANSI_YELLOW + getVictoryPoints() + Constants.ANSI_RESET+ "     ║"
                + "\033[" + (x+10) + ";" + y + "H╚══════════════╝";
    }

    /**
     * Creates a list of printable elements from a map of resources
     * @param map the map from which the list should be created off from
     * @param maxSize the maximum size expected of the map
     * @return a list of printable strings with some ascii escape sequences
     */
    private List<String> convertMapToPrintableList(Map<Resource, Integer> map, int maxSize){
        List<String> stringList = new ArrayList<>(map.entrySet())
                .stream()
                .map(
                        entry -> String.valueOf(entry.getValue()).concat("x ").concat(entry.getKey().toString())
                ).collect(Collectors.toList());
        IntStream.range(0, maxSize-stringList.size()).forEach(
                i -> stringList.add("    ")
        );
        return stringList;
    }

    /**
     * Creates a printable list based on the card's level and color
     * @return a list of printable strings with some ascii escape sequences
     */
    private List<String> convertLevelToPrintableList(){
        List<String> stringList = new ArrayList<>();
        IntStream.range(0, level.getLevel()).forEach(
                i -> stringList.add(color.getColoredLevel())
        );
        IntStream.range(0, 3-stringList.size()).forEach(
                i -> stringList.add(" ")
        );
        return stringList;
    }
}
