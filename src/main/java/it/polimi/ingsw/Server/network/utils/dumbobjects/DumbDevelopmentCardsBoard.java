package it.polimi.ingsw.server.network.utils.dumbobjects;

import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;

/**
 * This is a dumber version of a regular DevelopmentCardsBoard,
 * this class only contains the top layer of the cards contained in the DevelopmentCardsBoard on the server.
 * Also it contains none of the regular DevelopmentCardsBoard's logic.
 */
public class DumbDevelopmentCardsBoard {
    private final DevelopmentCard[][] board;

    public DumbDevelopmentCardsBoard(DevelopmentCard[][] board) {
        this.board = board;
    }

    public DevelopmentCard[][] getBoard() {
        return board;
    }
}
