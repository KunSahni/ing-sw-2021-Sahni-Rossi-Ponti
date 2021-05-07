package it.polimi.ingsw.server.model.utils;


/**
 * All inheriting classes will handle different game phases by implementing the run method
 */
public enum GameState {
    PRE_GAME,
    IN_GAME,
    LAST_ROUND,
    GAME_FINISHED
}