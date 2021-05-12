package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;
import it.polimi.ingsw.server.model.market.MarketMarble;

import java.util.List;

/**
 * This class contains an update regarding the market marbles which were just chosen by the client
 */
public class TempMarblesUpdate extends PrivateRenderable {
    private final List<MarketMarble> updateMarbles;

    public TempMarblesUpdate(String nickname, List<MarketMarble> updateMarbles) {
        super(nickname);
        this.updateMarbles = updateMarbles;
    }

    @Override
    public void render(UI ui) {
        ui.renderTempMarbles(updateMarbles);
    }
}
