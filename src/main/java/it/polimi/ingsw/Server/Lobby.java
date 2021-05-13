package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

    public synchronized void addPlayer(String nickname, Connection connection){
        players.put(nickname, connection);
    }

    public boolean isFull(){
        return players.size() == size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void startGame(){
        Game game = null;
        try {
            game = new Game(maxGameId, List.copyOf(players.keySet()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Controller controller = new Controller(game);
        RemoteView remoteView = new RemoteView(game);
        //todo: add gameIdRemoteView.put, do we really need it?
        for (String s: players.keySet()) {
            game.connect(s);
        }

        for (Connection c: players.values()) {
            c.subscribeRemoteView(remoteView);
        }

        this.maxGameId++;

        try {
            FileWriter file = new FileWriter("src/main/resources/maxId");
            file.write(maxGameId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        clear();
    }

    private void clear(){
        instance = null;
        players = new HashMap<>();
        size = 0;
    }

    public List<String> getPlayers() {
        return List.copyOf(players.keySet());
    }

    public boolean isEmpty(){
        return size == 0;
    }
}
