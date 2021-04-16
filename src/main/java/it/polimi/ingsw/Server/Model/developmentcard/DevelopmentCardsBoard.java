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


    public DevelopmentCardsBoard() {
        board = new DevelopmentCardsDeck[3][4];
        loadDevelopmentCards();
    }

    /**
    * @return and pick the first deck development card of color and level specified
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
        return board;
    }
    /**
     * discard two card from the board in single player games when the action token discard is picked
    */


    public void discardTwo(Color color) {
        int i = 0; //counts how many cards have been discarded

        //collect in a list the deck of development cards level 1 and color the parameter color
        List<DevelopmentCardsDeck> dcd = Arrays.stream(board[2])
                .filter(developmentCardsDeck -> color
                .equals(developmentCardsDeck.peek().getColor()))
                .collect(Collectors.toList());
        //if there are elements in the deck discard two of them
        for (DevelopmentCardsDeck d: dcd) {
            if (i == 2){return;}
            if (d.peek()!=null){
                d.pop();
                i++;
            }
        }

        dcd = Arrays.stream(board[1])
                .filter(developmentCardsDeck -> color
                .equals(developmentCardsDeck.peek().getColor()))
                .collect(Collectors.toList());

        for (DevelopmentCardsDeck d: dcd) {
            if (i == 2){return;}
            if (d.peek()!=null){
                d.pop();
                i++;
            }
        }

        dcd = Arrays.stream(board[0])
                .filter(developmentCardsDeck -> color
                        .equals(developmentCardsDeck.peek().getColor()))
                .collect(Collectors.toList());

        for (DevelopmentCardsDeck d: dcd) {
            if (i == 2){return;}
            if (d.peek()!=null){
                d.pop();
                i++;
            }
        }
    }
}