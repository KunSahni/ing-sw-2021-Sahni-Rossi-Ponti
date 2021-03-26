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

    private DevelopmentCardsBoard ;

    private DevelopmentCardsDeck ;

    private ResourceBank ;

    private Color ;


    public Color getType() {
        return type;
    }

    public Level getLevel() {
        return level;
    }

    public int getVictoryPoints() {
        return victoryPoints;
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

    public DevelopmentCardsBoard get() {
        return ;
    }

    public DevelopmentCardsDeck get() {
        return ;
    }

    public ResourceBank get() {
        return ;
    }

    public Color get() {
        return ;
    }

    public Level get() {
        return ;
    }

    public void set(DevelopmentCardsBoard ) {
        this. = ;
    }

    public void set(DevelopmentCardsDeck ) {
        this. = ;
    }

    public void set(ResourceBank ) {
        this. = ;
    }

    public void set(Color ) {
        this. = ;
    }

    public void set(Level ) {
        this. = ;
    }

    public void link(DevelopmentCardsBoard _) {
        if (_ != null) {
            _.unlink();
            _.set(this);
        }

        unlink();
        set(_);
    }

    public void link(DevelopmentCardsDeck _) {
        if (_ != null) {
            _.getDeck().add(this);
        }

        unlink();
        set(_);
    }

    public void link(ResourceBank _) {
        set(_);
    }

    public void link(Color _) {
        if (_ != null) {
            _.unlink();
            _.set(this);
        }

        unlink();
        set(_);
    }

    public void link(Level _) {
        if (_ != null) {
            _.unlink();
            _.set(this);
        }

        unlink();
        set(_);
    }

    public void unlink() {
        if (get() != null) {
            get().set(null);
            set(null);
        }
    }

    public void unlink() {
        if (get() != null) {
            get().getDeck().remove(this);
            set(null);
        }
    }

    public void unlink() {
        set(null);
    }

    public void unlink() {
        if (get() != null) {
            get().set(null);
            set(null);
        }
    }

    public void unlink() {
        if (get() != null) {
            get().set(null);
            set(null);
        }
    }

    @Override
    public int getVictoryPoints() {

    }

    /**
     * use the production of a development card calling the StorageElement methods and
     * move the marker on the Faith Track
     * @return the resources it produces
     */
    public Map<Resource, Integer> produce() {
        int i;

        StorageElement.discardResources(inputResources);
        StorageElement.storeResources(outputResources);
        for(i=0; i<faithIncrement; i++) FaithTrack.moveMarker();
        return outputResources;
    }

}