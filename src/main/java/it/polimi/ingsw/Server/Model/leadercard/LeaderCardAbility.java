package it.polimi.ingsw.server.model.leadercard;

/**
 * this enumeration represents all the abilities that a leader card can have
 */
public enum LeaderCardAbility {
    DISCOUNT("Discount"),
    STORE("Store"),
    CONVERT("Convert"),
    PRODUCE("Produce");

    public final String label;

    LeaderCardAbility(String label) {
        this.label = label;
    }
}