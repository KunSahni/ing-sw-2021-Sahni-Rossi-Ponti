package it.polimi.ingsw.client.utils.dumbobjects;

import java.io.Serializable;
import java.util.stream.IntStream;

/**
 * This is a dumber version of a regular DevelopmentCardsBoard,
 * this class only contains the top layer of the cards contained in the DevelopmentCardsBoard on the server.
 * Also it contains none of the regular DevelopmentCardsBoard's logic.
 */
public class DumbDevelopmentCardsBoard implements Serializable {
    private DumbDevelopmentCard[][] board;
    private static DumbDevelopmentCardsBoard dumbDevelopmentCardsBoard;

    public static DumbDevelopmentCardsBoard getInstance(){
        if(dumbDevelopmentCardsBoard!=null)
            return dumbDevelopmentCardsBoard;
        return new DumbDevelopmentCardsBoard();
    }


    private DumbDevelopmentCardsBoard() {
        board = new DumbDevelopmentCard[3][4];
    }

    /**
     * @param updatedDevelopmentCardsBoard an updated version of the cards contained in the DevelopmentCardsBoard
     */
    public void updateBoard(DumbDevelopmentCard[][] updatedDevelopmentCardsBoard) {
        IntStream.range(0,3).forEach(
                i->IntStream.range(0,4).forEach(
                        j-> this.board[i][j] = updatedDevelopmentCardsBoard[i][j]
                )
        );
    }

    public DumbDevelopmentCard[][] getBoard() {
        return board;
    }
}
