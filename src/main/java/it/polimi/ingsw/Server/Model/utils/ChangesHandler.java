package it.polimi.ingsw.server.model.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.network.message.renderable.updates.*;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.developmentcard.*;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.personalboardpackage.DevelopmentCardSlot;
import it.polimi.ingsw.server.model.personalboardpackage.FaithTrack;
import it.polimi.ingsw.network.message.renderable.Renderable;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.SubmissionPublisher;

// TODO: this has to be async to not interrupt server flow
// maybe not, it is important that the calls happen in the right order
public class ChangesHandler {
    private final String root;
    private final SubmissionPublisher<Renderable> submissionPublisher;
    private boolean isNewGame;
    private Map<Object, String> changesBuffer;

    public ChangesHandler(int gameId) {
        this.root = "src/main/resources/" + gameId + "/";
        this.submissionPublisher = new SubmissionPublisher<>();
        this.isNewGame = false;
        changesBuffer = new HashMap<>();
    }

    public void createGameFilesFromBlueprint(List<String> nicknames) {
        // create gameId folder
        // inside of it:
        // create json array of nicknames
        // copy market json from blueprint
        // copy dev cards board from blueprint
        // copy leader cards deck from blueprint
        isNewGame = true;
    }

    // GameState
    public GameState readGameState() throws FileNotFoundException {
        return readValueFromFile(
                root + "GameState.json",
                GameState.class
        );
    }

    public void writeGameState(GameState gameState) {
        changesBuffer.put(gameState, root + "GameState.json");
    }

    // Nicknames List
    public List<String> readNicknameList() throws FileNotFoundException {
        return readListFromFile(
                root + "Nicknames.json",
                String.class
        );
    }

    //TODO: call from createFilesFromBlueprint
    public void writeNicknameList(List<String> nicknameList) {
        changesBuffer.put(nicknameList, root + "Nicknames.json");
    }

    // Player
    public Player readPlayer(String nickname) throws FileNotFoundException {
        Player player = readValueFromFile(root + nickname + "/Player.json", Player.class);
        player.init(this, nickname);
        publishPlayer(player);
        return player;
    }

    public void publishPlayer(Player player) {
        submissionPublisher.submit(new PlayerBroadcastUpdate(player));
        submissionPublisher.submit(new PlayerPrivateUpdate(player));
    }

    public void writePlayer(Player player) {
        publishPlayer(player);
        changesBuffer.put(player, root + player.getNickname() + "/Player.json");
    }

    // Market
    public Market readMarket() throws FileNotFoundException {
        Market market = readValueFromFile(root + "Market.json", Market.class);
        market.init(this);
        if (isNewGame)
            market.shuffle();
        publishMarket(market);
        return market;
    }

    public void publishMarket(Market market) {
        submissionPublisher.submit(new MarketUpdate(market));
    }

    public void writeMarket(Market market) {
        publishMarket(market);
        changesBuffer.put(market, root + "Market.json");
    }

    // Development Cards Board
    public DevelopmentCardsBoard readDevelopmentCardsBoard() throws FileNotFoundException {
        DevelopmentCardsBoard developmentCardsBoard = readValueFromFile(root +
                "DevelopmentCardsBoard.json", DevelopmentCardsBoard.class);
        developmentCardsBoard.init(this);
        if (isNewGame)
            developmentCardsBoard.shuffle();
        publishDevelopmentCardsBoard(developmentCardsBoard);
        return developmentCardsBoard;
    }

    public void publishDevelopmentCardsBoard(DevelopmentCardsBoard developmentCardsBoard) {
        submissionPublisher.submit(new DevelopmentCardsBoardUpdate(developmentCardsBoard));
    }

    public void writeDevelopmentCardsBoard(DevelopmentCardsBoard developmentCardsBoard) {
        publishDevelopmentCardsBoard(developmentCardsBoard);
        changesBuffer.put(developmentCardsBoard, root + "DevelopmentCardsBoard.json");
    }

    // Leader Cards Deck
    public LeaderCardsDeck readLeaderCardsDeck() throws FileNotFoundException {
        LeaderCardsDeck leaderCardsDeck = readValueFromFile(
                root + "LeaderCardsDeck.json",
                LeaderCardsDeck.class
        );
        leaderCardsDeck.init(this);
        if (isNewGame)
            leaderCardsDeck.shuffle();
        return leaderCardsDeck;
    }

