package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.utils.ChangesHandler;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Game {
    private final int gameId;
    private GameState currentState;
    private final LinkedList<Player> players;
    private final Market market;
    private final LeaderCardsDeck leaderCardsDeck;
    private final DevelopmentCardsBoard developmentCardsBoard;
    private final ChangesHandler changesHandler;

    public Game(int gameId, List<String> nicknameList) throws FileNotFoundException {
        this.gameId = gameId;
        this.changesHandler = new ChangesHandler(gameId);
        if (nicknameList != null) {
            changesHandler.createGameFilesFromBlueprint(nicknameList);
        } else {
            nicknameList = changesHandler.readNicknameList();
        }
        this.currentState = changesHandler.readGameState();
        this.market = new Market(changesHandler);
        this.leaderCardsDeck = new LeaderCardsDeck(changesHandler);
        this.developmentCardsBoard = new DevelopmentCardsBoard(changesHandler);
        this.players = new LinkedList<>();
        nicknameList.forEach(nickname -> players.add(new Player(changesHandler, nickname)));
    }

    public void subscribe(RemoteView remoteView) {
        changesHandler.subscribe(remoteView);
    }

    public Player getPlayer(String nickname) {
        Optional<Player> target = players.stream()
                .filter(player -> player.getNickname().equals(nickname))
                .findAny();
        return target.orElse(null);
    }

    public void connect(String nickname) {
        getPlayer(nickname).connect();
    }

    public void disconnect(String nickname) {
        getPlayer(nickname).disconnect();
    }

    public void setNextState(GameState gameState) {
        currentState = gameState;
        changesHandler.writeGameState(currentState);
    }

    public Market getMarket() {
        return market;
    }

    public LeaderCardsDeck getLeaderCardsDeck() {
        return leaderCardsDeck;
    }

    public DevelopmentCardsBoard getDevelopmentCardsBoard() {
        return developmentCardsBoard;
    }
}
