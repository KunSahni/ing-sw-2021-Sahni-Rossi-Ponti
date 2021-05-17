package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.model.Game;

public class EndGameAction extends GameAction{
    public EndGameAction(Game game) {
        super(game);
    }

    @Override
    public GameAction execute() {
        return null;
    }
}
