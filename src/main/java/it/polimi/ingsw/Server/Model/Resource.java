package it.polimi.ingsw.server.model;

/**
 * this enumeration represents all possibles type of resources
 */
public enum Resource {
    COIN("Coin"),
    SERVANT("Servant"),
    STONE("Stone"),
    SHIELD("Shield");

    public final String label;

    private Resource(String label) {
        this.label = label;
    }
}