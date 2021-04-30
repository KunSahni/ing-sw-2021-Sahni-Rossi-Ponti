package it.polimi.ingsw.server.controller.message.action.gameaction;

import it.polimi.ingsw.server.controller.message.action.Action;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.controller.gamepackage.Turn;

/**
 * This class represents the action of discarding two DevelopmentCards from
 * the DevelopmentCardsBoard
 */
public class DiscardTwoDevelopmentCardsAction implements Action {
    private final Color color;
    private final DevelopmentCardsBoard developmentCardsBoard;

    /**
     * @param turn the turn in which this action is happening
     * @param actionToken the actionToken corresponding to this action
     */
    public DiscardTwoDevelopmentCardsAction(Turn turn, ActionToken actionToken) {
        if(actionToken.equals(ActionToken.REMOVEBLUE))
            this.color = Color.BLUE;
        else if(actionToken.equals(ActionToken.REMOVEYELLOW))
            this.color = Color.YELLOW;
        else if(actionToken.equals(ActionToken.REMOVEPURPLE))
            this.color = Color.PURPLE;
        else
            this.color = Color.GREEN;

        this.developmentCardsBoard = turn.getGame().getDevelopmentCardsBoard();
    }


    @Override
    public void execute() {
        discardCards();
    }

    private void discardCards(){
        developmentCardsBoard.discardTwo(color);
    }
}