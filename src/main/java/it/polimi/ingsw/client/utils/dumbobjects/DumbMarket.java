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


    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    public String formatPrintableStringAt(int x, int y) {
        return   "\033[" + x + ";" + y + "H╔═══════════╗"
               + "\033[" + (x+1) + ";" + y + "H║ ╔═══════╗ ║"
               + "\033[" + (x+1) + ";" + y + "H║ ║" + market[0][0].formatPrintableString() + " " + market[0][1].formatPrintableString() + " " + market[0][2].formatPrintableString() + " " + market[0][3].formatPrintableString() + "║" + extraMarble.formatPrintableString() + "║"
               + "\033[" + (x+2) + ";" + y + "H║ ║" + market[1][0].formatPrintableString() + " " + market[1][1].formatPrintableString() + " " + market[1][2].formatPrintableString() + " " + market[1][3].formatPrintableString() + "║ ║"
               + "\033[" + (x+3) + ";" + y + "H║ ║" + market[2][0].formatPrintableString() + " " + market[2][1].formatPrintableString() + " " + market[2][2].formatPrintableString() + " " + market[2][3].formatPrintableString() + "║ ║"
               + "\033[" + (x+4) + ";" + y + "H║ ╚═══════╝ ║"
               + "\033[" + (x+5) + ";" + y + "H╚═══════════╝";
    }
}
