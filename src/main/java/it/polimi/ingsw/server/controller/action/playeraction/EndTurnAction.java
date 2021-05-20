package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.controller.action.gameaction.GameAction;
import it.polimi.ingsw.server.controller.action.gameaction.StartNextTurnAction;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.GameState;

public class EndTurnAction extends PlayerAction {

    @Override
    public GameAction execute() {
        player.addAction(ExecutedActions.TURN_ENDED_ACTION);
        if (game.getCurrentState().equals(GameState.IN_GAME)) {
            if (game.getPlayerList().stream().anyMatch(npc ->
                    npc.getPersonalBoard().getFaithTrack().getFaithMarkerPosition() == 24)
                    || game.getPlayer(nickname).getPersonalBoard().getDevelopmentCardsCount() == 7) {
                game.setState(GameState.LAST_ROUND);
            }
        }
        return new StartNextTurnAction(game);
    }

    @Override
    public void runChecks() throws InvalidActionException {
        super.runChecks();
        if (player.isValidNextAction(ExecutedActions.TURN_ENDED_ACTION))
            throw new InvalidActionException("You cannot end your turn at this time");
    }
}