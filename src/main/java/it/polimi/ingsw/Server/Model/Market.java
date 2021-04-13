package it.polimi.ingsw.server.model;

import java.util.*;
import java.util.stream.Collectors;



public class Market {

    private final MarketMarble[][] market;
    private MarketMarble extraMarble;

    /**
     * sets the market and the extra marble passed to it
     * than it shuffles them
     */
    public Market() {
        //todo: read from file
        Map<MarketMarble, Integer> marbles = new HashMap<>();
        market = new MarketMarble[3][4];
        Arrays.stream(market).forEach(
                marketMarbles -> Arrays.stream(marketMarbles).forEach(
                        marketMarble -> marketMarble=pickMarble(marbles)
                )
        );
        extraMarble = pickMarble(marbles);
    }

    private MarketMarble pickMarble(Map<MarketMarble, Integer> marbles){
        Random random = new Random();
        List<MarketMarble> keys = new ArrayList<>(marbles.keySet());

        MarketMarble randomKey = keys.get(random.nextInt(keys.size()));
        if (marbles.get(randomKey)>0){
            marbles.put(randomKey,marbles.get(randomKey)-1);
            return randomKey;
        }
        else{
            marbles.remove(randomKey);
            return pickMarble(marbles);
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
        MarketMarble[] marketColumn = (MarketMarble[]) Arrays.stream(market).map(
                marketMarbles -> marketMarbles[column]
        ).toArray(); //Kunal thinks it works

        Map<MarketMarble, Integer> marbles = Arrays.stream(marketColumn).collect(
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

}