package it.polimi.ingsw.server.controller.messages.actions;

import it.polimi.ingsw.server.model.ActionToken;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.DevelopmentCardsBoard;
import it.polimi.ingsw.server.controller.gamepackage.Turn;

/**
 * This class represents the action of discarding two DevelopmentCards from the DevelopmentCardsBoard
 */
public class DiscardDevelopmentCardsAction implements Forwardable {
    private final Color color;
    private final DevelopmentCardsBoard developmentCardsBoard;

    /**
     * @param turn the turn in which this action is happening
     * @param actionToken the actionToken corresponding to this action
     */
    public DiscardDevelopmentCardsAction(Turn turn, ActionToken actionToken) {
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
    public void forward() {
        discardCards();
    }

    private void discardCards(){
        developmentCardsBoard.discardTwo(color);
    }
}