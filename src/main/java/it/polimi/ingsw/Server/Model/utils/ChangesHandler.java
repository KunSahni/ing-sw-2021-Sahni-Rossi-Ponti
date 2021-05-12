package it.polimi.ingsw.server.model.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.network.message.renderable.updates.*;
import it.polimi.ingsw.network.message.renderable.updates.LeaderCardsUpdate;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboardpackage.FavorStatus;
import it.polimi.ingsw.network.message.renderable.Renderable;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Collectors;

// TODO: this has to be async to not interrupt server flow
// maybe not, it is important that the calls happen in the right order
public class ChangesHandler {
    private final String root;
    private final SubmissionPublisher<Renderable> submissionPublisher;
    private boolean isNewGame;
    private Map<Object, Writer> changesBuffer;

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

    public GameState readGameState() throws FileNotFoundException {
        return readValueFromFile(
                root + "GameState.json",
                GameState.class
        );
    }

    public void writeGameState(GameState gameState) {

    }

    public List<String> readNicknameList() throws FileNotFoundException {
        return readListFromFile(
                root + "Nicknames.json",
                String.class
        );
    }

    public void writeNicknameList(List<String> nicknameList) {
        changesBuffer.put(nicknameList, createWriter(root + "Nicknames.json"));
        writeBufferedChanges();
    }

    public List<MarketMarble> readMarket() throws FileNotFoundException {
        List<MarketMarble> rawMarbles = readListFromFile(
                root + "Market.json",
                MarketMarble.class
        );
        if (isNewGame)
            Collections.shuffle(rawMarbles);
        return rawMarbles;
    }

    public void writeMarket(List<MarketMarble> marketMarbles) {
        submissionPublisher.submit(new MarketUpdate(marketMarbles));
        changesBuffer.put(marketMarbles, createWriter(root + "Market.json"));
    }

    public void publishDevelopmentCardsBoard(List<DevelopmentCard> board) {
        List<DumbDevelopmentCard> dumbDevelopmentCards =
                board.stream().map(DevelopmentCard::convertToDumb).collect(Collectors.toList());
        submissionPublisher.submit(new DevelopmentCardsBoardUpdate(dumbDevelopmentCards));
    }

