package it.polimi.ingsw.server.model;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


public class LeaderCardsDeck {

    private Stack<LeaderCard> deck;

    public Stack<LeaderCard> getDeck() {
        return deck;
    }

    /**
     * shuffles the main deck
     */

    private void shuffle() {
        Random random = ThreadLocalRandom.current();
        LeaderCard[] leaderCards = new LeaderCard[16];

        //place the deck into an array
        for(int i=0; i<16; i++){
            leaderCards[i]=deck.pop();
        }

        //shuffle the array
        for(int i=leaderCards.length-1; i>0; i-- ){
            int index = random.nextInt(i+1);
            LeaderCard a = leaderCards[index];
            leaderCards[index] = leaderCards[i];
            leaderCards[i] = a;
        }

        //load the array in the deck
        for (LeaderCard l: leaderCards) {
            deck.push(l);
        }
    }

    /**
     * creates and returns the deck for a player popping four cards from the main deck
     * @return supp that contains the deck of a player
     */
    public List<LeaderCard> popFour() {
        List supp = new ArrayList();

        for(int i=0; i<4; i++){
            supp.add(i,deck.pop());
        }
        return supp;
    }

    /**
     * loads the leader cards from an XML file
     */
    public void loadLeaderCards() {
        // write the leader card ability as first
        // after reading it choose which producer have to call
        deck.clear();
        Gson gson = new Gson();

        //upload of ConvertLeaderCard
        try {
            JsonReader reader = new JsonReader(new FileReader("filename")); //todo : add the right json file name
            ConvertLeaderCard[] convertLeaderCards = new Gson().fromJson(reader, ConvertLeaderCard.class);
            for (ConvertLeaderCard c: convertLeaderCards) {
                deck.push(c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //upload of DiscountLeaderCard
        try {
            JsonReader reader = new JsonReader(new FileReader("filename")); //todo : add the right json file name
            DiscountLeaderCard[] discountLeaderCards = new Gson().fromJson(reader, DiscountLeaderCard.class);
            for (DiscountLeaderCard d: discountLeaderCards) {
                deck.push(d);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //upload of ProduceLeaderCard
        try {
            JsonReader reader = new JsonReader(new FileReader("filename")); //todo : add the right json file name
            ProduceLeaderCard[] produceLeaderCards = new Gson().fromJson(reader, ProduceLeaderCard.class);
            for (ProduceLeaderCard p: produceLeaderCards) {
                deck.push(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //upload of StoreLeaderCard
        try {
            JsonReader reader = new JsonReader(new FileReader("filename")); //todo : add the right json file name
            StoreLeaderCard[] storeLeaderCards = new Gson().fromJson(reader, StoreLeaderCard.class);
            for (StoreLeaderCard s: storeLeaderCards) {
                deck.push(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        shuffle();
    }

}