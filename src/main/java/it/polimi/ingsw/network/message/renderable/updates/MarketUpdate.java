package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;

/**
 *  This class contains an updated version of the Market which will be saved in the local DumbModel
 */
public class MarketUpdate extends BroadcastRenderable {
    private final Market updatedMarket;

    public MarketUpdate() {
        this.updatedMarket = updatedMarket;
    }

    @Override
    public void render(UI ui) {
        ui.updateMarket(updatedMarket);
    }
}
