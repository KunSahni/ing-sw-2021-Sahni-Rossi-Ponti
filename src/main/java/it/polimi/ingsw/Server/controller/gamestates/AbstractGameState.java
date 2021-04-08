package it.polimi.ingsw.server.controller.gamestates;


import it.polimi.ingsw.server.controller.gamepackage.Game;

/**
 * All inheriting classes will handle different game phases by implementing the run method
 */
public abstract class AbstractGameState {
    protected Game game;

    public AbstractGameState(Game game) {
        this.game = game;
    }

    public abstract void run();
}