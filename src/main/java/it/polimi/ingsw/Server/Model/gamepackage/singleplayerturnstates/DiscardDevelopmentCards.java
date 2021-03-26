package it.polimi.ingsw.server.model.gamepackage.singleplayerturnstates;

import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.gamepackage.Turn;
import it.polimi.ingsw.server.model.gamepackage.actions.Action;
import it.polimi.ingsw.server.model.gamepackage.singleplayeractions.DiscardDevelopmentCardsAction;
import it.polimi.ingsw.server.model.gamepackage.turnstates.AbstractTurnState;

public class DiscardDevelopmentCards implements AbstractTurnState {
    private DiscardDevelopmentCardsAction action;
    private DevelopmentCardsBoard developmentCardsBoard;

    @Override
    public void setup(Turn turn, Action action) {
        this.action = (DiscardDevelopmentCardsAction) action;
        this.developmentCardsBoard = turn.getGame().getDevelopmentCardsBoard();
    }

    @Override
    public void performAction() {
        discardCards(action.getColor());
    }

    private void discardCards(Color color){
        developmentCardsBoard.discardTwo(color);
    }
}