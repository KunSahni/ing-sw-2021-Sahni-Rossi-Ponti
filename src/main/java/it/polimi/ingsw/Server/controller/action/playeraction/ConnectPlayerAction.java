package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.controller.action.gameaction.GameAction;

public class ConnectPlayerAction extends PlayerAction{
    @Override
    public GameAction execute() {
        game.connect(nickname);
        return null;
    }

    @Override
    public void runChecks() throws InvalidActionException {
        if (player.isConnected())
            throw new InvalidActionException("Something went wrong: you are already connected.");
    }
}
