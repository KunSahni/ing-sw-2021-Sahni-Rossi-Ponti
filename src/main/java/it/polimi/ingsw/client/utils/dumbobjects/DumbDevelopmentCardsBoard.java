package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;

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

    /**
     * @return the first deck development card of color and level specified not picking it
     */
    public DumbDevelopmentCard peekCard(Level level, Color color) {
        int line;
        int column;
        line = switch (level) {
            case LEVEL1 -> 2;
            case LEVEL2 -> 1;
            case LEVEL3 -> 0;
        };
        column = switch (color) {
            case GREEN -> 0;
            case BLUE -> 1;
            case YELLOW -> 2;
            case PURPLE -> 3;
        };

        try {
            return board[line][column];
        }catch (NullPointerException e){
            return null;
        }
    }


    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    public String formatPrintableStringAt(int x, int y) {
        StringBuilder printableString = new StringBuilder();

        IntStream.range(0,3).forEach(
                i -> IntStream.range(0,4).forEach(
                        j -> {
                            if(board[i][j]!=null)
                                printableString.append(board[i][j].formatPrintableStringAt(x+i*11, y+j*17));
                        }
                )
        );

        return printableString.toString();
    }

}
