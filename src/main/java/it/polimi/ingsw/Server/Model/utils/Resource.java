package it.polimi.ingsw.server.model.utils;

import it.polimi.ingsw.server.model.market.MarketMarble;

/**
 * this enumeration represents all possibles type of resources
 */
public enum Resource {
    COIN("Coin"),
    SERVANT("Servant"),
    STONE("Stone"),
    SHIELD("Shield");

    public final String label;

    Resource(String label) {
        this.label = label;
    }

    public MarketMarble toMarble() {
        return switch(this) {
            case STONE -> MarketMarble.GREY;
            case SERVANT -> MarketMarble.PURPLE;
            case SHIELD -> MarketMarble.BLUE;
            case COIN -> MarketMarble.YELLOW;
        };
    }
}