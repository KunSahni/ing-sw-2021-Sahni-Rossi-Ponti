package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.remoteview.RemoteView;
import it.polimi.ingsw.server.connection.state.PlayingState;
import it.polimi.ingsw.server.connection.state.WaitingForGameSizeState;

import java.io.*;
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
            maxGameId = new Gson().fromJson(new JsonReader(new FileReader("src/main/resources/maxId.json")), Integer.class);
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
    public void addPlayer(String nickname, Connection connection){
        connection.setNickname(nickname);
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
     * create a new game with maxGameId as id and all players in Lobby, than create a new controller, a new remote view,
     * connect all players in Lobby to the game, subscribe remote view to all connections.
     * It also sets maxGameId as gameId to all connections and add to server currentGames map game just created.
     * Finally increase maxGameId, overwrite it in memory and call clear method.
     */
    public void startGame(){

        try {
            Writer writer = new FileWriter("src/main/resources/maxId.json");
            new Gson().toJson(maxGameId+1, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Game game = null;
        try {
            game = new Game(maxGameId, List.copyOf(players.keySet()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Controller controller = new Controller(game);
        RemoteView remoteView = new RemoteView(controller);
        controller.setRemoteView(remoteView);
        game.subscribe(remoteView);
        for (String s: players.keySet()) {
            controller.connectPlayer(s, players.get(s));
        }

        for (Connection c: players.values()) {
            c.setGameId(maxGameId);
            c.addCurrentGame(maxGameId, game);
            c.getServer().addGameIDRemoteView(maxGameId, controller);
        }

        this.maxGameId++;

        clear();
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

    public synchronized boolean checkNicknameAvailability(String nickname, Connection connection){
        if(players.containsKey(nickname)){
            return false;
        }
        else{
            addPlayer(nickname, connection);
            if (isEmpty()){
                connection.setState(new WaitingForGameSizeState(connection));
                connection.askForSize();
            }

            connection.setState(new PlayingState(connection));

            connection.sendJoinLobbyNotification(size);

            if (isFull()) {
                startGame();
            } else {
                connection.waitForPlayers();
            }
            return true;
        }
    }
}
