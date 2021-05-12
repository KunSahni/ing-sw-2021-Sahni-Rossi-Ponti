package it.polimi.ingsw.server.model.utils;


/**
 * All inheriting classes will handle different game phases by implementing the run method
 */
public enum GameState {
    NOT_STARTED,
    PRE_GAME_LEADER_CARDS_CHOICE,
    PRE_GAME_ASSIGN_INKWELL,
    PRE_GAME_INIT_FAITH_POSITIONS,
    PRE_GAME_RESOURCES_CHOICE,
    IN_GAME_TURN_STARTED,
    LAST_ROUND,
    GAME_FINISHED
}