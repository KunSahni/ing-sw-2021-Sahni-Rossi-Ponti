package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.controller.action.gameaction.GameAction;
import it.polimi.ingsw.server.controller.action.gameaction.StartNextTurnAction;

public class DisconnectPlayerAction extends PlayerAction{

    @Override
    public GameAction execute() {
        GameAction consequentAction = null;
        game.disconnect(nickname);
        if (player.isPlayersTurn()) {
            consequentAction = new StartNextTurnAction(game);
        }
        return consequentAction;
    }

    @Override
    public void runChecks() throws InvalidActionException {
    }
}
