package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;

/**
 * This class contains an updated version of the ActionTokenDeck which will be saved in the local DumbModel
 */
public class ActionTokenDeckUpdate extends BroadcastRenderable {
    private final ActionTokenDeck updatedActionTokenDeck;

    public ActionTokenDeckUpdate(ActionTokenDeck updatedActionTokenDeck) {
        this.updatedActionTokenDeck = updatedActionTokenDeck;
    }

    @Override
    public void render(UI ui) {
        ui.renderActionTokenDeck(updatedActionTokenDeck);
    }
}
