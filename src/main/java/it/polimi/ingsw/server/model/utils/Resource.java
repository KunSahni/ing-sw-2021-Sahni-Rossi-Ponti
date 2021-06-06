package it.polimi.ingsw.server.model.utils;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.market.MarketMarble;

import java.io.Serializable;

/**
 * this enumeration represents all possibles type of resources
 */
public enum Resource implements Serializable {
    @SerializedName("COIN") COIN("coin", Constants.ANSI_YELLOW),
    @SerializedName("SERVANT") SERVANT("servant", Constants.ANSI_PURPLE),
    @SerializedName("STONE") STONE("stone", Constants.ANSI_GREY),
    @SerializedName("SHIELD") SHIELD("shield", Constants.ANSI_BLUE);

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

    public String formatPrintableString() {
        return color + Constants.RESOURCE + Constants.ANSI_RESET;
    }

    public static Resource getResource(String resource) {
        for(Resource r : values())
            if(r.label.equals(resource)) return r;
        throw new IllegalArgumentException();
    }
}