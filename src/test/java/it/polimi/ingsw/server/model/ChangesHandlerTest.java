package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsDeck;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboard.DevelopmentCardSlot;
import it.polimi.ingsw.server.model.personalboard.FaithTrack;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.ResourceManager;
import org.junit.jupiter.api.*;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChangesHandlerTest {
    ChangesHandler changesHandler;
    ArrayList<String> nicknames;
    Game game;

    @BeforeEach
    void setUp() throws IOException {
        nicknames = new ArrayList<>();
        nicknames.add("Mario");
        nicknames.add("Luigi");
        changesHandler = new ChangesHandler(1);
        game = new Game(new Server(), 1, nicknames);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        changesHandler.publishGameOutcome(game);
    }

    @Test
    @Disabled
    void createGameFilesFromBlueprint() throws IOException {
        //Game already calls createGameFilesFromBlueprint

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
        File file23 = new File("src/main/resources/default/player/FaithTrack.json");
        File file24 = new File("src/main/resources/games/1/players/Mario/FaithTrack.json");
        File file25 = new File("src/main/resources/default/player/Strongbox.json");
        File file26 = new File("src/main/resources/games/1/players/Mario/Strongbox.json");
        File file27 = new File("src/main/resources/default/player/WarehouseDepots.json");
        File file28 = new File("src/main/resources/games/1/players/Mario/WarehouseDepots.json");
        
        //Generic game files
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
                () -> assertTrue(file17.exists(), "Error: file should exist"),
                () -> assertArrayEquals(Files.readAllBytes(file19.toPath()), Files.readAllBytes(file20.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file21.toPath()), Files.readAllBytes(file22.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file23.toPath()), Files.readAllBytes(file24.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file25.toPath()), Files.readAllBytes(file26.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file27.toPath()), Files.readAllBytes(file28.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file31.toPath()), Files.readAllBytes(file32.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file33.toPath()), Files.readAllBytes(file34.toPath()), "Error: files are not identical"),
                () -> assertArrayEquals(Files.readAllBytes(file35.toPath()), Files.readAllBytes(file36.toPath()), "Error: files are not identical")
        );
    }

    @Test
    @DisplayName("Game state write/read test")
    void gameStateTest() throws FileNotFoundException {
        changesHandler.writeGameState(GameState.DEALT_LEADER_CARDS);
        changesHandler.flushBufferToDisk();
        assertEquals(GameState.DEALT_LEADER_CARDS, changesHandler.readGameState(), "Error: changes handler did not correctly write/read game state");
    }

    @Test
    @DisplayName("Nicknames write/read test")
    void nicknameListTest() throws FileNotFoundException {
        changesHandler.writeNicknameList(nicknames);
        changesHandler.flushBufferToDisk();
        List<String> actualNicknames = changesHandler.readNicknameList();
        assertAll(
                () -> assertEquals(2, actualNicknames.size(), "Error: file contains different amount of nicknames"),
                () -> assertEquals("Mario", actualNicknames.get(0), "Error: file contains different nickname"),
                () -> assertEquals("Luigi", actualNicknames.get(1), "Error: file contains different nickname")
        );
    }

    @Test
    @DisplayName("Player write/read test")
    void playerTest() throws FileNotFoundException {
        changesHandler.writePlayer(game.getPlayer("Mario"));
        changesHandler.flushBufferToDisk();
        Player actualPlayer = changesHandler.readPlayer("Mario");
        assertAll(
                () -> assertEquals(game.getPlayer("Mario").getNickname(), actualPlayer.getNickname(), "Changes handler didn't properly read/write nickname"),
                () -> assertEquals(game.getPlayer("Mario").getTempLeaderCards(), actualPlayer.getTempLeaderCards(), "Changes handler didn't properly read/write temp leader cards"),
                () -> assertEquals(game.getPlayer("Mario").getTempMarbles(), actualPlayer.getTempMarbles(), "Changes handler didn't properly read/write temp marbles"),
                () -> assertEquals(game.getPlayer("Mario").getPosition(), actualPlayer.getPosition(), "Changes handler didn't properly read/write position"),
                () -> assertEquals(game.getPlayer("Mario").getPerformedActions(), actualPlayer.getPerformedActions(), "Changes handler didn't properly read/write performed actions")
        );
    }

    @Test
    @DisplayName("Market write/read test")
    void marketTest() throws FileNotFoundException {
        changesHandler.writeMarket(game.getMarket());
        changesHandler.flushBufferToDisk();
        Market actualMarket = changesHandler.readMarket();
        assertAll(
                () -> assertArrayEquals(game.getMarket().getMarblesLayout(),
                        actualMarket.getMarblesLayout(),
                        "Error: change handler did not properly write/read market"),
                () -> assertEquals(game.getMarket().getExtraMarble().getColor(), actualMarket.getExtraMarble().getColor(), "Error: change handler did not properly write/read market's extra marble")


        );
    }

    @Test
    @DisplayName("Development cards board write/read test")
    void developmentCardsBoardTest() throws FileNotFoundException {
        changesHandler.writeDevelopmentCardsBoard(game.getDevelopmentCardsBoard());
        changesHandler.flushBufferToDisk();

        List<DevelopmentCard> expectedDevelopmentCardsBoard = Arrays
                .stream(game.getDevelopmentCardsBoard().peekBoard())
                .flatMap(Arrays::stream)
                .map(
                        DevelopmentCardsDeck::getDeck
                ).flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<DevelopmentCard> actualDevelopmentCardsBoard = Arrays
                .stream(changesHandler.readDevelopmentCardsBoard().peekBoard())
                .flatMap(Arrays::stream)
                .map(
                        DevelopmentCardsDeck::getDeck
                ).flatMap(Collection::stream)
                .collect(Collectors.toList());
        assertEquals(expectedDevelopmentCardsBoard, actualDevelopmentCardsBoard, "Error: change handler did not properly write/read development cards board");
    }

    @Test
    @DisplayName("Leader cards deck write/read test")
    void leaderCardsDeckTest() throws FileNotFoundException {
        changesHandler.writeLeaderCardsDeck(game.getLeaderCardsDeck());
        changesHandler.flushBufferToDisk();
        LeaderCardsDeck actualLeaderCardsDeck = changesHandler.readLeaderCardsDeck();
        assertEquals(game.getLeaderCardsDeck().getDeck(), actualLeaderCardsDeck.getDeck(), "Error: change handler did not properly write/read leader cards deck");
    }

    @Test
    @Disabled
    @DisplayName("Action token deck write/read test")
    void actionTokenDeckTest() throws FileNotFoundException {
        changesHandler.writeActionTokenDeck(game.getActionTokenDeck());
        changesHandler.flushBufferToDisk();
        ActionTokenDeck actualActionTokenDeck = changesHandler.readActionTokenDeck();
        assertEquals(game.getActionTokenDeck().getCurrentDeck(), actualActionTokenDeck.getCurrentDeck(), "Error: change handler did not properly write/read action token deck");
    }

    @Test
    @DisplayName("Player's leader cards write/read test")
    void playerLeaderCardsTest() throws FileNotFoundException {
        changesHandler.writePlayerLeaderCards("Mario", game.getPlayer("Mario").getTempLeaderCards());
        changesHandler.flushBufferToDisk();
        List<LeaderCard> actualLeaderCards = changesHandler.readPlayerLeaderCards("Mario");
        assertEquals(game.getPlayer("Mario").getTempLeaderCards(), actualLeaderCards, "Error: change handler did not properly write/read player's leader cards");
    }

    @Test
    @DisplayName("Development card slot write/read test")
    void developmentCardSlotTest() throws FileNotFoundException {
        changesHandler.writeDevelopmentCardSlot("Mario", game.getPlayer("Mario").getPersonalBoard().getDevelopmentCardSlots().get(0));
        changesHandler.flushBufferToDisk();
        DevelopmentCardSlot actualDevelopmentCardSlot = changesHandler.readDevelopmentCardSlot("Mario", 1);
        assertEquals(game.getPlayer("Mario").getPersonalBoard().getDevelopmentCardSlots().get(0).getDevelopmentCards(), actualDevelopmentCardSlot.getDevelopmentCards(), "Error: change handler did not properly write/read player's development cards slot");
    }

    @Test
    @DisplayName("Warehouse depots write/read test")
    void warehouseDepotsTest() throws FileNotFoundException {
        changesHandler.writeWarehouseDepots("Mario", game.getPlayer("Mario").getPersonalBoard().getWarehouseDepots());
        changesHandler.flushBufferToDisk();
        ResourceManager actualWarehouseDepots = changesHandler.readWarehouseDepots("Mario");
        assertEquals(game.getPlayer("Mario").getPersonalBoard().getWarehouseDepots(), actualWarehouseDepots, "Error: change handler did not properly write/read player's warehouse depots");
    }

    @Test
    @DisplayName("Strongbox write/read test")
    void strongboxTest() throws FileNotFoundException {
        changesHandler.writeStrongbox("Mario", game.getPlayer("Mario").getPersonalBoard().getStrongbox());
        changesHandler.flushBufferToDisk();
        ResourceManager actualStrongbox = changesHandler.readWarehouseDepots("Mario");
        assertEquals(game.getPlayer("Mario").getPersonalBoard().getStrongbox(), actualStrongbox, "Error: change handler did not properly write/read player's strongbox");
    }

    @Test
    @DisplayName("Faith track write/read test")
    void faithTrackTest() throws FileNotFoundException {
        changesHandler.writeFaithTrack("Mario", game.getPlayer("Mario").getPersonalBoard().getFaithTrack());
        changesHandler.flushBufferToDisk();
        FaithTrack actualFaithTrack = changesHandler.readFaithTrack("Mario");
        assertEquals(game.getPlayer("Mario").getPersonalBoard().getFaithTrack().getPopesFavors(), actualFaithTrack.getPopesFavors(), "Error: change handler did not properly write/read player's leader cards");
    }

    //todo: need to fix changesHandler with proper management for singleplayer games, so I can fix test methods
}
