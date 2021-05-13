package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.market.MarketMarble;

import java.util.ArrayList;
import java.util.List;

/**
 *  This class contains an updated version of the Market which will be saved in the local DumbModel
 */
public class MarketUpdate extends BroadcastRenderable {
    private final MarketMarble[][] updatedMarket;
    private final MarketMarble updatedExtraMarble;

    public MarketUpdate(Market updatedMarket) {
        this.updatedMarket = updatedMarket.getMarblesLayout();
        this.updatedExtraMarble = updatedMarket.getExtraMarble();
    }

    @Override
    public void render(UI ui) {
        ui.updateMarket(updatedMarket, updatedExtraMarble);
    }
}
