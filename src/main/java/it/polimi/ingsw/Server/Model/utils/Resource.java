package it.polimi.ingsw.server.model.utils;

/**
 * this enumeration represents all possibles type of resources
 */
public enum Resource {
    COIN("Coin"),
    SERVANT("Servant"),
    STONE("Stone"),
    SHIELD("Shield");

    public final String label;

    Resource(String label) {
        this.label = label;
    }
}