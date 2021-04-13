package it.polimi.ingsw.server.model;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.model.LeaderCard;


public class LeaderCardsDeck {

    public Stack<LeaderCard> deck;

    public Stack<LeaderCard> getDeck() {
        return deck;
    }

    /**
     * shuffles the main deck
     */

    private void shuffle() {
        List<LeaderCard> leaderCards = new ArrayList<>();
        //put all cards in the deck in an array
        for (LeaderCard l: deck) {
            leaderCards.add(deck.pop());
        }
        Collections.shuffle(leaderCards);

        //put all cards in the array in the deck
        for (LeaderCard l: leaderCards) {
            deck.push(l);
        }
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
        Gson gson = new Gson();

        //upload of ConvertLeaderCard
        try {
            JsonReader reader = new JsonReader(new FileReader("filewithconvertleadercard")); //todo : add the right json file name
            ConvertLeaderCard[] convertLeaderCards = new Gson().fromJson(reader, ConvertLeaderCard[].class);
            for (ConvertLeaderCard c: convertLeaderCards) {
                deck.push(c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //upload of DiscountLeaderCard
        try {
            JsonReader reader = new JsonReader(new FileReader("filewithdiscountledercard")); //todo : add the right json file name
            DiscountLeaderCard[] discountLeaderCards = new Gson().fromJson(reader, DiscountLeaderCard.class);
            for (DiscountLeaderCard d: discountLeaderCards) {
                deck.push(d);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //upload of ProduceLeaderCard
        try {
            JsonReader reader = new JsonReader(new FileReader("filewithproduceleadercard")); //todo : add the right json file name
            ProduceLeaderCard[] produceLeaderCards = new Gson().fromJson(reader, ProduceLeaderCard.class);
            for (ProduceLeaderCard p: produceLeaderCards) {
                deck.push(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //upload of StoreLeaderCard
        try {
            JsonReader reader = new JsonReader(new FileReader("filewithstoreleadercard")); //todo : add the right json file name
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