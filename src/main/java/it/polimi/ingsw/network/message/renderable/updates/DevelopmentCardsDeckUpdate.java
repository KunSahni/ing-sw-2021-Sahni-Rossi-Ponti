package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsDeck;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;

public class DevelopmentCardsDeckUpdate extends BroadcastRenderable {
    private final DevelopmentCardsDeck uppdatedDevelopmentCardsDeck;

    public DevelopmentCardsDeckUpdate(DevelopmentCardsDeck uppdatedDevelopmentCardsDeck) {
        this.uppdatedDevelopmentCardsDeck = uppdatedDevelopmentCardsDeck;
    }

    @Override
    public void render(UI ui) {
        ui.updateDevelopmentCardsBoard(uppdatedDevelopmentCardsDeck);
    }
}
