package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.network.message.renderable.updates.*;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import it.polimi.ingsw.server.model.developmentcard.*;
import it.polimi.ingsw.server.model.leadercard.*;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.personalboard.DevelopmentCardSlot;
import it.polimi.ingsw.server.model.personalboard.FaithTrack;
import it.polimi.ingsw.network.message.renderable.Renderable;
import it.polimi.ingsw.server.model.personalboard.SinglePlayerFaithTrack;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.ResourceManager;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO: this has to be async to not interrupt server flow
// maybe not, it is important that the calls happen in the right order
// TODO: move to model package
public class ChangesHandler {
    private final String root;
    private final SubmissionPublisher<Renderable> submissionPublisher;
    private boolean isNewGame;
    private boolean isSinglePlayerGame;
    private Map<Object, String> changesBuffer;

    public ChangesHandler(int gameId) {
        this.root = "src/main/resources/games/" + gameId;
        this.submissionPublisher = new SubmissionPublisher<>();
        this.isSinglePlayerGame = false;
        this.isNewGame = false;
        changesBuffer = new HashMap<>();
    }

    public void createGameFilesFromBlueprint(List<String> nicknames) throws IOException {
        copyFolder("src/main/resources/default/game", root);
        writeNicknameList(nicknames);
        for (String nickname : nicknames) {
            copyFolder("src/main/resources/default/player",
                    root + "/players/" + nickname);
        }
        isNewGame = true;
        if (nicknames.size() == 1) {
            isSinglePlayerGame = true;
            (new File(root + "/players/" + nicknames.get(0) + "/FaithTrack.json")).delete();
        } else {
            (new File(root + "/ActionTokenDeck.json")).delete();
            for (String nickname : nicknames) {
                (new File(root + "/players/" + nickname + "/SinglePlayerFaithTrack.json"))
                        .delete();
            }
        }
    }

