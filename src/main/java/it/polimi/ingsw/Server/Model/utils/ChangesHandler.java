package it.polimi.ingsw.server.model.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboardpackage.FavorStatus;
import it.polimi.ingsw.network.message.renderable.Renderable;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.SubmissionPublisher;

// TODO: this has to be async to not interrupt server flow
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

    public void writePlayerAction(Actions playerAction) {

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

    }

    public List<LeaderCard> readPlayerLeaderCards(String nickname) throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/PlayerLeaderCards.json",
                LeaderCard.class
        );
    }

    public List<FavorStatus> readPlayerPopesFavors(String nickname) throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/PopesFavors.json",
                FavorStatus.class
        );
    }

    public void writePlayerPopesFavors(String nickname, List<FavorStatus> popesFavors) {

    }

    public int readPlayerFaithMarkerPosition(String nickname) throws FileNotFoundException {
        return readValueFromFile(
                root + nickname + "/FaithMarkerPosition.json",
                Integer.class
        );
    }

    public void writePlayerFaithMarkerPosition(String nickname, int faithMarkerPosition) {

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

    }

    public Map<Resource, Integer> readPlayerWarehouseDepots(String nickname)
            throws FileNotFoundException {
        return readMapFromFile(root + nickname + "WarehouseDepots.json");
    }

    public void writePlayerWarehouseDepots(String nickname, Map<Resource, Integer> resources) {

    }

    public Map<Resource, Integer> readPlayerStrongbox(String nickname)
            throws FileNotFoundException {
        return readMapFromFile(root + nickname + "Strongbox.json");
    }

    public void writePlayerStrongbox(String nickname, Map<Resource, Integer> map) {

    }

    public Map<Resource, Integer> readPlayerProxyStorage(String nickname)
            throws FileNotFoundException {
        return readMapFromFile(root + nickname + "ProxyStorage.json");
    }

    public void writePlayerProxyStorage(String nickname, Map<Resource, Integer> map) {

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

    public List<Actions> readTurnActions(String nickname) throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/TurnActions.json",
                Actions.class
        );
    }

    public void writeTurnActions(String nickname, List<Actions> actions) {

    }

    public Map<MarketMarble, Integer> readTempMarbles(String nickname) throws FileNotFoundException {
        return readMapFromFile(root + nickname + "/TempMarbles.json");
    }

    public void writeTempMarbles(String nickname, List<MarketMarble> marbles) {

    }

    public boolean readPlayerTurnFlag(String nickname) throws FileNotFoundException {
        return readValueFromFile(
                root + nickname + "/PlayerTurnFlag.json",
                Boolean.class
        );
    }

    public void writePlayerTurnFlag(String nickname, boolean flag) {

    }

    public List<LeaderCard> readPlayerTempLeaderCards(String nickname)
            throws FileNotFoundException {
        return readListFromFile(
                root + nickname + "/TempLeaderCards.json",
                LeaderCard.class
        );
    }

    public void writePlayerTempLeaderCards(String nickname, List<LeaderCard> cards) {

    }

    public int readPlayerPosition(String nickname) throws FileNotFoundException {
        return readValueFromFile(
                root + nickname + "/Position.json",
                Integer.class
        );
    }

    public void writePlayerPosition(String nickname, int position) {

    }

    /**
     * Utility method used to parse any single-value json file into
     * a variable of the type specified via parameter.
     * @param filepath file disk location.
     * @param classType classType of the required output
     * @param <T> output type
     * @return the value contained in the file.
     * @throws FileNotFoundException exception thrown when the target
     * file is not present on disk.
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
            throws FileNotFoundException{
        Type mapType = new TypeToken<Map<T, K>>(){}.getType();
        return new Gson().fromJson(new JsonReader(new FileReader(filepath)),
                mapType);
    }

    public void subscribe(RemoteView remoteView) {
        submissionPublisher.subscribe(remoteView);
    }
}