    public void writeLeaderCardsDeck(LeaderCardsDeck leaderCardsDeck) {
        changesBuffer.put(leaderCardsDeck, root + "LeaderCardsDeck.json");
    }

    // Player on-board Leader Cards
    public List<LeaderCard> readPlayerLeaderCards(String nickname) throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/LeaderCards.json",
                LeaderCard.class
        );
    }

    public void publishPlayerLeaderCards(String nickname, List<LeaderCard> cards) {
        submissionPublisher.submit(new LeaderCardsBroadcastUpdate(nickname, cards));
        submissionPublisher.submit(new LeaderCardsPrivateUpdate(nickname, cards));
    }

    public void writePlayerLeaderCards(String nickname, List<LeaderCard> cards) {
        publishPlayerLeaderCards(nickname, cards);
        changesBuffer.put(cards, root + nickname + "/LeaderCards.json");
    }

    // Development Cards Slot
    public DevelopmentCardSlot readDevelopmentCardSlot(String nickname, int index)
            throws FileNotFoundException {
        DevelopmentCardSlot developmentCardSlot = readValueFromFile(root + nickname +
                "/DevelopmentCardSlot" + index + ".json", DevelopmentCardSlot.class);
        developmentCardSlot.init(nickname, this);
        publishDevelopmentCardSlot(nickname, developmentCardSlot);
        return developmentCardSlot;
    }

    public void publishDevelopmentCardSlot(String nickname,
                                           DevelopmentCardSlot developmentCardSlot) {
        submissionPublisher.submit(new DevelopmentCardSlotUpdate(nickname, developmentCardSlot));
    }

    public void writeDevelopmentCardSlot(String nickname, DevelopmentCardSlot developmentCardSlot) {
        publishDevelopmentCardSlot(nickname, developmentCardSlot);
        changesBuffer.put(developmentCardSlot, root + nickname +
                "/DevelopmentCardSlot" + developmentCardSlot.getSlotIndex() + ".json");
    }

    // Warehouse Depots
    public ResourceManager readWarehouseDepots(String nickname)
            throws FileNotFoundException {
        ResourceManager depots = readValueFromFile(root + nickname + "/WarehouseDepots.json",
                ResourceManager.class);
        publishWarehouseDepots(nickname, depots);
        return depots;
    }

    public void publishWarehouseDepots(String nickname, ResourceManager depots) {
        submissionPublisher.submit(new DepotsUpdate(nickname, depots));
    }

    public void writeWarehouseDepots(String nickname, ResourceManager depots) {
        publishWarehouseDepots(nickname, depots);
        changesBuffer.put(depots, root + nickname + "/WarehouseDepots.json");
    }

    // Strongbox
    public ResourceManager readStrongbox(String nickname)
            throws FileNotFoundException {
        ResourceManager strongbox = readValueFromFile(root + nickname + "/Strongbox.json",
                ResourceManager.class);
        publishStrongbox(nickname, strongbox);
        return strongbox;
    }

    public void publishStrongbox(String nickname, ResourceManager strongbox) {
        submissionPublisher.submit(new StrongboxUpdate(nickname, strongbox));
    }

    public void writeStrongbox(String nickname, ResourceManager strongbox) {
        publishStrongbox(nickname, strongbox);
        changesBuffer.put(strongbox, root + nickname + "/Strongbox.json");
    }

    // Faith Track
    public FaithTrack readFaithTrack(String nickname) throws FileNotFoundException {
        FaithTrack faithTrack = readValueFromFile(root + nickname + "/FaithTrack.json",
                FaithTrack.class);
        faithTrack.init(nickname, this);
        publishFaithTrack(nickname, faithTrack);
        return faithTrack;
    }

    public void publishFaithTrack(String nickname, FaithTrack faithTrack) {
        submissionPublisher.submit(new FaithTrackUpdate(nickname, faithTrack));
    }

    public void writeFaithTrack(String nickname, FaithTrack faithTrack) {
        publishFaithTrack(nickname, faithTrack);
        changesBuffer.put(faithTrack, root + nickname + "/FaithTrack.json");
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
        T[] array = new Gson().fromJson(new JsonReader(new FileReader(filepath)),
                classType.arrayType());
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
        Gson gson = new Gson();
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
