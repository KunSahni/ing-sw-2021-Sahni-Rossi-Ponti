package it.polimi.ingsw.server.model.market;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.utils.Resource;

/**
 * this enumeration represents the market marbles colors
 */
public enum MarketMarble {
    @SerializedName("WHITE") WHITE(Constants.ANSI_WHITE),
    @SerializedName("RED") RED(Constants.ANSI_RED),
    @SerializedName("GREY") GREY(Constants.ANSI_GREY),
    @SerializedName("BLUE") BLUE(Constants.ANSI_BLUE),
    @SerializedName("YELLOW") YELLOW(Constants.ANSI_YELLOW),
    @SerializedName("PURPLE") PURPLE(Constants.ANSI_PURPLE);

    private String marbleColor;

    MarketMarble (String marbleColor){
        this.marbleColor = marbleColor;
    }

    public String formatPrintableString() {
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