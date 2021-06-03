package it.polimi.ingsw.server.model.utils;

import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.market.MarketMarble;

/**
 * this enumeration represents all possibles type of resources
 */
public enum Resource {
    COIN("coin", Constants.ANSI_YELLOW),
    SERVANT("servant", Constants.ANSI_PURPLE),
    STONE("stone", Constants.ANSI_GREY),
    SHIELD("shield", Constants.ANSI_BLUE);

    public final String label;
    public final String color;

    Resource(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public MarketMarble toMarble() {
        return switch(this) {
            case STONE -> MarketMarble.GREY;
            case SERVANT -> MarketMarble.PURPLE;
            case SHIELD -> MarketMarble.BLUE;
            case COIN -> MarketMarble.YELLOW;
        };
    }

    @Override
    public String toString() {
        return color + Constants.RESOURCE + Constants.ANSI_RESET;
    }

    public static Resource getResource(String resource) {
        for(Resource r : values())
            if(r.label == resource) return r;
        throw new IllegalArgumentException();
    }
}