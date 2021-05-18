package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.market.MarketMarble;

import java.util.List;
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
        market = new MarketMarble[3][4];
        extraMarble = null;
        dumbMarket = this;
    }

    /**
     * @param updatedMarket an unpdated layout of the market
     */
    public void updateMarket(MarketMarble[][] updatedMarket) {
        IntStream.range(0, 3).forEach(
                i -> IntStream.range(0, 4).forEach(
                        j-> market[i][j] = updatedMarket[i][j]
                )
        );
    }

    public void updateExtraMarble(MarketMarble updatedExtraMarble) {
        this.extraMarble = updatedExtraMarble;
    }

    public MarketMarble[][] getMarket() {
        return market;
    }

    public MarketMarble getExtraMarble() {
        return extraMarble;
    }
}
