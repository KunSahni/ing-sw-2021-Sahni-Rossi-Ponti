package it.polimi.ingsw.server.model.developmentcard;

import java.util.Arrays;

import it.polimi.ingsw.server.model.utils.ChangesHandler;

public class DevelopmentCardsBoard {
    private transient ChangesHandler changesHandler;
    private final DevelopmentCardsDeck[][] board;

    private DevelopmentCardsBoard() {
        this.board = new DevelopmentCardsDeck[3][4];
    }

    public void init(ChangesHandler changesHandler) {
        this.changesHandler = changesHandler;
    }

    /**
     * pick the first card of the deck of color and level specified
    * @return the picked card
    */
    public DevelopmentCard pick(Level level, Color color) {
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

        DevelopmentCard popped = board[line][column].pop();
        changesHandler.writeDevelopmentCardsBoard(this);
        return popped;
    }


    /**
     * @return the first deck development card of color and level specified not picking it
    */
    public DevelopmentCard peekCard(Level level, Color color) {
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
        return board[line][column].peek();

    }

    public DevelopmentCardsDeck[][] peekBoard() {
        DevelopmentCardsDeck [][] newBoard = new DevelopmentCardsDeck[board.length][];
        for(int i = 0; i < board.length; i++)
            newBoard[i] = board[i].clone();
        return newBoard;
    }

    /**
     * discard two card from the board in single player games when the action token discard is picked
    */
    public void discardTwo(Color color) {
        int i = 0; //counts how many cards have been discarded
        int column;
        //select the column corresponding to the color
        column = switch (color) {
            case GREEN -> 0;
            case BLUE -> 1;
            case YELLOW -> 2;
            case PURPLE -> 3;
        };
        //if there are elements in the deck discard two of them
        for (DevelopmentCard d: board[2][column].getDeck()) {
            if (i == 2){return;}
            if (board[2][column].peek()!=null){
                board[2][column].pop();
                i++;
            }
        }

        for (DevelopmentCard d: board[1][column].getDeck()) {
            if (i == 2){return;}
            if (board[1][column].peek()!=null){
                board[1][column].pop();
                i++;
            }
        }

        for (DevelopmentCard d: board[0][column].getDeck()) {
            if (i == 2){return;}
            if (board[0][column].peek()!=null){
                board[0][column].pop();
                i++;
            }
        }
    }

    public void shuffle() {
        Arrays.stream(board).flatMap(Arrays::stream).forEach(DevelopmentCardsDeck::shuffle);
    }
}