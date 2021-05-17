package it.polimi.ingsw.server.model.actiontoken;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ActionTokenDeck {
    private final Stack<ActionToken> currentDeck;

    public ActionTokenDeck() {
        this.currentDeck = new Stack<>();
        reset();
    }

    /**
     * Returns a List containing the deck of ActionTokens specified in a resource file.
     * @return ActionToken list without a meaningful order.
     */
    public static List<ActionToken> getFullDeck() {
        ActionToken[] actionTokens = new ActionToken[7];
        try {
            JsonReader reader = new JsonReader(new FileReader("src/main/resources/ActionTokenFullDeck.json"));
            actionTokens = new Gson().fromJson(reader, ActionToken[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(Arrays.asList(actionTokens));
    }

    /**
     * Returns a copy of the current ActionTokenDeck state. ActionTokens are in reverse
     * order compared to the stack pop() calls: the ActionToken in the List's last position
     * is the first to get picked on a pop() call on the deck.
     * @return ordered List of ActionTokens representing the current deck state.
     */
    public List<ActionToken> getCurrentDeck() {
        return new ArrayList<>(currentDeck);
    }

    /**
     * Removes the ActionToken at the top of the deck (Stack) and returns it.
     * @return top-most ActionToken, removed from deck.
     */
    public ActionToken pop() {
        return currentDeck.pop();
    }

    /**
     * Clears the current deck and re-initializes it. ActionTokens
     * will be loaded in a random order.
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