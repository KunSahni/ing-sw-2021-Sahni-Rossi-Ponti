package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.actiontoken.ActionToken;

import java.util.List;

/**
 * This is a dumber version of a regular ActionTokenDeck,
 * this class only contains data contained in a ActionTokenDeck but has none of its logic.
 */
public class DumbActionTokenDeck {
    private List<ActionToken> actionTokens;

    public DumbActionTokenDeck() {
        super();
    }

    public void updateActionTokens(List<ActionToken> actionTokens) {
        this.actionTokens = actionTokens;
    }

    public List<ActionToken> getActionTokens() {
        return actionTokens;
    }
}
