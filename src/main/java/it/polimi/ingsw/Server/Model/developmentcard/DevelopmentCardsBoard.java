package it.polimi.ingsw.server.model.developmentcard;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import it.polimi.ingsw.server.model.utils.ChangesHandler;

import static java.util.stream.Collectors.toList;

public class DevelopmentCardsBoard {
    private final ChangesHandler changesHandler;
    private final DevelopmentCardsDeck[][] board;


    /**
     * Creates Development cards board loading all cards from a json file
     * Cards are put in a matrix of decks,
     * all level1 cards are in decks in the row 2, level2 cards are in the decks in the row 1,
     * level 3 cards are in the deck in the row 0
     * green cards are in the decks in the column 0, blue cards are in the decks in column 1,
     * yellow cards are in the decks in column 2, purple cards are in the decks in column 3
     */
    public DevelopmentCardsBoard(ChangesHandler changesHandler) throws FileNotFoundException {
        this.changesHandler = changesHandler;
        this.board = new DevelopmentCardsDeck[3][4];
        int i = 2, j = 0;
        for (Level l : Level.values()) {
            for (Color c : Color.values()) {
                board[i][j] = new DevelopmentCardsDeck(changesHandler, c, l);
                j++;
            }
            j=0;
            i--;
        }
        this.changesHandler.publishDevelopmentCardsBoard(topViewList());
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
        changesHandler.publishDevelopmentCardsBoard(topViewList());
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

    private List<DevelopmentCard> topViewList() {
        return Arrays.stream(board)
                .flatMap(Arrays::stream)
                .map(DevelopmentCardsDeck::peek)
                .collect(Collectors.toList());
    }
}