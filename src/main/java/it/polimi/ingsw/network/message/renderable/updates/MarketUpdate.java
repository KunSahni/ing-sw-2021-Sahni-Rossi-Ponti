package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.market.MarketMarble;

import java.util.ArrayList;
import java.util.List;

/**
 *  This class contains an updated version of the Market which will be saved in the local DumbModel
 */
public class MarketUpdate extends BroadcastRenderable {
    private final List<MarketMarble> updatedMarket;

    public MarketUpdate(List<MarketMarble> updatedMarket) {
        this.updatedMarket = updatedMarket;
    }

    @Override
    public void render(UI ui) {
        ui.updateMarket(updatedMarket);
    }
}
