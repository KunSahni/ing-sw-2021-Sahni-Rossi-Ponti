package it.polimi.ingsw.server.model.developmentcard;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.utils.constants.Constants;

/**
 * this enumeration represents possible colors of Development Cards
 */
public enum Color {
    @SerializedName("GREEN") GREEN("green", Constants.GREEN_LEVEL),
    @SerializedName("BLUE") BLUE("blue", Constants.BLUE_LEVEL),
    @SerializedName("YELLOW") YELLOW("yellow", Constants.YELLOW_LEVEL),
    @SerializedName("PURPLE") PURPLE("purple", Constants.PURPLE_LEVEL);

    private final String coloredLevel;
    private final String color;

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
            if(c.getColor() == color) return c;
        throw new IllegalArgumentException();
    }
}