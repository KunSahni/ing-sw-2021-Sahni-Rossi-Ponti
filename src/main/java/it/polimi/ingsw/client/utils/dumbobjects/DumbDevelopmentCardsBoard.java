package it.polimi.ingsw.client.utils.dumbobjects;

import java.util.stream.IntStream;

/**
 * This is a dumber version of a regular DevelopmentCardsBoard,
 * this class only contains the top layer of the cards contained in the DevelopmentCardsBoard on the server.
 * Also it contains none of the regular DevelopmentCardsBoard's logic.
 */
public class DumbDevelopmentCardsBoard {
    private DumbDevelopmentCard[][] board;


    public DumbDevelopmentCardsBoard() {
        board = new DumbDevelopmentCard[4][3];
    }

    /**
     * @param developmentCards a flattened list of all the DevelopmentCards placed
     *                         in the top layer of the DevelopmentCardsBoard, an empty deck is represented with null
     */
    public void updateBoard(DumbDevelopmentCard[] developmentCards) {
        IntStream.range(0, developmentCards.length).forEach(
                i-> this.board[i/4][i%4] = developmentCards[i]
        );
    }

    public DumbDevelopmentCard[][] getBoard() {
        return board;
    }
}
