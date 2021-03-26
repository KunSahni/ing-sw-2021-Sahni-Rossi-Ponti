package it.polimi.ingsw.server.model.gamepackage.singleplayeractions;

import it.polimi.ingsw.server.model.ActionToken;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.gamepackage.actions.Action;

public class DiscardDevelopmentCardsAction implements Action {
    private final ActionToken actionToken;
    private final Color color;

    public DiscardDevelopmentCardsAction(ActionToken actionToken) {
        this.actionToken = actionToken;

        if(actionToken.equals(ActionToken.REMOVEBLUE))
            this.color = Color.BLUE;
        else if(actionToken.equals(ActionToken.REMOVEYELLOW))
            this.color = Color.YELLOW;
        else if(actionToken.equals(ActionToken.REMOVEPURPLE))
            this.color = Color.PURPLE;
        else
            this.color = Color.GREEN;
    }

    public ActionToken getActionToken() {
        return actionToken;
    }

    public Color getColor() {
        return color;
    }
}