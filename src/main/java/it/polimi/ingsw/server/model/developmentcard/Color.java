package it.polimi.ingsw.server.model.developmentcard;

import it.polimi.ingsw.client.utils.constants.Constants;

/**
 * this enumeration represents possible colors of Development Cards
 */
public enum Color {
    GREEN(Constants.ANSI_GREEN),
    YELLOW(Constants.ANSI_YELLOW),
    BLUE(Constants.ANSI_BLUE),
    PURPLE(Constants.ANSI_PURPLE);

    private String color;

    private Color(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }
}