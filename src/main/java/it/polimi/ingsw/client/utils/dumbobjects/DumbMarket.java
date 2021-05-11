package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.market.MarketMarble;

import java.util.stream.IntStream;

/**
 * This is a dumber and singleton version of a regular Market,
 * this class only contains data contained in a Market but has none of its logic.
 */
public class DumbMarket {
    private MarketMarble[][] market;
    private MarketMarble extraMarble;
    private static DumbMarket dumbMarket;

    public static DumbMarket getInstance(){
        if(dumbMarket!=null)
            return dumbMarket;
        return new DumbMarket();
    }

    private DumbMarket() {
        market = new MarketMarble[4][3];
        extraMarble = null;
        dumbMarket = this;
    }

    /**
     * @param marketMarbles a flattened list of MarketMarbles in which the last element is the extra marble
     */
    public void updateMarket(MarketMarble[] marketMarbles) {
        IntStream.range(0, marketMarbles.length-1).forEach(
                i-> this.market[i/4][i%4] = marketMarbles[i]
        );
        this.extraMarble = marketMarbles[marketMarbles.length-1];
    }

    public MarketMarble[][] getMarket() {
        return market;
    }

    public MarketMarble getExtraMarble() {
        return extraMarble;
    }
}
