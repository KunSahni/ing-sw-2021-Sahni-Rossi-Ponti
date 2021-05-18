package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private static final int port = 8080;
    private final ServerSocket serverSocket;
    private Map<String, Integer> players = new HashMap<>();
    private Map<Integer, Game> currentGames = new HashMap<>();
    private List<Integer> dormantGames = new ArrayList<>();
    private Map<Integer, RemoteView> gameIDRemoteViewMap = new HashMap<>();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    /**
     * server starts to hear on the port to accept, create and run connections
     */
    public void start(){
        checkDormantGames();
        while(true){
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, this);
                Thread thread = new Thread(connection);
                thread.start();
            } catch (IOException e){
                System.err.println("Connection error!");
            }
        }
    }

    /**
     * check if a player was disconnected from a Game looking for player nickname in players attribute, once found check
     * if his game is in currentGame attribute
     * @param nickname is the nickname of the player
     * @param gameId is the gameId of the player
     * @return the game of the disconnected player or null if the game doesn't exist
     */
    public Game checkDisconnectedPlayer(String nickname, int gameId){
        for (String n: players.keySet()) {
            if (nickname.equals(n)){
                if (gameId == players.get(nickname)){
                    if(currentGames.get(gameId)!=null){
                        return currentGames.get(gameId);
                    }
                }
            }
        }
        return null;
    }

    public Map<String, Integer> getPlayers() {
        return players;
    }

    /**
     * check if there are existing game folders, in case add their id in dormantGames attribute
     */
    private void checkDormantGames(){
        int maxId = 0;
        try {
            maxId = new Gson().fromJson(new JsonReader(new FileReader("src/main/resources/maxId.json")), Integer.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i=1; i<maxId; i++){
            if (new File("src/main/resources/" + i).isDirectory()){
                dormantGames.add(i);
            }
        }
    }

    public List<Integer> getDormantGames() {
        return dormantGames;
    }

    public void restoreGame(int gameId, Game game){
        dormantGames.remove(gameId);
        currentGames.put(gameId, game);
    }

    public void addNicknameGameId(String nickname, int gameId){
        players.put(nickname, gameId);
    }

    public void addCurrentGames(int gameId, Game game) {
        currentGames.putIfAbsent(gameId, game);
    }

    public void addGameIDRemoteView(int gameID, RemoteView remoteView){
        gameIDRemoteViewMap.putIfAbsent(gameID, remoteView);
    }

    public RemoteView getRemoteView(Integer gameID){
        return gameIDRemoteViewMap.get(gameID);
    }
}
