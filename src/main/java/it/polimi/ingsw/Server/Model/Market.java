package it.polimi.ingsw.server.model;

import java.util.*;
import java.time.*;


import GamePackage.Game;
import PersonalBoardPackage.PersonalBoard;



public class Market {

    private MarketMarble[][] market[3][4];


    private MarketMarble extraMarble;


    private Game ;

    private PersonalBoard ;


    private PersonalBoard ;


    private ResourceBank ;


    private MarketMarble ;


    /**
     * sets the market and the extra marble passed to it
     * than it shuffles them
     */
    public void setMarket[3[4](MarketMarble[][] market[3][4], MarketMarble extraMarble) {
        MarketMarble[] supp = new MarketMarble()[13];
        boolean b[] = new Boolean()[13];
        int i=0;
        int j=0;
        int a=0;
        int rand=0;

        this.market[3][4] = market[3][4];
        this.extraMarble = extraMarble;

        for (i=0; i<3; i++){
            for (j=0; j<4; j++){
                supp[a]=market[i][j];
                b[a]=false;
                a++;
            }
        }
        supp[12]=extraMarble;

        for(i=0; i<3; i++){
            for(j=0; j<4; j++){
                rand = random.nextInt(12);
                while(b[rand]==true){
                    rand++;
                    if (rand>12) rand=0;
                }
                market[i][j]=supp[rand];
                b[rand]=true;
            }
        }
        i=0;
        while(b[i]==true) i++;
        this.extraMarble = supp[i];
    }

    public void set(Game ) {
        this. = ;
    }

    public void set(PersonalBoard ) {
        this. = ;
    }

    public void set(PersonalBoard ) {
        this. = ;
    }

    public void set(ResourceBank ) {
        this. = ;
    }

    public void set(MarketMarble ) {
        this. = ;
    }

    public void link(PersonalBoard _) {
        set(_);
    }


    /**
     * create a map with the resources in the chosen line and moves the market marbles
     * @return a map with the Market Marble in the chosen line
    * @param line
    */


    public Map<MarketMarble, Integer> chooseRow(int line) {
        Map<MarketMarble, Integer> resources = new HashMap<>();
        int white = 0;
        int grey = 0;
        int red = 0;
        int blue = 0;
        int yellow = 0;
        int purple = 0;
        int i = 0;
        MarketMarble supp = extraMarble;

        for(i=0;i<4;i++){
            switch (market[line][i]) {
                case "WHITE":
                    white++;
                    break;
                case "GREY":
                    grey++;
                    break;
                case "RED":
                    red++;
                    break;
                case "BLUE":
                    blue++;
                    break;
                case "YELLOW":
                    yellow++;
                    break;
                case "PURPLE":
                    purple++;
                    break;
            }
        }
        resources.put(WHITE, white);
        resources.put(GREY, grey);
        resources.put(RED, red);
        resources.put(BLUE, blue);
        resources.put(YELLOW, yellow);
        resources.put(PURPLE, purple);

        supp = extraMarble;
        extraMarble = market[line][0];
        market[line][0]=market[line][1];
        market[line][1]=market[line][2];
        market[line][2]=market[line][3];
        market[line][3]=supp;

        return resources;
    }
    /**
     * create a map with the resources in the chosen line and moves the market marbles
     * @return a map with the Market Marble in the chosen line
    * @param column
    */


    public Map<MarketMarble, Integer> chooseColumn(int column) {
        Map<MarketMarble, Integer> resources = new HashMap<>();
        int white = 0;
        int grey = 0;
        int red = 0;
        int blue = 0;
        int yellow = 0;
        int purple = 0;
        int i = 0;
        MarketMarble supp = extraMarble;

        for(i=0;i<3;i++){
            switch (market[i][column]) {
                case "WHITE":
                    white++;
                    break;
                case "GREY":
                    grey++;
                    break;
                case "RED":
                    red++;
                    break;
                case "BLUE":
                    blue++;
                    break;
                case "YELLOW":
                    yellow++;
                    break;
                case "PURPLE":
                    purple++;
                    break;
            }
        }
        resources.put(WHITE, white);
        resources.put(GREY, grey);
        resources.put(RED, red);
        resources.put(BLUE, blue);
        resources.put(YELLOW, yellow);
        resources.put(PURPLE, purple);

        supp = extraMarble;
        extraMarble = market[0][column];
        market[0][column]=market[1][column];
        market[1][column]=market[2][column];
        market[2][column]=supp;

        return resources;
    }

}