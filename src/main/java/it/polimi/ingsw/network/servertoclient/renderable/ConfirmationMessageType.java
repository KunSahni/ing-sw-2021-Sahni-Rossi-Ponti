package it.polimi.ingsw.network.servertoclient.renderable;

/**
 * Contains various types of actions which can be sent by user and might be valid
 */
public enum ConfirmationMessageType {
    ACTIVATE_LEADER_CARD("Activated leader card"),
    ACTIVATE_PRODUCTION("Activated production"),
    BUY_DEVELOPMENT_CARD("Bought development card"),
    DISCARD_LEADER_CARD("Discarded leader card"),
    END_TURN("Turn ended"),
    PREGAME_LEADER_CARDS_CHOICE("Chose leader cards"),
    PREGAME_RESOURCE_CHOICE("Chose resources"),
    SELECT_MARBLES("Selected marbles"),
    TAKE_FROM_MARKET("Took from market");

    private final String message;

    ConfirmationMessageType(String message){
        this.message = message;
    }
}