    private void copyFolder(String sourceDir, String destinationDir) throws IOException {
        Files.walk(Paths.get(sourceDir))
                .forEach(source -> {
                    Path destination = Paths.get(destinationDir, source.toString()
                            .substring(sourceDir.length()));
                    try {
                        Files.copy(source, destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    // GameState
    public GameState readGameState() throws FileNotFoundException {
        return readValueFromFile(
                root + "/GameState.json",
                GameState.class
        );
    }

    public void writeGameState(GameState gameState) {
        changesBuffer.put(gameState, root + "/GameState.json");
    }

    // Nicknames List
    public List<String> readNicknameList() throws FileNotFoundException {
        return readListFromFile(
                root + "/Nicknames.json",
                String.class
        );
    }

    public void writeNicknameList(List<String> nicknameList) {
        changesBuffer.put(nicknameList, root + "/Nicknames.json");
    }

    // Publisher on reconnection
    public void publishModel(String nickname, Game game) {
        submissionPublisher.submit(new ModelUpdate(nickname, game));
    }

    // Player
    public Player readPlayer(String nickname) throws FileNotFoundException {
        Player player = readValueFromFile(root + "/players/" + nickname + "/Player.json",
                Player.class);
        player.init(this, nickname);
        return player;
    }

    public void publishPlayer(Player player) {
        submissionPublisher.submit(new PlayerBroadcastUpdate(player));
        submissionPublisher.submit(new PlayerPrivateUpdate(player));
    }

    public void writePlayer(Player player) {
        publishPlayer(player);
        changesBuffer.put(player, root + "/players/" + player.getNickname() + "/Player.json");
    }

    // Market
    public Market readMarket() throws FileNotFoundException {
        Market market = readValueFromFile(root + "/Market.json", Market.class);
        market.init(this);
        if (isNewGame)
            market.shuffle();
        return market;
    }

    public void publishMarket(Market market) {
        submissionPublisher.submit(new MarketUpdate(market));
    }

    public void writeMarket(Market market) {
        publishMarket(market);
        changesBuffer.put(market, root + "/Market.json");
    }

    // Development Cards Board
    public DevelopmentCardsBoard readDevelopmentCardsBoard() throws FileNotFoundException {
        DevelopmentCardsBoard developmentCardsBoard = readValueFromFile(root +
                "/DevelopmentCardsBoard.json", DevelopmentCardsBoard.class);
        developmentCardsBoard.init(this);
        if (isNewGame)
            developmentCardsBoard.shuffle();
        return developmentCardsBoard;
    }

    public void publishDevelopmentCardsBoard(DevelopmentCardsBoard developmentCardsBoard) {
        submissionPublisher.submit(new DevelopmentCardsBoardUpdate(developmentCardsBoard));
    }

    public void writeDevelopmentCardsBoard(DevelopmentCardsBoard developmentCardsBoard) {
        publishDevelopmentCardsBoard(developmentCardsBoard);
        changesBuffer.put(developmentCardsBoard, root + "/DevelopmentCardsBoard.json");
    }

    // Leader Cards Deck
    public LeaderCardsDeck readLeaderCardsDeck() throws FileNotFoundException {
        LeaderCard[] finalDeck = readListFromFile(root + "/LeaderCardsDeck" +
                "/ConvertLeaderCards.json", ConvertLeaderCard.class).toArray(new LeaderCard[0]);
        LeaderCard[] storeLeaderCards = readListFromFile(root + "/LeaderCardsDeck" +
                "/StoreLeaderCards.json", StoreLeaderCard.class).toArray(new LeaderCard[0]);
        LeaderCard[] produceLeaderCards = readListFromFile(root + "/LeaderCardsDeck" +
                "/ProduceLeaderCards.json", ProduceLeaderCard.class).toArray(new LeaderCard[0]);
        LeaderCard[] discountLeaderCards = readListFromFile(root + "/LeaderCardsDeck" +
                "/DiscountLeaderCards.json", DiscountLeaderCard.class).toArray(new LeaderCard[0]);
        IntStream.range(0, finalDeck.length).forEach(i -> {
            Optional.ofNullable(storeLeaderCards[i]).ifPresent(card -> finalDeck[i] = card);
            Optional.ofNullable(produceLeaderCards[i]).ifPresent(card -> finalDeck[i] = card);
            Optional.ofNullable(discountLeaderCards[i]).ifPresent(card -> finalDeck[i] = card);
        });
        LeaderCardsDeck leaderCardsDeck = new LeaderCardsDeck(Arrays.asList(finalDeck), this);
        if (isNewGame)
            leaderCardsDeck.shuffle();
        return leaderCardsDeck;
    }

    public void writeLeaderCardsDeck(LeaderCardsDeck leaderCardsDeck) {
        List<LeaderCard> deck = leaderCardsDeck.getDeck();
        changesBuffer.put(deck.stream().map(card -> (card instanceof ConvertLeaderCard) ? card :
                null).collect(Collectors.toList()), root + "/LeaderCardsDeck/ConvertLeaderCards" +
                ".json");
        changesBuffer.put(deck.stream().map(card -> (card instanceof StoreLeaderCard) ? card :
                null).collect(Collectors.toList()), root + "/LeaderCardsDeck/StoreLeaderCards" +
                ".json");
        changesBuffer.put(deck.stream().map(card -> (card instanceof ProduceLeaderCard) ? card :
                null).collect(Collectors.toList()), root + "/LeaderCardsDeck/ProduceLeaderCards" +
                ".json");
        changesBuffer.put(deck.stream().map(card -> (card instanceof DiscountLeaderCard) ? card :
                null).collect(Collectors.toList()), root + "/LeaderCardsDeck/DiscountLeaderCards" +
                ".json");
    }

    // Action Token Deck
    public ActionTokenDeck readActionTokenDeck() throws FileNotFoundException {
        ActionTokenDeck deck = readValueFromFile(root + "/ActionTokenDeck.json",
                ActionTokenDeck.class);
        if (isNewGame) deck.shuffle();
        return deck;
    }

    public void publishActionTokenDeck(ActionTokenDeck actionTokenDeck) {
        submissionPublisher.submit(new ActionTokenDeckUpdate(actionTokenDeck));
    }

    public void writeActionTokenDeck(ActionTokenDeck actionTokenDeck) {
        publishActionTokenDeck(actionTokenDeck);
        changesBuffer.put(actionTokenDeck, root + "/ActionTokenDeck.json");
    }

    // Player on-board Leader Cards
    public List<LeaderCard> readPlayerLeaderCards(String nickname) throws FileNotFoundException {
        return readListFromFile(
                root + "/players/" + nickname + "/LeaderCards.json",
                LeaderCard.class
        );
    }

    public void publishPlayerLeaderCards(String nickname, List<LeaderCard> cards) {
        submissionPublisher.submit(new LeaderCardsBroadcastUpdate(nickname, cards));
        submissionPublisher.submit(new LeaderCardsPrivateUpdate(nickname, cards));
    }

    public void writePlayerLeaderCards(String nickname, List<LeaderCard> cards) {
        publishPlayerLeaderCards(nickname, cards);
        changesBuffer.put(cards, root + "/players/" + nickname + "/LeaderCards.json");
    }

    // Development Cards Slot
    public DevelopmentCardSlot readDevelopmentCardSlot(String nickname, int index)
            throws FileNotFoundException {
        DevelopmentCardSlot developmentCardSlot = readValueFromFile(root + "/players/" + nickname +
                "/DevelopmentCardSlot" + index + ".json", DevelopmentCardSlot.class);
        developmentCardSlot.init(nickname, this);
        return developmentCardSlot;
    }

    public void publishDevelopmentCardSlot(String nickname,
                                           DevelopmentCardSlot developmentCardSlot) {
        submissionPublisher.submit(new DevelopmentCardSlotUpdate(nickname, developmentCardSlot));
    }

    public void writeDevelopmentCardSlot(String nickname, DevelopmentCardSlot developmentCardSlot) {
        publishDevelopmentCardSlot(nickname, developmentCardSlot);
        changesBuffer.put(developmentCardSlot, root + "/players/" + nickname +
                "/DevelopmentCardSlot" + developmentCardSlot.getSlotIndex() + ".json");
    }

    // Warehouse Depots
    public ResourceManager readWarehouseDepots(String nickname)
            throws FileNotFoundException {
        return readValueFromFile(root + "/players/" + nickname + "/WarehouseDepots.json",
                ResourceManager.class);
    }

    public void publishWarehouseDepots(String nickname, ResourceManager depots) {
        submissionPublisher.submit(new DepotsUpdate(nickname, depots));
    }

    public void writeWarehouseDepots(String nickname, ResourceManager depots) {
        publishWarehouseDepots(nickname, depots);
        changesBuffer.put(depots, root + "/players/" + nickname + "/WarehouseDepots.json");
    }

    // Strongbox
    public ResourceManager readStrongbox(String nickname)
            throws FileNotFoundException {
        return readValueFromFile(root + "/players/" + nickname + "/Strongbox.json",
                ResourceManager.class);
    }

    public void publishStrongbox(String nickname, ResourceManager strongbox) {
        submissionPublisher.submit(new StrongboxUpdate(nickname, strongbox));
    }

    public void writeStrongbox(String nickname, ResourceManager strongbox) {
        publishStrongbox(nickname, strongbox);
        changesBuffer.put(strongbox, root + "/players/" + nickname + "/Strongbox.json");
    }

    // Faith Track
    public FaithTrack readFaithTrack(String nickname) throws FileNotFoundException {
        FaithTrack faithTrack = isSinglePlayerGame
                ? readValueFromFile(root + "/players/" + nickname +
                "/SinglePlayerFaithTrack.json", SinglePlayerFaithTrack.class)
                : readValueFromFile(root + "/players/" + nickname +
                "/FaithTrack.json", FaithTrack.class);
        faithTrack.init(nickname, this);
        return faithTrack;
    }

    public void publishFaithTrack(String nickname, FaithTrack faithTrack) {
        submissionPublisher.submit(new FaithTrackUpdate(nickname, faithTrack));
    }

    public void writeFaithTrack(String nickname, FaithTrack faithTrack) {
        publishFaithTrack(nickname, faithTrack);
        changesBuffer.put(faithTrack, root + "/players/" + nickname + "/FaithTrack.json");
    }

    /**
     * Utility method used to parse any single-value json file into
     * a variable of the type specified via parameter.
     *
     * @param filepath  file disk location.
     * @param classType classType of the required output
     * @param <T>       output type
     * @return the value contained in the file.
     * @throws FileNotFoundException exception thrown when the target
     *                               file is not present on disk.
     */
    private <T> T readValueFromFile(String filepath, Class<T> classType)
            throws FileNotFoundException {
        return new Gson().fromJson(new JsonReader(new FileReader(filepath)), classType);
    }

    private <T> List<T> readListFromFile(String filepath, Class<T> classType)
            throws FileNotFoundException {
        T[] array =
                new GsonBuilder().setPrettyPrinting().serializeNulls().create()
                        .fromJson(new JsonReader(new FileReader(filepath)), classType.arrayType());
        return Arrays.asList(array);
    }

    private <T, K> Map<T, K> readMapFromFile(String filepath)
            throws FileNotFoundException {
        Type mapType = new TypeToken<Map<T, K>>() {
        }.getType();
        return new Gson().fromJson(new JsonReader(new FileReader(filepath)),
                mapType);
    }

    public void subscribe(RemoteView remoteView) {
        submissionPublisher.subscribe(remoteView);
    }

    public void flushBufferToDisk() {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        changesBuffer.forEach((object, filepath) -> {
            try {
                Writer writer = new FileWriter(filepath);
                gson.toJson(object, writer);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        changesBuffer = new HashMap<>();
    }
}
