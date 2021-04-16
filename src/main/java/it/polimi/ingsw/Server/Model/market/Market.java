package it.polimi.ingsw.server.model.market;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        market = new MarketMarble[3][4];
        int index=0;
        List<MarketMarble> marbles = new ArrayList<>();
        try {
            JsonReader reader = new JsonReader(new FileReader(new File("src/main/resources/Market.json")));
            marbles = new Gson().fromJson(reader, ArrayList.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Collections.shuffle(marbles);
        for(int line=0; line<3; line++){
            for(int column=0; column<4; column++){
                market[line][column] = marbles.remove(index);
                index++;
            }
        }
        extraMarble = marbles.remove(index);
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