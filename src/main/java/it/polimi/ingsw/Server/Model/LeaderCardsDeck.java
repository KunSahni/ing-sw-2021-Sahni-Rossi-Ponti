package it.polimi.ingsw.server.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.time.*;


import GamePackage.Game;
import jdk.internal.org.objectweb.asm.TypeReference;


public class LeaderCardsDeck {

    private Stack<LeaderCard> deck;


    private Game ;

    public Stack<LeaderCard> getDeck() {
        Stack<LeaderCard> d = new Stack<LeaderCard>();

        for (LeaderCard l: deck) {
            d.push(l);
        }
        return d;
    }

    public Game get() {
        return ;
    }

    public void setDeck(Stack<LeaderCard> deck) {
        for (LeaderCard l: deck) {
            this.deck.push(l);
        }
    }

    public void set(Game ) {
        this. = ;
    }



    /**
     * shuffles the main deck
     */
    private void shuffle() {
        LeaderCard sup[] = new LeaderCard()[4];
        int i = 0;
        int rand = 0;
        boolean b[] = new Boolean()[16];

        for(i=0; i<16; i++){
            sup[i]= deck.pop();
            b[i]=false;
        }

        for(i=0; i<16; i++){
            rand = random.nextInt(15);
            while(b[rand]==true){
                rand++;
                if (rand>15) rand=0;
            }
            deck.push(sup[rand]);
            b[rand]=true;
        }
    }

    /**
     * creates and returns the deck for a player popping four cards from the main deck
     * @return supp that contains the deck of a player
     */
    public List<LeaderCard> popFour() {
        List supp = new ArrayList();
        int i=0;

        for(i=0; i<4; i++){
            supp.add(i,deck.pop());
        }
        return supp;
    }

    /**
     * loads the leader cards from an XML file
     */
    public void loadLeaderCards() {
        deck.clear();
        try {
            ObjectMapper mapper = new XmlMapper();
            InputStream inputStream = new FileInputStream(new File("/poszionedelfilexml")); //METTERRE LA POSIZIONE DEL FILE GIUSTA
            TypeReference<List<LeaderCard>> typeReference = new TypeReference<List<LeaderCard>>() {
            };
            List<LeaderCard> leaderCards = mapper.readValue(inputStream, typeReference);
            for (Leadercard l : leaderCards) {
                deck.push(l);
            }
            shuffle();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}