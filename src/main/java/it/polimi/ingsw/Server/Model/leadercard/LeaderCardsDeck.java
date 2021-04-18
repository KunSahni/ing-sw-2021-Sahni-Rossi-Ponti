package it.polimi.ingsw.server.model.leadercard;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


public class LeaderCardsDeck {

    private Stack<LeaderCard> deck = new Stack<>();

    public LeaderCardsDeck() {
        loadLeaderCards();
    }

    public Stack<LeaderCard> getDeck() {
        return deck;
    }


    /**
     * creates and returns the deck for a player popping four cards from the main deck
     * @return supp that contains the deck of a player
     */
    public List<LeaderCard> popFour() {
        List<LeaderCard> supp = new ArrayList<>();

        for(int i=0; i<4; i++){
            supp.add(deck.pop());
        }
        return supp;
    }

    /**
     * loads the leader cards from a json file
     */
    public void loadLeaderCards() {
        deck.clear();

        //upload of ConvertLeaderCard
        try {
            JsonReader reader = new JsonReader(new FileReader(new File("src/main/resources/ConvertLeaderCards.json")));
            ConvertLeaderCard[] convertLeaderCards = new Gson().fromJson(reader, ConvertLeaderCard[].class);
            for (ConvertLeaderCard c: convertLeaderCards) {
                deck.push(c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //upload of DiscountLeaderCard
        try {
            JsonReader reader = new JsonReader(new FileReader(new File("src/main/resources/DiscountLeaderCards.json")));
            DiscountLeaderCard[] discountLeaderCards = new Gson().fromJson(reader, DiscountLeaderCard[].class);
            for (DiscountLeaderCard d: discountLeaderCards) {
                deck.push(d);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //upload of ProduceLeaderCard
        try {
            JsonReader reader = new JsonReader(new FileReader(new File("src/main/resources/ProduceLeaderCards.json")));
            ProduceLeaderCard[] produceLeaderCards = new Gson().fromJson(reader, ProduceLeaderCard[].class);
            for (ProduceLeaderCard p: produceLeaderCards) {
                deck.push(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //upload of StoreLeaderCard
        try {
            JsonReader reader = new JsonReader(new FileReader(new File("src/main/resources/StoreLeaderCards.json")));
            StoreLeaderCard[] storeLeaderCards = new Gson().fromJson(reader, StoreLeaderCard[].class);
            for (StoreLeaderCard s: storeLeaderCards) {
                deck.push(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Collections.shuffle(deck);
    }

}