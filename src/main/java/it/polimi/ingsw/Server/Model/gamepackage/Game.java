package it.polimi.ingsw.server.model.gamepackage;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gamestates.*;

import java.util.Queue;

/**
 * This class represents a game and controls all the turns/game logic
 */

public class Game {
    private final int gameID;
    private final int inkwell;  //todo: should inkwell be of type Player?
    private final DevelopmentCardsBoard developmentCardsBoard;
    private final Market market;
    private final LeaderCardsDeck leaderCardsDeck;
    private final ActionTokenDeck actionTokenDeck;
    private AbstractGameState currentState;
    private Turn currentTurn;
    private Queue<Player> players;

    /**
     * @param gameID an unique ID that identifies the game
     * @param size the number of players who will be participating in this game
     */
    public Game(int gameID, int size) {
        this.gameID = gameID;
        this.inkwell = (int) (Math.random()*(size-1) + 1);
        this.developmentCardsBoard = new DevelopmentCardsBoard();
        this.market = new Market();
        this.leaderCardsDeck = new LeaderCardsDeck();
        this.actionTokenDeck = new ActionTokenDeck();
        this.currentState = new Pregame();
    }

    public int getGameID() {
        return gameID;
    }

    public int getInkwell() {
        return inkwell;
    }

    public DevelopmentCardsBoard getDevelopmentCardsBoard() {
        return developmentCardsBoard;
    }

    public Market getMarket() {
        return market;
    }

    public ActionTokenDeck getActionTokenDeck() {
        return actionTokenDeck;
    }


    /**
     *This method sets a new Turn for the next player
    */
    public void nextTurn() {
        players.add(players.remove());
        currentTurn = new Turn(this, players.peek());
    }

    /**
     * This method moves each players' fait marker by one except the on who just discarded a resource
    * @param excludedPlayer the player who discarded the resource
    */
    public void moveOtherMarkers(Player excludedPlayer) {
        players.stream().filter(
                player -> !player.equals(excludedPlayer)
        ).forEach(
                player -> player.getPersonalBoard().getFaithTrack().moveMarker()
        );
    }

    /**
    * @param nextState the state in which the game should be next
    */
    public void setNextState(AbstractGameState nextState) {
        currentState = nextState;
    }

    /**
    * @param position the number of the pope's favor (1, 2 or 3) which players should flip or discard
    */
    public void flipOtherPopesFavor(int position) {
        players.forEach(
                player -> player.getPersonalBoard().getFaithTrack().flipPopesFavor(position)
        );
    }

    /**
     * @param nickname the nickname of the Player that needs to be added
     */
    public void addPlayer(String nickname){
        players.add(
                new Player(nickname, players.size(), this, leaderCardsDeck.popFour())
        );
    }

}