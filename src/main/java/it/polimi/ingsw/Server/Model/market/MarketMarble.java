package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.utils.Resource;

/**
 * this enumeration represents the market marbles colors
 */
public enum MarketMarble {
    WHITE("White"),
    RED("Red"),
    GREY("Grey"),
    BLUE("Blue"),
    YELLOW("Yellow"),
    PURPLE("Purple");

    public final String label;

    MarketMarble(String label) {
        this.label = label;
    }

    public Resource toResource() {
        return switch(this) {
            case GREY -> Resource.STONE;
            case YELLOW -> Resource.COIN;
            case BLUE -> Resource.SHIELD;
            case PURPLE -> Resource.SERVANT;
            case RED, WHITE -> null;
        };
    }
}