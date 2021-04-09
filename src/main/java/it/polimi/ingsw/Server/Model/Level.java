package it.polimi.ingsw.server.model;

/**
 * this enumeration represents leader cards level
 */
public enum Level {
    LEVEL1("Level1"),
    LEVEL2("Level2"),
    LEVEL3("Level3");

    public final String label;

    private Level(String label) {
        this.label = label;
    }
}