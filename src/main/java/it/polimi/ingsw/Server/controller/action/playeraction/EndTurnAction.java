package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.controller.action.gameaction.GameAction;
import it.polimi.ingsw.server.controller.action.gameaction.StartNextTurnAction;
import it.polimi.ingsw.server.model.utils.Actions;

public class EndTurnAction extends PlayerAction {

    @Override
    public GameAction execute() {
        player.addAction(Actions.TURN_ENDED_ACTION);
        return new StartNextTurnAction(game);
    }

    @Override
    public void runChecks() throws InvalidActionException {
        super.runChecks();
        if (player.isValidNextAction(Actions.TURN_ENDED_ACTION))
            throw new InvalidActionException("You cannot end your turn at this time");
    }
}