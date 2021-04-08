package it.polimi.ingsw.server.controller.gamestates;


import it.polimi.ingsw.server.controller.gamepackage.Game;

public class LastRound extends AbstractGameState {
    public LastRound(Game game) {
        super(game);
    }

    /**
     * Executes the last round of turns up until the last player is reached
     */
    @Override
    public void run() {
        // TODO: Test if loop is correct and is not cutting off/allowing more turns
        while(game.getPlayers().get(0).getPosition() != game.getGameSize()) {
            game.nextTurn();
        }
        game.setNextState(new GameFinished(game));
    }
}