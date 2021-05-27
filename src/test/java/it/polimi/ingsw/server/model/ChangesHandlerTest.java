package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.personalboard.DevelopmentCardSlot;
import it.polimi.ingsw.server.model.personalboard.FaithTrack;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.ResourceManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ChangesHandlerTest {
    ChangesHandler changesHandler;
    ArrayList<String> nicknames;
    Game game;

    @BeforeEach
    void setUp() throws IOException {
        nicknames = new ArrayList<>();
        nicknames.add("Mario");
        changesHandler = new ChangesHandler(1);
        game = new Game(null, 1, nicknames);
    }

    @Test
    void createGameFilesFromBlueprint() throws IOException {
        assertFalse(Files.exists(Paths.get("src/main/resources/games/1")), "Error: Directory already exists");

        changesHandler.createGameFilesFromBlueprint(nicknames);
        //LeaderCardsDeck
        File file1 = new File("src/main/resources/default/game/LeaderCardsDeck/ConvertLeaderCards.json");
        File file2 = new File("src/main/resources/games/1/LeaderCardsDeck/ConvertLeaderCards.json");
        File file3 = new File("src/main/resources/default/game/LeaderCardsDeck/DiscountLeaderCards.json");
        File file4 = new File("src/main/resources/games/1/LeaderCardsDeck/DiscountLeaderCards.json");
        File file5 = new File("src/main/resources/default/game/LeaderCardsDeck/ProduceLeaderCards.json");
        File file6 = new File("src/main/resources/games/1/LeaderCardsDeck/ProduceLeaderCards.json");
        File file7 = new File("src/main/resources/default/game/LeaderCardsDeck/StoreLeaderCards.json");
        File file8 = new File("src/main/resources/games/1/LeaderCardsDeck/StoreLeaderCards.json");
        File file9 = new File("src/main/resources/default/game/LeaderCardsDeck/StoreLeaderCards.json");
        File file10 = new File("src/main/resources/games/1/LeaderCardsDeck/StoreLeaderCards.json");
        
        //Player
        File file11 = new File("src/main/resources/default/player/DevelopmentCardSlot1.json");
        File file12 = new File("src/main/resources/games/1/players/Mario/DevelopmentCardSlot1.json");
        File file13 = new File("src/main/resources/default/player/DevelopmentCardSlot2.json");
        File file14 = new File("src/main/resources/games/1/players/Mario/DevelopmentCardSlot2.json");
        File file15 = new File("src/main/resources/default/player/DevelopmentCardSlot3.json");
        File file16 = new File("src/main/resources/games/1/players/Mario/DevelopmentCardSlot3.json");
        File file17 = new File("src/main/resources/games/1/players/Mario/FaithTrack.json");
        File file19 = new File("src/main/resources/default/player/LeaderCards.json");
        File file20 = new File("src/main/resources/games/1/players/Mario/LeaderCards.json");
        File file21 = new File("src/main/resources/default/player/Player.json");
        File file22 = new File("src/main/resources/games/1/players/Mario/Player.json");
        File file23 = new File("src/main/resources/default/player/SinglePlayerFaithTrack.json");
        File file24 = new File("src/main/resources/games/1/players/Mario/SinglePlayerFaithTrack.json");
        File file25 = new File("src/main/resources/default/player/Strongbox.json");
        File file26 = new File("src/main/resources/games/1/players/Mario/Strongbox.json");
        File file27 = new File("src/main/resources/default/player/WarehouseDepots.json");
        File file28 = new File("src/main/resources/games/1/players/Mario/WarehouseDepots.json");
        
        //Generic game files
        File file29 = new File("src/main/resources/default/game/ActionTokenDeck.json");
        File file30 = new File("src/main/resources/games/1/ActionTokenDeck.json");
        File file31 = new File("src/main/resources/default/game/DevelopmentCardsBoard.json");
        File file32 = new File("src/main/resources/games/1/DevelopmentCardsBoard.json");
        File file33 = new File("src/main/resources/default/game/GameState.json");
        File file34 = new File("src/main/resources/games/1/GameState.json");
        File file35 = new File("src/main/resources/default/game/Market.json");
        File file36 = new File("src/main/resources/games/1/Market.json");

        assertAll(
                () -> assertArrayEquals(Files.readAllBytes(file1.toPath()), Files.readAllBytes(file2.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file3.toPath()), Files.readAllBytes(file4.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file5.toPath()), Files.readAllBytes(file6.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file7.toPath()), Files.readAllBytes(file8.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file9.toPath()), Files.readAllBytes(file10.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file11.toPath()), Files.readAllBytes(file12.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file13.toPath()), Files.readAllBytes(file14.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file15.toPath()), Files.readAllBytes(file16.toPath()), "Error: files are not identical"),
                () -> assertFalse(file17.exists(), "Error: file should not exist"),
                () -> assertArrayEquals(Files.readAllBytes(file19.toPath()), Files.readAllBytes(file20.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file21.toPath()), Files.readAllBytes(file22.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file23.toPath()), Files.readAllBytes(file24.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file25.toPath()), Files.readAllBytes(file26.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file27.toPath()), Files.readAllBytes(file28.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file29.toPath()), Files.readAllBytes(file30.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file31.toPath()), Files.readAllBytes(file32.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file33.toPath()), Files.readAllBytes(file34.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file35.toPath()), Files.readAllBytes(file36.toPath()), "Error: files are not identical")
        );
    }

    @Test
    @DisplayName("publishGameOutcome method test")
    void publishGameOutcomeTest() throws IOException {
        changesHandler.publishGameOutcome(new Game(null,1, nicknames));
        assertFalse(Files.exists(Paths.get("src/main/resources/games/1")), "Error: changes handler did not properly remove directory");
    }

    @Test
    @DisplayName("Game state write/read test")
    void gameStateTest() throws FileNotFoundException {
        changesHandler.writeGameState(GameState.DEALT_LEADER_CARDS);
        assertEquals(GameState.DEALT_LEADER_CARDS, changesHandler.readGameState(), "Error: changes handler did not correctly write/read game state");
    }

    @Test
    @DisplayName("Nicknames write/read test")
    void nicknameListTest() throws FileNotFoundException {
        changesHandler.writeNicknameList(nicknames);
        List<String> actualNicknames = changesHandler.readNicknameList();
        assertAll(
                () -> assertEquals(1, actualNicknames.size(), "Error: file contains different amount of nicknames"),
                () -> assertEquals("Mario", actualNicknames.get(0), "Error: file contains different nickname")
        );
    }

    @Test
    @DisplayName("Player write/read test")
    void playerTest() throws FileNotFoundException {
        changesHandler.writePlayer(game.getPlayer("Mario"));
        Player actualPlayer = changesHandler.readPlayer("Mario");
        assertEquals(game.getPlayer("Mario"), actualPlayer, "Error: change handler did not properly write/read player");
    }

    @Test
    @DisplayName("Market write/read test")
    void marketTest() throws FileNotFoundException {
        changesHandler.writeMarket(game.getMarket());
        Market actualMarket = changesHandler.readMarket();
        assertEquals(game.getMarket(), actualMarket, "Error: change handler did not properly write/read market");
    }

    @Test
    @DisplayName("Development cards board write/read test")
    void developmentCardsBoardTest() throws FileNotFoundException {
        changesHandler.writeDevelopmentCardsBoard(game.getDevelopmentCardsBoard());
        DevelopmentCardsBoard actualDevelopmentCardsBoard = changesHandler.readDevelopmentCardsBoard();
        assertEquals(game.getDevelopmentCardsBoard(), actualDevelopmentCardsBoard, "Error: change handler did not properly write/read development cards board");
    }

    @Test
    @DisplayName("Leader cards deck write/read test")
    void leaderCardsDeckTest() throws FileNotFoundException {
        changesHandler.writeLeaderCardsDeck(game.getLeaderCardsDeck());
        LeaderCardsDeck actualLeaderCardsDeck = changesHandler.readLeaderCardsDeck();
        assertEquals(game.getLeaderCardsDeck(), actualLeaderCardsDeck, "Error: change handler did not properly write/read leader cards deck");
    }

    @Test
    @DisplayName("Action token deck write/read test")
    void actionTokenDeckTest() throws FileNotFoundException {
        changesHandler.writeActionTokenDeck(game.getActionTokenDeck());
        ActionTokenDeck actualActionTokenDeck = changesHandler.readActionTokenDeck();
        assertEquals(game.getActionTokenDeck(), actualActionTokenDeck, "Error: change handler did not properly write/read action token deck");
    }

    @Test
    @DisplayName("Player's leader cards write/read test")
    void playerLeaderCardsTest() throws FileNotFoundException {
        changesHandler.writePlayerLeaderCards("Mario", game.getPlayer("Mario").getTempLeaderCards());
        List<LeaderCard> actualLeaderCards = changesHandler.readPlayerLeaderCards("Mario");
        assertEquals(game.getPlayer("Mario").getTempLeaderCards(), actualLeaderCards, "Error: change handler did not properly write/read player's leader cards");
    }

    @Test
    @DisplayName("Development card slot write/read test")
    void developmentCardSlotTest() throws FileNotFoundException {
        changesHandler.writeDevelopmentCardSlot("Mario", game.getPlayer("Mario").getPersonalBoard().getDevelopmentCardSlots().get(0));
        DevelopmentCardSlot actualDevelopmentCardSlot = changesHandler.readDevelopmentCardSlot("Mario", 1);
        assertEquals(game.getPlayer("Mario").getPersonalBoard().getDevelopmentCardSlots().get(0), actualDevelopmentCardSlot, "Error: change handler did not properly write/read player's development cards slot");
    }

    @Test
    @DisplayName("Warehouse depots write/read test")
    void warehouseDepotsTest() throws FileNotFoundException {
        changesHandler.writeWarehouseDepots("Mario", game.getPlayer("Mario").getPersonalBoard().getWarehouseDepots());
        ResourceManager actualWarehouseDepots = changesHandler.readWarehouseDepots("Mario");
        assertEquals(game.getPlayer("Mario").getPersonalBoard().getWarehouseDepots(), actualWarehouseDepots, "Error: change handler did not properly write/read player's warehouse depots");
    }

    @Test
    @DisplayName("Strongbox write/read test")
    void strongboxTest() throws FileNotFoundException {
        changesHandler.writeStrongbox("Mario", game.getPlayer("Mario").getPersonalBoard().getStrongbox());
        ResourceManager actualStrongbox = changesHandler.readWarehouseDepots("Mario");
        assertEquals(game.getPlayer("Mario").getPersonalBoard().getStrongbox(), actualStrongbox, "Error: change handler did not properly write/read player's strongbox");
    }

    @Test
    @DisplayName("Faith track write/read test")
    void faithTrackTest() throws FileNotFoundException {
        changesHandler.writeFaithTrack("Mario", game.getPlayer("Mario").getPersonalBoard().getFaithTrack());
        FaithTrack actualFaithTrack = changesHandler.readFaithTrack("Mario");
        assertEquals(game.getPlayer("Mario").getPersonalBoard().getFaithTrack(), actualFaithTrack, "Error: change handler did not properly write/read player's leader cards");
    }

    @Test
    @Disabled
    void flushBufferToDisk() {
        //todo: what should I check?
    }
}
