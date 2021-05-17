package it.polimi.ingsw.server.model.utils;


/**
 * All inheriting classes will handle different game phases by implementing the run method
 */
public enum GameState {
    NOT_STARTED,
    DEALT_LEADER_CARDS,
    PICKED_LEADER_CARDS,
    ASSIGNED_INKWELL,
    PICKED_RESOURCES,
    IN_GAME,
    LAST_ROUND,
    GAME_FINISHED
}