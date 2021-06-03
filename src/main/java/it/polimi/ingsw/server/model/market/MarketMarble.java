package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.utils.Resource;

/**
 * this enumeration represents the market marbles colors
 */
public enum MarketMarble {
    WHITE(Constants.ANSI_WHITE),
    RED(Constants.ANSI_RED),
    GREY(Constants.ANSI_GREY),
    BLUE(Constants.ANSI_BLUE),
    YELLOW(Constants.ANSI_YELLOW),
    PURPLE(Constants.ANSI_PURPLE);

    private String marbleColor;

    private MarketMarble (String marbleColor){
        this.marbleColor = marbleColor;
    }

    @Override
    public String toString() {
        return marbleColor + Constants.MARBLE + Constants.ANSI_RESET;
    }

    public String getMarbleColor() {
        return marbleColor;
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