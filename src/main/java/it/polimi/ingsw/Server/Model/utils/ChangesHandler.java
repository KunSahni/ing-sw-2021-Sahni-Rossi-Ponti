package it.polimi.ingsw.server.model.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.network.message.renderable.updates.*;
import it.polimi.ingsw.network.message.renderable.updates.LeaderCardsUpdate;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.developmentcard.*;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboardpackage.FavorStatus;
import it.polimi.ingsw.network.message.renderable.Renderable;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
        writeBufferedChanges();
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
        writeBufferedChanges();
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
                root + nickname + "/PlayerLeaderCards.json",
                LeaderCard.class
        );
    }

    public void publishPlayerLeaderCards(String nickname, List<LeaderCard> cards) {
        List<DumbLeaderCard> privateDumbLeaderCards =
                cards.stream().map(LeaderCard::convertToDumb).collect(Collectors.toList());
        List<DumbLeaderCard> publicDumbLeaderCards =
                privateDumbLeaderCards.stream()
                        .map(card -> (card.isActive()) ? card : null)
                        .collect(Collectors.toList());
        submissionPublisher.submit(new LeaderCardsBroadcastUpdate(nickname, publicDumbLeaderCards));
        submissionPublisher.submit(new LeaderCardsPrivateUpdate(nickname, privateDumbLeaderCards));
    }

    public void writePlayerLeaderCards(String nickname, List<LeaderCard> cards) {
        publishPlayerLeaderCards(nickname, cards);
        changesBuffer.put(cards, root + nickname + "/PlayerLeaderCards.json");
    }

    // Information contained in the Player class:
    // Player temp Leader Cards
    public List<LeaderCard> readPlayerTempLeaderCards(String nickname)
            throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/TempLeaderCards.json",
                LeaderCard.class
        );
    }

    public void publishTempLeaderCards(String nickname, List<LeaderCard> cards) {
        submissionPublisher.submit(new LeaderCardsChoiceUpdate(nickname,
                cards.stream().map(LeaderCard::convertToDumb).collect(Collectors.toList())));
    }

    public void writePlayerTempLeaderCards(String nickname, List<LeaderCard> cards) {
        publishTempLeaderCards(nickname, cards);
        changesBuffer.put(cards, root + nickname + "/TempLeaderCards.json");
    }

    // Player Temp Market Marbles
    public Map<MarketMarble, Integer> readPlayerTempMarbles(String nickname) throws FileNotFoundException {
        return readMapFromFile(root + nickname + "/TempMarbles.json");
    }

    public void publishPlayerTempMarbles(String nickname, Map<MarketMarble, Integer> marbles) {
        submissionPublisher.submit(new TempMarblesUpdate(nickname, marbles));
    }

    public void writePlayerTempMarbles(String nickname, Map<MarketMarble, Integer> marbles) {
        publishPlayerTempMarbles(nickname, marbles);
        changesBuffer.put(marbles, root + nickname + "/TempMarbles.json");
    }

    // Turn Actions
    public List<Actions> readPlayerTurnActions(String nickname) throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/TurnActions.json",
                Actions.class
        );
    }

    public void publishPlayerTurnActions(String nickname, List<Actions> actions) {
        submissionPublisher.submit(new TurnActionsUpdate(nickname, actions));
    }

    public void writePlayerTurnActions(String nickname, List<Actions> actions) {
        publishPlayerTurnActions(nickname, actions);
        changesBuffer.put(actions, root + nickname + "/TurnActions.json");
        writeBufferedChanges();
    }

    // Turn boolean flag
    public boolean readPlayerTurnFlag(String nickname) throws FileNotFoundException {
        return readValueFromFile(
                root + nickname + "/PlayerTurnFlag.json",
                Boolean.class
        );
    }

    public void publishPlayerTurnFlag(String nickname, boolean flag) {
        submissionPublisher.submit(new PlayerTurnFlagUpdate(nickname, flag));
    }

    public void writePlayerTurnFlag(String nickname, boolean flag) {
        publishPlayerTurnFlag(nickname, flag);
        changesBuffer.put(flag, root + nickname + "/PlayerTurnFlag.json");
        writeBufferedChanges();
    }

    // Position information
    public int readPlayerPosition(String nickname) throws FileNotFoundException {
        return readValueFromFile(
                root + nickname + "/Position.json",
                Integer.class
        );
    }

    public void publishPlayerPosition(String nickname, int position) {
        submissionPublisher.submit(new PlayerInfoUpdate(nickname, position));
    }

    public void writePlayerPosition(String nickname, int position) {
        publishPlayerPosition(nickname, position);
        changesBuffer.put(position, root + nickname + "/Position.json");
    }

    // Personal Board related information
    // Storages:
    // Warehouse Depots
    public Map<Resource, Integer> readPlayerWarehouseDepots(String nickname)
            throws FileNotFoundException {
        return readMapFromFile(root + nickname + "WarehouseDepots.json");
    }

    public void publishPlayerWarehouseDepots(String nickname, Map<Resource, Integer> resources) {
        submissionPublisher.submit(new DepotsUpdate(nickname, resources));
    }

    public void writePlayerWarehouseDepots(String nickname, Map<Resource, Integer> resources) {
        publishPlayerWarehouseDepots(nickname, resources);
        changesBuffer.put(resources, root + nickname + "WarehouseDepots.json");
    }

    // Strongbox
    public Map<Resource, Integer> readPlayerStrongbox(String nickname)
            throws FileNotFoundException {
        return readMapFromFile(root + nickname + "Strongbox.json");
    }

    public void publishPlayerStrongbox(String nickname, Map<Resource, Integer> resources) {
        submissionPublisher.submit(new StrongboxUpdate(nickname, resources));
    }

    public void writePlayerStrongbox(String nickname, Map<Resource, Integer> resources) {
        publishPlayerStrongbox(nickname, resources);
        changesBuffer.put(resources, root + nickname + "Strongbox.json");
    }

    // Faith Track
    public List<FavorStatus> readPlayerPopesFavors(String nickname) throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/PopesFavors.json",
                FavorStatus.class
        );
    }

    public void publishPlayerPopesFavors(String nickname, List<FavorStatus> popesFavors)
    public void writePlayerPopesFavors(String nickname, List<FavorStatus> popesFavors) {
        submissionPublisher.submit(new PopesFavorsUpdate(nickname, popesFavors));
        changesBuffer.put(popesFavors, createWriter(root + nickname + "/PopesFavors.json"));
    }

    public int readPlayerFaithMarkerPosition(String nickname) throws FileNotFoundException {
        return readValueFromFile(
                root + nickname + "/FaithMarkerPosition.json",
                Integer.class
        );
    }

    public void writePlayerFaithMarkerPosition(String nickname, int faithMarkerPosition) {
        submissionPublisher.submit(new FaithMarkerPositionUpdate(nickname, faithMarkerPosition));
        changesBuffer.put(faithMarkerPosition, createWriter(root + nickname +
                "/FaithMarkerPosition.json"));
    }

    public List<DevelopmentCard> readPlayerDevelopmentCardSlot(String nickname, int index)
            throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/DevelopmentCardsSlot" + index + ".json",
                DevelopmentCard.class
        );
    }

    public void writePlayerDevelopmentCardSlot(String nickname,
                                               int index,
                                               List<DevelopmentCard> cards) {
        List<DumbDevelopmentCard> dumbDevelopmentCards =
                cards.stream().map(DumbDevelopmentCard::new).collect(Collectors.toList());
        submissionPublisher.submit(new DevelopmentCardSlotUpdate(nickname, index,
                dumbDevelopmentCards));
        changesBuffer.put(cards,
                createWriter(root + nickname + "/DevelopmentCardsSlot" + index + ".json"));
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

    private void writeBufferedChanges() {
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
