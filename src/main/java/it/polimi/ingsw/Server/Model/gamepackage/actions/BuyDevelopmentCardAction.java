package it.polimi.ingsw.server.model.gamepackage.actions;

import java.util.*;
import java.util.stream.Collectors;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

public class BuyDevelopmentCardAction implements Action {
    private final PersonalBoard board;
    private final DevelopmentCardsBoard developmentCardsBoard;

    public BuyDevelopmentCardAction(PersonalBoard board, DevelopmentCardsBoard developmentCardsBoard) {
        this.board = board;
        this.developmentCardsBoard = developmentCardsBoard;
    }

    @Override
    public void performAction() {

    }

    /**
     * @return a list containing development cards that player can buy
     */
    public List<DevelopmentCard> affordableCards() {
        return developmentCardsBoard.peekBoard().stream().filter(
                developmentCards -> developmentCards.stream().filter(
                        developmentCard -> board.hasResources(developmentCard.getCost())
                )
        ).collect(Collectors.toList());
    }
    /**
    * @param level the level of the development card that will be bought
    * @param color the color of the development card that will be bought
    */
    public void buyDevelopmentCard(Level level, Color color, int position) {
        board.placeDevelopmentCard(developmentCardsBoard.pick(level, color), position);
    }

}