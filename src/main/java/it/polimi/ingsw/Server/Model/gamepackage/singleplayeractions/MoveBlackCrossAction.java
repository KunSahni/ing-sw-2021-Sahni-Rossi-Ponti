package it.polimi.ingsw.server.model.gamepackage.singleplayeractions;

import it.polimi.ingsw.server.model.ActionToken;
import it.polimi.ingsw.server.model.gamepackage.actions.Action;

public class MoveBlackCrossAction implements Action {
    private final ActionToken actionToken;

    public MoveBlackCrossAction(ActionToken actionToken) {
        this.actionToken = actionToken;
    }

    public ActionToken getActionToken() {
        return actionToken;
    }
}