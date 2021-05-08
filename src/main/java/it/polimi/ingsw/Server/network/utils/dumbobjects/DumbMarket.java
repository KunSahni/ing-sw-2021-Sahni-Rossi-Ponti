package it.polimi.ingsw.server.network.utils.dumbobjects;

import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.market.MarketMarble;

/**
 * This is a dumber version of a regular Market,
 * this class only contains data contained in a Market but has none of its logic.
 */
public class DumbMarket {
    private MarketMarble[][] market;
    private MarketMarble extraMarble;

    public DumbMarket(Market market) {
        this.market = market.getMarblesLayout();
        this.extraMarble = market.getExtraMarble();
    }

    public MarketMarble[][] getMarket() {
        return market;
    }

    public MarketMarble getExtraMarble() {
        return extraMarble;
    }
}
