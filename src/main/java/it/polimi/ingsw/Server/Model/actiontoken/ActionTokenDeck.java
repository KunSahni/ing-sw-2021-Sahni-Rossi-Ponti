package it.polimi.ingsw.server.model.actiontoken;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ActionTokenDeck {

    private final Stack<ActionToken> currentDeck;

    public ActionTokenDeck() {
        this.currentDeck = new Stack<>();
        reset();
    }

    public List<ActionToken> getFullDeck() {
        ActionToken[] actionTokens = new ActionToken[7];
        List<ActionToken> actionTokenFullDeck = new ArrayList<>();
        try {
            JsonReader reader = new JsonReader(new FileReader(new File("src/main/resources/ActionTokenFullDeck.json")));
            actionTokens = new Gson().fromJson(reader, ActionToken[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        actionTokenFullDeck.addAll(Arrays.asList(actionTokens));
        return actionTokenFullDeck;
    }
    public Stack<ActionToken> getCurrentDeck() {
        return currentDeck;
    }


    public ActionToken pop() {
        return currentDeck.pop();
    }

    /**
     * reset the deck at the beginning of the game and when MOVEANDSHUFFLE token is taken
     */
    public void reset() {

        currentDeck.clear();
        //place the full deck into current deck
        for (ActionToken actionToken: getFullDeck()) {
            currentDeck.push(actionToken);
        }
        //shuffle the deck
        Collections.shuffle(currentDeck);
    }
}