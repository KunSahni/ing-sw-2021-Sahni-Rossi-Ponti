package it.polimi.ingsw.server.model.utils;

import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.market.MarketMarble;

/**
 * this enumeration represents all possibles type of resources
 */
public enum Resource {
    COIN("Coin", Constants.ANSI_YELLOW),
    SERVANT("Servant", Constants.ANSI_PURPLE),
    STONE("Stone", Constants.ANSI_GREY),
    SHIELD("Shield", Constants.ANSI_BLUE);

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
}