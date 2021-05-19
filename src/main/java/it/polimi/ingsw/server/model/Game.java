package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.utils.ChangesHandler;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.io.IOException;
import java.util.*;

public class Game {
    private GameState currentState;
    private final LinkedList<Player> players;
    private final Market market;
    private final LeaderCardsDeck leaderCardsDeck;
    private final DevelopmentCardsBoard developmentCardsBoard;
    private final ChangesHandler changesHandler;

    /**
     * Creates a game instance.
     * @param gameId game identifier.
     * @param nicknameList optional list of nicknames. When this parameter is null
     *                     the game gets restored from disk, when present a fresh
     *                     game instance is created.
     * @throws IOException exception thrown when game files or folders are corrupt.
     */
    public Game(int gameId, List<String> nicknameList) throws IOException {
        this.changesHandler = new ChangesHandler(gameId);
        if (nicknameList != null) {
            changesHandler.createGameFilesFromBlueprint(nicknameList);
        } else {
            nicknameList = changesHandler.readNicknameList();
        }
        this.players = new LinkedList<>();
        for (String nickname : nicknameList) {
            players.add(changesHandler.readPlayer(nickname));
        }
        this.currentState = changesHandler.readGameState();
        this.market = this.changesHandler.readMarket();
        this.leaderCardsDeck = this.changesHandler.readLeaderCardsDeck();
        this.developmentCardsBoard = this.changesHandler.readDevelopmentCardsBoard();

    }

    /**
     * Returns the current state of the game as an element of the GameState
     * enum.
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Subscribes a RemoteView to the game's model updates.
     * @param remoteView RemoteView object that will receive all
     *                   model publishes.
     */
    public void subscribe(RemoteView remoteView) {
        changesHandler.subscribe(remoteView);
    }

    /**
     * The returned list of players contains all Player objects in
     * the current game. The list is sorted, the first element is the
     * Player with the Inkwell marker, in position 1. If the Inkwell
     * has not been assigned yet, the returned list is unordered and
     * all positions are set to 0.
     * @return list of Players.
     */
    public List<Player> getPlayerList() {
        return new ArrayList<>(players);
    }

    /**
     * Returns the only player in the game who has a matching nickname.
     * @param nickname identifier for the wanted Player.
     * @return Player instance, null if a nickname not present in the game
     * gets passed as parameter.
     */
    public Player getPlayer(String nickname) {
        Optional<Player> target = players.stream()
                .filter(player -> player.getNickname().equals(nickname))
                .findAny();
        return target.orElse(null);
    }

    /**
     * @return instance of the Player currently playing its turn.
     */
    public Player getCurrentTurnPlayer() {
        return players.stream().filter(Player::isPlayersTurn).findFirst().get();
    }

    /**
     * Sorts the players' list in ascending order. Must be called only after
     * the Inkwell has been assigned otherwise the sorting process is meaningless.
     */
    public void sortPlayers() {
        Collections.sort(players);
    }

    /**
     * Marks the player as connected to the game.
     * @param nickname Player identifier.
     */
    public void connect(String nickname) {
        getPlayer(nickname).connect();
        changesHandler.publishModel(nickname, this);
    }

    /**
     * Marks the player as disconnected from the game.
     * @param nickname Player identifier.
     */
    public void disconnect(String nickname) {
        getPlayer(nickname).disconnect();
        changesHandler.publishPlayer(getPlayer(nickname));
    }

    /**
     * Updates the current GameState.
     * @param gameState specified GameState.
     */
    public void setState(GameState gameState) {
        currentState = gameState;
        changesHandler.writeGameState(currentState);
        changesHandler.flushBufferToDisk();
    }

    /**
     * @return returns the only Market instance in the game.
     */
    public Market getMarket() {
        return market;
    }

    /**
     * @return returns the only LeaderCardsDeck instance in the game.
     */
    public LeaderCardsDeck getLeaderCardsDeck() {
        return leaderCardsDeck;
    }

    /**
     * @return returns the only DevelopmentCardsBoard instance in the game.
     */
    public DevelopmentCardsBoard getDevelopmentCardsBoard() {
        return developmentCardsBoard;
    }
}
