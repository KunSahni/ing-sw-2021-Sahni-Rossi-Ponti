package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a Singleton that represents the Game Lobby
 */
public class Lobby {
    private static Lobby instance = null;
    private Map<String, Connection> players;
    private int size;
    private int maxGameId;

    private Lobby(){
        players = new HashMap<>();
        size = 0;
        try {
            maxGameId = new Gson().fromJson(new JsonReader(new FileReader("src/main/resources/maxId")), Integer.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Lobby getInstance(){
        if (instance == null){
            instance = new Lobby();
        }
        return instance;
    }

    /**
     * add a player to server players map
     * @param nickname is the nickname of the player
     * @param connection is the connection of the player
     */
    public synchronized void addPlayer(String nickname, Connection connection){
        players.put(nickname, connection);
    }

    /**
     * control if number of players in Lobby equals Lobby size
     * @return true if Lobby is full, otherwise false
     */
    public boolean isFull(){
        return players.size() == size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Set instance null, players to new Map and size to zero
     */
    private void clear(){
        instance = null;
        players = new HashMap<>();
        size = 0;
    }

    public List<String> getPlayers() {
        return List.copyOf(players.keySet());
    }

    /**
     * @return true only if Lobby' size is zero
     */
    public boolean isEmpty(){
        return size == 0;
    }
}
