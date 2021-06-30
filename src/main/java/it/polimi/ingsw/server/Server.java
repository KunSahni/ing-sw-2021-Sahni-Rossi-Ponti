package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Server implements Flow.Subscriber<Integer> {
    private static final int port = 8080;
    private ServerSocket serverSocket;
    private final Map<Integer, List<String>> players = new HashMap<>();
    private final Map<Integer, Game> currentGames = new HashMap<>();
    private final List<Integer> dormantGames = new ArrayList<>();
    private final Map<Integer, Controller> gameIDControllerMap = new HashMap<>();
    private final Queue<Thread> waitingThreads = new ArrayDeque<>();

    public Server() throws IOException {
        this.serverSocket = null;
    }

    /**
     * server starts to hear on the port to accept, create and run connections
     * only one connection can choose Lobby size, so the first who arrived is started, the others wait until
     * Lobby size is set, than are ran.
     */
    public void start(){
        Lobby.getInstance().setServer(this);
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            createDefaultGameDir();
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkDormantGames();
        while(true){
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, this);
                Thread thread = new Thread(connection);
                if (Lobby.getInstance().getSize()==-1){
                    waitingThreads.add(thread);
                }
                else {
                    if (Lobby.getInstance().getSize()==0){
                        Lobby.getInstance().setSize(-1);
                    }
                    thread.start();
                }
            } catch (IOException e){
                System.err.println("Connection error!");
            }
        }
    }

    public Map<Integer, List<String>> getPlayers() {
        return players;
    }

    /**
     * check if there are existing game folders, in case add their id in dormantGames attribute
     */
    private void checkDormantGames(){
        int maxId = 0;
        try {
            JsonReader reader = new JsonReader(new FileReader(ChangesHandler.getWorkingDirectory() + "/server/maxID.json"));
            maxId = new Gson().fromJson(reader, int.class);
            reader.close();
        } catch (FileNotFoundException e) {
            maxId = 1;
            try {
                File rootDir = new File(ChangesHandler.getWorkingDirectory() + "/server");
                if(!rootDir.exists())
                    rootDir.mkdirs();
                Writer writer = new FileWriter(ChangesHandler.getWorkingDirectory() + "/server/maxID.json");
                Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                gson.toJson(maxId, writer);
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i=1; i<maxId; i++){
            if (new File(ChangesHandler.getWorkingDirectory() + "/server/games/" + i).isDirectory()){
                dormantGames.add(i);
            }
        }
    }

    public List<Integer> getDormantGames() {
        return dormantGames;
    }

    /**
     * remove the passed gameId from dormant games and add it to current games,
     * add all nicknames of that game to players map
     * @param gameId
     * @param game
     * @param nickname
     */
    public void restoreGame(Integer gameId, Game game, String nickname){
        dormantGames.remove(gameId);
        currentGames.put(gameId, game);
        players.put(gameId, new ArrayList() {{ add(nickname);}});
        for (Player player: game.getPlayerList()) {
            if (!player.getNickname().equals(nickname)){
                players.get(gameId).add(player.getNickname());
            }
        }
        Lobby.getInstance().setSize(0);
    }

    /**
     * add nickname and gameId passed to players list
     * @param nickname is the nickname that will be added to the map
     * @param gameId is the gameId that will be added to the map
     */
    public void addNicknameGameId(String nickname, int gameId){
        if (players.containsKey(gameId)){
            players.get(gameId).add(nickname);
        }
        else {
            players.put(gameId, new ArrayList() {{ add(nickname);}});
        }
    }

    public void addGameToCurrentGames(int gameId, Game game) {
        currentGames.put(gameId, game);
    }

    public void addGameIdController(int gameID, Controller controller){
        gameIDControllerMap.put(gameID, controller);
    }

    public Controller getController(Integer gameID){
        return gameIDControllerMap.get(gameID);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    /**
     * remove the gameId passed from currentGames, gameIDControllerMap and players map
     * @param item is the gameId to be removed
     */
    @Override
    public void onNext(Integer item) {
        currentGames.remove(item);
        gameIDControllerMap.remove(item);
        players.remove(item);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

    /**
     * start a number of thread equals to the Lobby size
     */
    public void wakeUpThread(){
        for (int i=1; i<Lobby.getInstance().getSize(); i++){
            if (!waitingThreads.isEmpty()){
                waitingThreads.poll().start();
            }
        }
        if (!waitingThreads.isEmpty()){
            waitingThreads.poll().start();
            Lobby.getInstance().setSize(-1);
        }
    }

    /**
     * @param nickname nickname of the player who is controlled
     * @param id id of the game controlled
     * @return true only if the player with same nickname passed is connected
     */
    public boolean playerAlreadyConnected(String nickname, Integer id){
        for (Player player: currentGames.get(id).getPlayerList()) {
            if (player.getNickname().equals(nickname) && player.isConnected()){
                return true;
            }
        }
        return false;
    }

    private void createDefaultGameDir() throws IOException {
        InputStream resFolder = this.getClass().getResourceAsStream("/default.zip");
        File destDir = new File(ChangesHandler.getWorkingDirectory() + "/server/games/default");
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(resFolder);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
