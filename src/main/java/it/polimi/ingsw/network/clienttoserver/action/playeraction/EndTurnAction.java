package it.polimi.ingsw.network.clienttoserver.action.playeraction;

import it.polimi.ingsw.network.clienttoserver.action.gameaction.GameAction;
import it.polimi.ingsw.network.clienttoserver.action.gameaction.LorenzoAction;
import it.polimi.ingsw.network.clienttoserver.action.gameaction.StartNextTurnAction;
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
        GameAction consequentAction;
        if (game.size() == 1)
            consequentAction = new LorenzoAction(game);
        else consequentAction = new StartNextTurnAction(game);
        return consequentAction;
    }

    @Override
    public void runChecks() throws InvalidActionException {
        super.runChecks();
        if (!player.isValidNextAction(ExecutedActions.TURN_ENDED_ACTION))
            throw new InvalidActionException("You cannot end your turn at this time");
    }
}