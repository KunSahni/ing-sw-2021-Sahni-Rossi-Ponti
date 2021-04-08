package it.polimi.ingsw.server.controller.gamestates;


import it.polimi.ingsw.server.controller.gamepackage.Game;

public class InGame extends AbstractGameState {
    public InGame(Game game) {
        super(game);
    }

    /**
     * Execute turns regularly until the gameState switches to LastRound
     */
    @Override
    public void run() {
        while(game.getCurrentState() instanceof InGame) {
            game.nextTurn();
        }
        //todo: figure out who is actually setting nextstate to LastRound
    }
}