    public List<LeaderCard> readPlayerLeaderCards(String nickname) throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/PlayerLeaderCards.json",
                LeaderCard.class
        );
    }

    public void writePlayerLeaderCards(String nickname, List<LeaderCard> cards) {
        List<DumbLeaderCard> privateDumbLeaderCards =
                cards.stream().map(LeaderCard::convertToDumb).collect(Collectors.toList());
        List<DumbLeaderCard> publicDumbLeaderCards =
                privateDumbLeaderCards.stream()
                        .map(card -> (card.isActive()) ? card : null)
                        .collect(Collectors.toList());
        submissionPublisher.submit(new LeaderCardsBroadcastUpdate(nickname, publicDumbLeaderCards));
        submissionPublisher.submit(new LeaderCardsPrivateUpdate(nickname, privateDumbLeaderCards));
        changesBuffer.put(cards, createWriter(root + nickname + "/PlayerLeaderCards.json"));
    }

    public List<FavorStatus> readPlayerPopesFavors(String nickname) throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/PopesFavors.json",
                FavorStatus.class
        );
    }

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

    public Map<Resource, Integer> readPlayerWarehouseDepots(String nickname)
            throws FileNotFoundException {
        return readMapFromFile(root + nickname + "WarehouseDepots.json");
    }

    public void writePlayerWarehouseDepots(String nickname, Map<Resource, Integer> resources) {
        submissionPublisher.submit(new DepotsUpdate(nickname, resources));
        changesBuffer.put(resources, createWriter(root + nickname + "WarehouseDepots.json"));
    }

    public Map<Resource, Integer> readPlayerStrongbox(String nickname)
            throws FileNotFoundException {
        return readMapFromFile(root + nickname + "Strongbox.json");
    }

    public void writePlayerStrongbox(String nickname, Map<Resource, Integer> resources) {
        submissionPublisher.submit(new StrongboxUpdate(nickname, resources));
        changesBuffer.put(resources, createWriter(root + nickname + "Strongbox.json"));
    }

    public Map<Resource, Integer> readPlayerProxyStorage(String nickname)
            throws FileNotFoundException {
        return readMapFromFile(root + nickname + "ProxyStorage.json");
    }

    public void writePlayerProxyStorage(String nickname, Map<Resource, Integer> resources) {
        changesBuffer.put(resources, createWriter(root + nickname + "ProxyStorage.json"));
    }

    public List<LeaderCard> readLeaderCardsDeck() throws FileNotFoundException {
        List<LeaderCard> deck = readListFromFile(
                root + "LeaderCardsDeck.json",
                LeaderCard.class
        );
        if (isNewGame)
            Collections.shuffle(deck);
        return deck;
    }

    public void writeLeaderCardsDeck(List<LeaderCard> deck) {
        changesBuffer.put(deck, createWriter(root + "LeaderCardsDeck.json"));
    }

    public List<DevelopmentCard> readDevelopmentCardsDeck(Color color, Level level)
            throws FileNotFoundException {
        List<DevelopmentCard> rawDeck = readListFromFile(
                root + "developmentcardsboard/" + color + level + "deck.json",
                DevelopmentCard.class
        );
        if (isNewGame)
            Collections.shuffle(rawDeck);
        return rawDeck;
    }

    public void writeDevelopmentCardsDeck(Color color, Level level, List<DevelopmentCard> cards) {
        changesBuffer.put(cards, createWriter(root + "developmentcardsboard/" + color + level +
                "deck.json"))
    }

    public List<Actions> readTurnActions(String nickname) throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/TurnActions.json",
                Actions.class
        );
    }

    public void writeTurnActions(String nickname, List<Actions> actions) {
        changesBuffer.put(actions, createWriter(root + nickname + "/TurnActions.json"));
        writeBufferedChanges();
    }

    public Map<MarketMarble, Integer> readTempMarbles(String nickname) throws FileNotFoundException {
        return readMapFromFile(root + nickname + "/TempMarbles.json");
    }

    public void writeTempMarbles(String nickname, Map<MarketMarble, Integer> marbles) {
        submissionPublisher.submit(new TempMarblesUpdate(nickname, marbles));
        changesBuffer.put(marbles, createWriter(root + nickname + "/TempMarbles.json"));
    }

    public boolean readPlayerTurnFlag(String nickname) throws FileNotFoundException {
        return readValueFromFile(
                root + nickname + "/PlayerTurnFlag.json",
                Boolean.class
        );
    }

    public void writePlayerTurnFlag(String nickname, boolean flag) {
        changesBuffer.put(flag, createWriter(root + nickname + "/PlayerTurnFlag.json"));
        writeBufferedChanges();
    }

    public List<LeaderCard> readPlayerTempLeaderCards(String nickname)
            throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/TempLeaderCards.json",
                LeaderCard.class
        );
    }

    public void writePlayerTempLeaderCards(String nickname, List<LeaderCard> cards) {
        submissionPublisher.submit(new LeaderCardsChoiceUpdate(nickname,
                cards.stream().map(LeaderCard::convertToDumb).collect(Collectors.toList())));
        changesBuffer.put(cards, createWriter(root + nickname + "/TempLeaderCards.json"));
    }

    public int readPlayerPosition(String nickname) throws FileNotFoundException {
        return readValueFromFile(
                root + nickname + "/Position.json",
                Integer.class
        );
    }

    public void writePlayerPosition(String nickname, int position) {
        submissionPublisher.submit(new PlayerInfoUpdate(nickname, position));
        changesBuffer.put(position, createWriter(root + nickname + "/Position.json"));
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

    /**
     * Creates a Writer object for the given filepath, if the target file
     * throws an exception behavior of the method is undefined.
     *
     * @param filepath target file location.
     * @return Writer object.
     */
    private Writer createWriter(String filepath) {
        Writer outcome = null;
        try {
            outcome = new FileWriter(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outcome;
    }

    private void writeBufferedChanges() {
        Gson gson = new Gson();
        changesBuffer.forEach((object, writer) -> {
            gson.toJson(object, writer);
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        changesBuffer = new HashMap<>();
    }
}
