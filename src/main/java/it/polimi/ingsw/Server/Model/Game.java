package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.utils.ChangesHandler;
import it.polimi.ingsw.server.remoteview.RemoteView;

import java.io.FileNotFoundException;
import java.util.*;

public class Game {
    private GameState currentState;
    private final LinkedList<Player> players;
    private final Market market;
    private final LeaderCardsDeck leaderCardsDeck;
    private final DevelopmentCardsBoard developmentCardsBoard;
    private final ChangesHandler changesHandler;

    public Game(int gameId, List<String> nicknameList) throws FileNotFoundException {
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

    public GameState getCurrentState() {
        return currentState;
    }

    public void subscribe(RemoteView remoteView) {
        changesHandler.subscribe(remoteView);
    }

    public List<Player> getPlayerList() {
        return new ArrayList<>(players);
    }

    // Always sorted after inkwell assignment
    public Player getPlayer(String nickname) {
        Optional<Player> target = players.stream()
                .filter(player -> player.getNickname().equals(nickname))
                .findAny();
        return target.orElse(null);
    }

    public void sortPlayers() {
        Collections.sort(players);
    }

    public void connect(String nickname) {
        Player player = getPlayer(nickname);
        player.connect();
        changesHandler.publishMarket(market);
        changesHandler.publishDevelopmentCardsBoard(developmentCardsBoard);
        players.forEach(npc -> {
            changesHandler.publishPlayer(npc);
            changesHandler.publishPersonalBoard(npc.getNickname(), npc.getPersonalBoard());
        });
    }

    public void disconnect(String nickname) {
        getPlayer(nickname).disconnect();
    }

    public void setState(GameState gameState) {
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
