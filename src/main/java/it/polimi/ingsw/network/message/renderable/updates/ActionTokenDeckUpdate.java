package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains an updated version of the ActionTokenDeck which will be saved in the local DumbModel
 */
public class ActionTokenDeckUpdate extends BroadcastRenderable {
    private final List<ActionToken> updatedActionTokenDeck;

    public ActionTokenDeckUpdate(ActionTokenDeck updatedActionTokenDeck) {
        this.updatedActionTokenDeck = updatedActionTokenDeck.getCurrentDeck();
    }

    @Override
    public void render(UI ui) {
        ui.updateActionTokenDeck(updatedActionTokenDeck);
    }
}
