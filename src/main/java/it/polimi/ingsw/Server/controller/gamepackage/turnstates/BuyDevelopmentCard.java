package it.polimi.ingsw.server.controller.gamepackage.turnstates;

import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.Level;
import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.controller.gamepackage.actions.Action;
import it.polimi.ingsw.server.controller.gamepackage.actions.BuyDevelopmentCardAction;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

import java.util.*;
import java.util.stream.Collectors;

//todo: resources are discarded by the controller, don't manage

public class BuyDevelopmentCard implements AbstractTurnState {
    private PersonalBoard board;
    private DevelopmentCardsBoard developmentCardsBoard;
    private BuyDevelopmentCardAction action;

    @Override
    public void setup(Turn turn, Action action) {
        board = turn.getPlayer().getPersonalBoard();
        developmentCardsBoard = turn.getGame().getDevelopmentCardsBoard();
        this.action = (BuyDevelopmentCardAction) action;
    }

    @Override
    public void performAction() {
        buyDevelopmentCard(action.getLevel(), action.getColor(), action.getPosition());
    }

    /**
     * @return a list containing development cards that player can buy
     */
    public List<DevelopmentCard> affordableCards() {
        return Arrays.stream(developmentCardsBoard.peekBoard()).filter(
                developmentCards -> Arrays.stream(developmentCards).filter(
                        developmentCard -> board.hasResources(developmentCard.getCost())
                )
        ).collect(Collectors.toList());
    }
    /**
     * @param level the level of the development card that will be bought
     * @param color the color of the development card that will be bought
     */
    private void buyDevelopmentCard(Level level, Color color, int position) {
        board.placeDevelopmentCard(developmentCardsBoard.pick(level, color), position);
    }

    public void setAction(Action action){
        this.action = (BuyDevelopmentCardAction) action;
    }
}