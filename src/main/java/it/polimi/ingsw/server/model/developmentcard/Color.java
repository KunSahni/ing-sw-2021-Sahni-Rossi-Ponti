package it.polimi.ingsw.server.model.developmentcard;

import it.polimi.ingsw.client.utils.constants.Constants;

/**
 * this enumeration represents possible colors of Development Cards
 */
public enum Color {
    GREEN(Constants.GREEN_LEVEL),
    BLUE(Constants.BLUE_LEVEL),
    YELLOW(Constants.YELLOW_LEVEL),
    PURPLE(Constants.PURPLE_LEVEL),
    ;

    private final String coloredLevel;

    Color(String coloredLevel){
        this.coloredLevel = coloredLevel;
    }

    public String getColoredLevel() {
        return coloredLevel;
    }
}