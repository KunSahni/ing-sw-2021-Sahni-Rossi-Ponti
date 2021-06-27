package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.ChangesHandler;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


public class Market {
    private transient ChangesHandler changesHandler;
    private final MarketMarble[][] market;
    private MarketMarble extraMarble;

    private Market() {
        this.market = new MarketMarble[3][4];
        extraMarble = null;
    }

    public void init(ChangesHandler changesHandler) {
        this.changesHandler = changesHandler;
    }

    public void shuffle() {
        List<MarketMarble> rawMarbles = convertMarketToList();
        Collections.shuffle(rawMarbles);
        extraMarble = rawMarbles.remove(0);
        for (int line = 0; line < 3; line++) {
            for (int column = 0; column < 4; column++) {
                this.market[line][column] = rawMarbles.remove(0);
            }
        }
        changesHandler.writeMarket(this);
    }

    /**
     * create a map with the resources in the chosen line and moves the market marbles
     *
     * @param row is the row chosen by the player
     * @return a map with the Market Marble in the chosen line
     */


    public Map<MarketMarble, Integer> chooseRow(int row) {
        Map<MarketMarble, Integer> marbles = Arrays.stream(market[row]).collect(
                Collectors.toMap(s -> s, s -> 1, Integer::sum)
        );
        moveLeft(row);
        changesHandler.writeMarket(this);
        return marbles;
    }

    private void moveLeft(int row) {
        MarketMarble supp = market[row][0];
        for (int i = 0; i < 3; i++)
            market[row][i] = market[row][i + 1];
        market[row][3] = extraMarble;
        extraMarble = supp;
    }

    /**
     * create a map with the resources in the chosen line and moves the market marbles
     *
     * @param column is the column chosen by the player
     * @return a map with the Market Marble in the chosen line
     */


    public Map<MarketMarble, Integer> chooseColumn(int column) {
        MarketMarble[] marketMarbles = new MarketMarble[3];
        for (int i = 0; i < 3; i++) {
            marketMarbles[i] = market[i][column];
        }

        Map<MarketMarble, Integer> marbles = Arrays.stream(marketMarbles).collect(
                Collectors.toMap(s -> s, s -> 1, Integer::sum)
        );
        moveUp(column);
        changesHandler.writeMarket(this);
        return marbles;
    }

    private void moveUp(int column) {
        MarketMarble supp = market[0][column];
        for (int i = 0; i < 2; i++)
            market[i][column] = market[i + 1][column];
        market[2][column] = extraMarble;
        extraMarble = supp;
    }

    public MarketMarble getExtraMarble() {
        return extraMarble;
    }

    /**
     * Returns the current MarketMarble layout.
     * The multi-dimensional array is arranged in a way that, on insertion, the ExtraMarble will
     * be placed in row 2 (when a column is selected) or column 3 (when a row is selected) of the
     * matrix.
     * Every MarketMarble gets moved in the previous index of the column/row.
     * The MarketMarble in position 0 of the column/row gets popped out of the matrix and becomes
     * ExtraMarble.
     *
     * @return two dimensional MarketMarble array.
     */
    public MarketMarble[][] getMarblesLayout() {
        return Arrays.stream(market).map(MarketMarble[]::clone).toArray($ -> market.clone());
    }

    private List<MarketMarble> convertMarketToList() {
        List<MarketMarble> marbleList = new ArrayList<>();
        marbleList.add(extraMarble);
        marbleList.addAll(Arrays.stream(market)
                .flatMap(Arrays::stream)
                .collect(toList()));
        return marbleList;
    }
}