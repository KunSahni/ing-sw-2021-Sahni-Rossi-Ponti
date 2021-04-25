package it.polimi.ingsw.server.model.developmentcard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import static it.polimi.ingsw.server.model.developmentcard.Color.*;
import static it.polimi.ingsw.server.model.developmentcard.Level.*;

public class DevelopmentCardsBoard {

    private final DevelopmentCardsDeck[][] board;


    /**
     * Creates Development cards board loading all cards from a json file
     * Cards are put in a matrix of decks,
     * all level1 cards are in decks in the row 2, level2 cards are in the decks in the row 1, level 3 cards are in the deck in the row 0
     * green cards are in the decks in the column 0, blue cards are in the decks in column 1, yellow cards are in the decks in column 2, purple cards are in the decks in column 3
     */
    public DevelopmentCardsBoard() {
        board = new DevelopmentCardsDeck[3][4];
        loadDevelopmentCards();
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
        return board[line][column].pop();
    }

    /**
    * Loads the cards from a json file and creates the related objects.
     * Then it creates all the needed decks by calling the constructor and passing them the cards they need.
    */
    private void loadDevelopmentCards() {
        List<DevelopmentCard> level1;
        List<DevelopmentCard> level2;
        List<DevelopmentCard> level3;

        try {
            JsonReader reader = new JsonReader(new FileReader(new File("src/main/resources/DevelopmentCards.json")));
            DevelopmentCard[] developmentCards = new Gson().fromJson(reader, DevelopmentCard[].class);

            level1 = Arrays.stream(developmentCards)
                    .filter( developmentCard -> LEVEL1.equals(developmentCard.getLevel()))
                    .collect(Collectors.toList());

            board[2][0] = new DevelopmentCardsDeck(level1.stream().filter( developmentCard -> GREEN.equals(developmentCard.getColor())).collect(Collectors.toList()));
            board[2][1] = new DevelopmentCardsDeck(level1.stream().filter( developmentCard -> BLUE.equals(developmentCard.getColor())).collect(Collectors.toList()));
            board[2][2] = new DevelopmentCardsDeck(level1.stream().filter( developmentCard -> YELLOW.equals(developmentCard.getColor())).collect(Collectors.toList()));
            board[2][3] = new DevelopmentCardsDeck(level1.stream().filter( developmentCard -> PURPLE.equals(developmentCard.getColor())).collect(Collectors.toList()));

            level2 = Arrays.stream(developmentCards)
                    .filter( developmentCard -> LEVEL2.equals(developmentCard.getLevel()))
                    .collect(Collectors.toList());

            board[1][0] = new DevelopmentCardsDeck(level2.stream().filter( developmentCard -> GREEN.equals(developmentCard.getColor())).collect(Collectors.toList()));
            board[1][1] = new DevelopmentCardsDeck(level2.stream().filter( developmentCard -> BLUE.equals(developmentCard.getColor())).collect(Collectors.toList()));
            board[1][2] = new DevelopmentCardsDeck(level2.stream().filter( developmentCard -> YELLOW.equals(developmentCard.getColor())).collect(Collectors.toList()));
            board[1][3] = new DevelopmentCardsDeck(level2.stream().filter( developmentCard -> PURPLE.equals(developmentCard.getColor())).collect(Collectors.toList()));

            level3 = Arrays.stream(developmentCards)
                    .filter( developmentCard -> LEVEL3.equals(developmentCard.getLevel()))
                    .collect(Collectors.toList());

            board[0][0] = new DevelopmentCardsDeck(level3.stream().filter( developmentCard -> GREEN.equals(developmentCard.getColor())).collect(Collectors.toList()));
            board[0][1] = new DevelopmentCardsDeck(level3.stream().filter( developmentCard -> BLUE.equals(developmentCard.getColor())).collect(Collectors.toList()));
            board[0][2] = new DevelopmentCardsDeck(level3.stream().filter( developmentCard -> YELLOW.equals(developmentCard.getColor())).collect(Collectors.toList()));
            board[0][3] = new DevelopmentCardsDeck(level3.stream().filter( developmentCard -> PURPLE.equals(developmentCard.getColor())).collect(Collectors.toList()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
}