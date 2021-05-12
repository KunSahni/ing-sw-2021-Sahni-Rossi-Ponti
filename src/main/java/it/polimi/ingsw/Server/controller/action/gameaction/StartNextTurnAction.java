package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.model.Game;

public class StartNextTurnAction extends GameAction {

    public StartNextTurnAction(Game game) {
        super(game);
    }

    @Override
    public GameAction execute() {
        game.nextTurn();
        return null;
    }

}