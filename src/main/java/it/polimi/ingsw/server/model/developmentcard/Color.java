package it.polimi.ingsw.server.model.developmentcard;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.utils.constants.Constants;

import java.io.Serializable;

/**
 * this enumeration represents possible colors of Development Cards
 */
public enum Color implements Serializable {
    @SerializedName("GREEN") GREEN("green", Constants.GREEN_LEVEL),
    @SerializedName("BLUE") BLUE("blue", Constants.BLUE_LEVEL),
    @SerializedName("YELLOW") YELLOW("yellow", Constants.YELLOW_LEVEL),
    @SerializedName("PURPLE") PURPLE("purple", Constants.PURPLE_LEVEL);

    private final String color;
    private final String coloredLevel;

    Color(String color, String coloredLevel){
        this.color = color;
        this.coloredLevel = coloredLevel;
    }

    public String getColoredLevel() {
        return coloredLevel;
    }

    private String getColor() {
        return color;
    }

    public static Color getColor(String color) {
        for(Color c : values())
            if(c.getColor().equals(color)) return c;
        throw new IllegalArgumentException();
    }
}