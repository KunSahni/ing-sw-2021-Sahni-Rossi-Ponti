package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.utils.Resource;

/**
 * this enumeration represents the market marbles colors
 */
public enum MarketMarble {
    WHITE("white"),
    RED("red"),
    GREY("grey"),
    BLUE("blue"),
    YELLOW("yellow"),
    PURPLE("purple");

    private final String color;

    private MarketMarble(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
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