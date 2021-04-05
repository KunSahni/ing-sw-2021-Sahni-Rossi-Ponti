package it.polimi.ingsw.server.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import static it.polimi.ingsw.server.model.Color.*;
import static it.polimi.ingsw.server.model.Level.*;

public class DevelopmentCardsBoard {

    private DevelopmentCardsDeck board[][] = new DevelopmentCardsDeck[4][3];

    /**
    * @param level 
    * @param color
    * @return and pick the first deck development card of color and level specified
    */


    public DevelopmentCard pick(Level level, Color color) {
        int line = 0;
        int column = 0;
        switch (level) {
            case LEVEL1:
                line = 2;
                break;
            case LEVEL2:
                line = 1;
                break;
            case LEVEL3:
                line = 0;
                break;
        }
        switch (color) {
            case GREEN:
                column = 0;
                break;
            case BLUE:
                column = 1;
                break;
            case YELLOW:
                column = 2;
                break;
            case PURPLE:
                column = 3;
                break;
        }
        return board[line][column].pop();
    }
    /**
    * Loads the cards from an XML file and creates the related objects.
     * Then it creates all the needed decks by calling the constructor and passing them the cards they need.
    */


    public void loadDevelopmentCards() {
        Gson gson = new Gson();
        List<DevelopmentCard> level1 = new ArrayList<>();
        List<DevelopmentCard> level2 = new ArrayList<>();
        List<DevelopmentCard> level3 = new ArrayList<>();

        try {
            JsonReader reader = new JsonReader(new FileReader("filename")); //todo : add the right json file name
            DevelopmentCard[] developmentCards = new Gson().fromJson(reader, DevelopmentCard.class);

            level1 = Arrays.stream(developmentCards)
                    .filter( developmentCard -> LEVEL1.equals(developmentCard.getLevel()))
                    .collect(Collectors.toList());

            board[2][0] = new DevelopmentCardsDeck(level1.stream().filter( developmentCard -> GREEN.equals(developmentCard.getType())).collect(Collectors.toList()));
            board[2][1] = new DevelopmentCardsDeck(level1.stream().filter( developmentCard -> BLUE.equals(developmentCard.getType())).collect(Collectors.toList()));
            board[2][2] = new DevelopmentCardsDeck(level1.stream().filter( developmentCard -> YELLOW.equals(developmentCard.getType())).collect(Collectors.toList()));
            board[2][3] = new DevelopmentCardsDeck(level1.stream().filter( developmentCard -> PURPLE.equals(developmentCard.getType())).collect(Collectors.toList()));

            level2 = Arrays.stream(developmentCards)
                    .filter( developmentCard -> LEVEL2.equals(developmentCard.getLevel()))
                    .collect(Collectors.toList());

            board[1][0] = new DevelopmentCardsDeck(level2.stream().filter( developmentCard -> GREEN.equals(developmentCard.getType())).collect(Collectors.toList()));
            board[1][1] = new DevelopmentCardsDeck(level2.stream().filter( developmentCard -> BLUE.equals(developmentCard.getType())).collect(Collectors.toList()));
            board[1][2] = new DevelopmentCardsDeck(level2.stream().filter( developmentCard -> YELLOW.equals(developmentCard.getType())).collect(Collectors.toList()));
            board[1][3] = new DevelopmentCardsDeck(level2.stream().filter( developmentCard -> PURPLE.equals(developmentCard.getType())).collect(Collectors.toList()));

            level3 = Arrays.stream(developmentCards)
                    .filter( developmentCard -> LEVEL3.equals(developmentCard.getLevel()))
                    .collect(Collectors.toList());

            board[0][0] = new DevelopmentCardsDeck(level3.stream().filter( developmentCard -> GREEN.equals(developmentCard.getType())).collect(Collectors.toList()));
            board[0][1] = new DevelopmentCardsDeck(level3.stream().filter( developmentCard -> BLUE.equals(developmentCard.getType())).collect(Collectors.toList()));
            board[0][2] = new DevelopmentCardsDeck(level3.stream().filter( developmentCard -> YELLOW.equals(developmentCard.getType())).collect(Collectors.toList()));
            board[0][3] = new DevelopmentCardsDeck(level3.stream().filter( developmentCard -> PURPLE.equals(developmentCard.getType())).collect(Collectors.toList()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
    * @param level 
    * @param color
     * @return the first deck development card of color and level specified not picking it
    */


    public DevelopmentCard peek(Level level, Color color) {
        int line = 0;
        int column = 0;
        switch (level) {
            case LEVEL1:
                line = 2;
                break;
            case LEVEL2:
                line = 1;
                break;
            case LEVEL3:
                line = 0;
                break;
        }
        switch (color) {
            case GREEN:
                column = 0;
                break;
            case BLUE:
                column = 1;
                break;
            case YELLOW:
                column = 2;
                break;
            case PURPLE:
                column = 3;
                break;
        }
        return board[line][column].peek();

    }

    public DevelopmentCardsDeck[][] peekBoard() {
        return board;
    }
    /**
     * discard two card from the board in single player games when the action token discard is picked
    * @param color
    */


    public void discardTwo(Color color) {
        int i = 0; //counts how many cards have been discarded

        //collect in a list the deck of development cards level 1 and color the parameter color
        List<DevelopmentCardsDeck> dcd = Arrays.stream(board[2])
                .filter(developmentCardsDeck -> color
                .equals(developmentCardsDeck.peek().getType()))
                .collect(Collectors.toList());
        //if there are elements in the deck discard two of them
        for (DevelopmentCardsDeck d: dcd) {
            if (i == 2){return;}
            if (d.peek()==null){}
            else{
                d.pop();
                i++;
            }
        }

        dcd = Arrays.stream(board[1])
                .filter(developmentCardsDeck -> color
                .equals(developmentCardsDeck.peek().getType()))
                .collect(Collectors.toList());

        for (DevelopmentCardsDeck d: dcd) {
            if (i == 2){return;}
            if (d.peek()==null){}
            else{
                d.pop();
                i++;
            }
        }

        dcd = Arrays.stream(board[0])
                .filter(developmentCardsDeck -> color
                        .equals(developmentCardsDeck.peek().getType()))
                .collect(Collectors.toList());

        for (DevelopmentCardsDeck d: dcd) {
            if (i == 2){return;}
            if (d.peek()==null){}
            else{
                d.pop();
                i++;
            }
        }
    }
}