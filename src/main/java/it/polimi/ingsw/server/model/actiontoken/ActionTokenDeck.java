package it.polimi.ingsw.server.model.actiontoken;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.model.ChangesHandler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ActionTokenDeck {
    private final Stack<ActionToken> currentDeck;
    private transient ChangesHandler changesHandler;

    private ActionTokenDeck() {
        this.currentDeck = new Stack<>();
    }

    public void init(ChangesHandler changesHandler) {
        this.changesHandler = changesHandler;
    }

    /**
     * Returns a List containing the deck of ActionTokens specified in a resource file.
     * @return ActionToken list without a meaningful order.
     */
    public static List<ActionToken> getFullDeck() {
        JsonReader reader = null;
        ActionToken[] actionTokens = null;
        try {
            reader = new JsonReader(new FileReader("src/main/resources/default/game/ActionTokenDeck.json"));
            actionTokens =
                    new GsonBuilder().setPrettyPrinting().serializeNulls().create()
                            .fromJson(reader, ActionToken.class.arrayType());
            reader.close();
        } catch (IOException e) {
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
        ActionToken actionToken = currentDeck.pop();
        changesHandler.writeActionTokenDeck(this);
        return actionToken;
    }


    //todo: is this needed?
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
        shuffle();
        changesHandler.writeActionTokenDeck(this);
    }

    public void shuffle() {
        Collections.shuffle(currentDeck);
    }
}