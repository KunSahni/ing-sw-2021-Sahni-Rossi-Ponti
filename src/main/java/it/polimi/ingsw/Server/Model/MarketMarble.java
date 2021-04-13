package it.polimi.ingsw.server.model;

/**
 * this enumeration represents the market marbles colors
 */
public enum MarketMarble {
    WHITE("White"),
    RED("Red"),
    GREY("Grey"),
    BLUE("Blue"),
    YELLOW("Yellow"),
    PURPLE("Pruple");

    public final String label;

    MarketMarble(String label) {
        this.label = label;
    }
}