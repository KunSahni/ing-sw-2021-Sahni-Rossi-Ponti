package it.polimi.ingsw.server.model.developmentcard;

/**
 * this enumeration represents possible colors of Development Cards
 */
public enum Color {
    GREEN("Green"),
    BLUE("Blue"),
    YELLOW("Yellow"),
    PURPLE("Purple");

    public final String label;

    Color(String label) {
        this.label = label;
    }
}