package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.connection.Connection;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.remoteview.RemoteView;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    Controller controller;
    Game game;
    List<String> nicknameList;
    RemoteView remoteView;
    String nick1;
    String nick2;
    Server server;

    @BeforeAll
    static void deleteActions(){
        deleteDir();
        new File("src/main/resources/games").mkdirs();
    }

    private void init(Integer gameId){
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nick1 = "qwe";
        nick2 = "asd";
        nicknameList = List.of(nick1, nick2);
        try {
            game = new Game(server, gameId, nicknameList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = new Controller(game);
        remoteView = new RemoteView(controller);
        controller.setRemoteView(remoteView);
    }

    @Test
    @DisplayName("Player has been connected correctly")
    void connectPlayerTest() {
        init(62);
        try {
            controller.connectPlayer(nick1, new Connection(new Socket(), new Server()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertAll(
                //()-> assertTrue(remoteView.getConnectedPlayers.containsKey("qwe")),
                ()-> assertTrue(game.getPlayer(nick1).isConnected())
        );
    }

    @Test
    @DisplayName("Player has been disconnected correctly")
    void disconnectPlayerTest() {
        init(63);
        try {
            controller.connectPlayer(nick1, new Connection(new Socket(), new Server()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.disconnectPlayer(nick1);
        assertAll(
                //()-> assertFalse(remoteView.getConnectedPlayers.containsKey("qwe")),
                ()-> assertFalse(game.getPlayer(nick1).isConnected())
        );
        deleteDir();
    }

    static void deleteDir() {
        try {
            Files.walk(Path.of("src/main/resources/games"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void delete(){
        deleteDir();
    }
}
