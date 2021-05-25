package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.actiontoken.ActionToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a dumber version of a regular ActionTokenDeck,
 * this class only contains data contained in a ActionTokenDeck but has none of its logic.
 */
public class DumbActionTokenDeck implements Serializable {
    private List<ActionToken> actionTokens;
    private static DumbActionTokenDeck actionTokenDeck;

    public static DumbActionTokenDeck getInstance(){
        if(actionTokenDeck!=null)
            return actionTokenDeck;
        return new DumbActionTokenDeck();
    }

    private DumbActionTokenDeck() {actionTokens = new ArrayList<>();
    }

    public void updateActionTokens(List<ActionToken> actionTokens) {
        this.actionTokens = actionTokens;
    }

    public List<ActionToken> getActionTokens() {
        return actionTokens;
    }
}
