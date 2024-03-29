package it.polimi.ingsw.server.model.market;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.utils.Resource;

import java.io.Serializable;

/**
 * this enumeration represents the market marbles colors
 */
public enum MarketMarble implements Serializable {
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

    public static MarketMarble getMarble(String marbleColor) {
        switch (marbleColor){
            case "grey" -> {
                return MarketMarble.GREY;
            }
            case "yellow" -> {
                return MarketMarble.YELLOW;
            }
            case "blue" -> {
                return MarketMarble.BLUE;
            }
            case "purple" -> {
                return MarketMarble.PURPLE;
            }
            case "red" -> {
                return MarketMarble.RED;
            }
            case "white" -> {
                return MarketMarble.WHITE;
            }
            default -> throw new IllegalArgumentException();
        }
    }

    public String toImagePath() {
        return "/img/marbles/"  + switch (this) {
            case RED -> "red";
            case YELLOW -> "yellow";
            case BLUE -> "blue";
            case PURPLE -> "purple";
            case GREY -> "grey";
            case WHITE -> "white";
        } + ".png";
    }
}