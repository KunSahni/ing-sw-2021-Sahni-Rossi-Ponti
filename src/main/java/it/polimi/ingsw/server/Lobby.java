package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.network.servertoclient.renderable.requests.GameStartedNotification;
import it.polimi.ingsw.network.servertoclient.renderable.requests.JoinedLobbyNotification;
import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.remoteview.RemoteView;
import it.polimi.ingsw.server.connection.state.PlayingState;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a Singleton that represents the Game Lobby
 */
public class Lobby {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private static Lobby instance = null;
    private Map<String, Connection> players;
    private int size;
    private int maxGameId;
    private Server server;

    private Lobby() {
        players = new HashMap<>();
        size = 0;
        try {
            JsonReader reader = new JsonReader(new FileReader(ChangesHandler.getWorkingDirectory() + "/server/maxID.json"));
            maxGameId = new Gson().fromJson(reader, int.class);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Lobby getInstance() {
        if (instance == null) {
            instance = new Lobby();
        }
        return instance;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * add nickname and connection to a map to trace players that will be part of the same game
     * @param nickname   is the nickname of the player
     * @param connection is the connection of the player
     */
    public synchronized void addPlayer(String nickname, Connection connection) {
        // logger.info("Player " + nickname + " is being added to the lobby.");
        players.put(nickname, connection);
        connection.send(new JoinedLobbyNotification(maxGameId, size));
        if (isFull()) {
            startGame();
        } else {
            connection.waitForPlayers();
        }
    }

    /**
     * control if number of players in Lobby equals Lobby size
     *
     * @return true if Lobby is full, otherwise false
     */
    public boolean isFull() {
        return players.size() == size;
    }

    public int getSize() {
        return size;
    }

    /**
     * set Lobby size and call a server method to start waiting threads
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
        server.wakeUpThread();
    }

    /**
     * create a new game with maxGameId as id and all players in Lobby, than create a new
     * controller, a new remote view,
     * connect all players in Lobby to the game, subscribe remote view to all connections.
     * It also sets maxGameId as gameId to all connections and add to server currentGames map
     * game just created.
     * Finally increase maxGameId, overwrite it in memory and call clear method.
     */
    public void startGame() {
        // logger.info("Launching game " + maxGameId);
        players.values().forEach(connection -> connection.send(new GameStartedNotification(size)));
        Game game = null;
        try {
            game = new Game(server, maxGameId, new ArrayList<>(players.keySet()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Controller controller = new Controller(game);
        RemoteView remoteView = new RemoteView(controller);
        controller.setRemoteView(remoteView);
        server.addGameToCurrentGames(maxGameId, game);
        server.addGameIdController(maxGameId, controller);
        players.forEach(
                (nickname, connection) -> {
                    controller.connectPlayer(nickname, connection);
                    connection.setGameId(maxGameId);
                    connection.setState(new PlayingState(connection));
                }
        );
        try {
            File rootDir = new File(ChangesHandler.getWorkingDirectory() + "/server");
            if(!rootDir.exists())
                rootDir.mkdirs();
            this.maxGameId++;
            Writer writer = new FileWriter(ChangesHandler.getWorkingDirectory() + "/server/maxID.json");
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            gson.toJson(maxGameId, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clear();
    }

    /**
     * Set instance null, players to new Map and size to zero
     */
    private void clear() {
        players = new HashMap<>();
        size = 0;
    }

    public List<String> getPlayers() {
        return List.copyOf(players.keySet());
    }

    /**
     * @return true only if Lobby' size is zero
     */
    public boolean isEmpty() {
        return size == 0 || size==-1;
    }

    public synchronized boolean nicknameAvailable(String nickname) {
        return !players.containsKey(nickname);
    }

    /**
     * remove a player from the Lobby
     * @param nickname is the nickname of the player that should be removed
     */
    public void removePlayer(String nickname) {
        players.remove(nickname);
        if (players.size() == 0) {
            size = 0;
        }
    }
}
