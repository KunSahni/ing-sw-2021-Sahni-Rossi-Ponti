package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.network.message.renderable.requests.GameStartedNotification;
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
            maxGameId = new Gson().fromJson(new JsonReader(new FileReader("src/main/resources" +
                    "/json/maxId.json")), Integer.class);
        } catch (FileNotFoundException e) {
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
     * add a player to server players map
     *
     * @param nickname   is the nickname of the player
     * @param connection is the connection of the player
     */
    public synchronized void addPlayer(String nickname, Connection connection) {
        logger.log(Level.INFO, "Player " + nickname + " is being added to the lobby.");
        connection.setNickname(nickname);
        players.put(nickname, connection);
        connection.joinedLobby(size);
        if (isEmpty()) {
            connection.setState(new WaitingForGameSizeState(connection));
        }
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

    public void setSize(int size) {
        this.size = size;
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
        logger.info("Launching game " + maxGameId);
        Game game = null;
        try {
            game = new Game(server, maxGameId, List.copyOf(players.keySet()));
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
                    connection.send(new GameStartedNotification());
                }
        );
        try {
            Writer writer = new FileWriter("src/main/resources/json/maxId.json");
            new Gson().toJson(maxGameId + 1, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.maxGameId++;
        clear();
    }

    /**
     * Set instance null, players to new Map and size to zero
     */
    private void clear() {
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
    public boolean isEmpty() {
        return size == 0;
    }

    public synchronized boolean nicknameAvailable(String nickname) {
        return !players.containsKey(nickname);
    }

    public void removePlayer(String nickname) {
        players.remove(nickname);
        if (players.size() == 0) {
            size = 0;
        }
    }
}
