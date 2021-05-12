package it.polimi.ingsw.server.model.utils;

/**
 * A set of enums in which each entry represents an action that can be chosen by a Player
 */

public enum Actions {
    ACTIVATED_LEADER_CARD_ACTION,
    ACTIVATED_PRODUCTION_ACTION,
    BOUGHT_DEVELOPMENT_CARD_ACTION,
    DISCARDED_LEADER_CARD_ACTION,
    TURN_ENDED_ACTION,
    STORED_TEMP_MARBLES_ACTION,
    STORED_MARKET_RESOURCES_ACTION,

    public boolean isCompulsory() {
        return switch(this) {
            case ACTIVATED_PRODUCTION_ACTION,
                    BOUGHT_DEVELOPMENT_CARD_ACTION,
                    STORED_MARKET_RESOURCES_ACTION -> true;
            default -> false;
        };
    }
}
