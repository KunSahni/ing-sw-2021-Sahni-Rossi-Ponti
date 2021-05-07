package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.utils.ChangesHandler;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;


public class Market {

    private final MarketMarble[][] market;
    private MarketMarble extraMarble;

    public Market(ChangesHandler changesHandler) throws FileNotFoundException {
        List<MarketMarble> rawMarket = changesHandler.readMarket();
        this.market = new MarketMarble[3][4];
        this.extraMarble = rawMarket.remove(0);
        int index = 0;
        for (int line = 0; line < 3; line++) {
            for (int column = 0; column < 4; column++) {
                this.market[line][column] = rawMarket.get(index);
                index++;
            }
        }
    }


    /**
     * create a map with the resources in the chosen line and moves the market marbles
     * @return a map with the Market Marble in the chosen line
    * @param row is the row chosen by the player
    */


    public Map<MarketMarble, Integer> chooseRow(int row) {
        Map<MarketMarble, Integer> marbles = Arrays.stream(market[row]).collect(
                Collectors.toMap(s -> s, s -> 1, Integer::sum)
        );
        moveLeft(row);
        return marbles;
    }

    private void moveLeft (int row){
        MarketMarble supp = market[row][0];
        for(int i=0; i<3; i++)
            market[row][i]=market[row][i+1];
        market[row][3]=extraMarble;
        extraMarble=supp;
    }

    /**
     * create a map with the resources in the chosen line and moves the market marbles
     * @return a map with the Market Marble in the chosen line
    * @param column is the column chosen by the player
    */


    public Map<MarketMarble, Integer> chooseColumn(int column) {
        MarketMarble[] marketMarbles = new MarketMarble[3];
        for(int i=0; i<3; i++){
            marketMarbles[i]=market[i][column];
        }

        Map<MarketMarble, Integer> marbles = Arrays.stream(marketMarbles).collect(
                Collectors.toMap(s -> s, s -> 1, Integer::sum)
        );
        moveUp(column);
        return marbles;
    }

    private void moveUp (int column){
        MarketMarble supp = market[0][column];
        for(int i=0; i<2; i++)
            market[i][column]=market[i+1][column];
        market[2][column]=extraMarble;
        extraMarble=supp;
    }

    public MarketMarble getExtraMarble() {
        return extraMarble;
    }

    /**
     * Returns the current MarketMarble layout.
     * The multi-dimensional array is arranged in a way that, on insertion, the ExtraMarble will be placed in row 2 (when a column is selected) or column 3 (when a row is selected) of the matrix.
     * Every MarketMarble gets moved in the previous index of the column/row.
     * The MarketMarble in position 0 of the column/row gets popped out of the matrix and becomes ExtraMarble.
     * @return two dimensional MarketMarble array.
     */
    public MarketMarble[][] getMarblesLayout(){
        return Arrays.stream(market).map(MarketMarble[]::clone).toArray($ -> market.clone());
    }
}