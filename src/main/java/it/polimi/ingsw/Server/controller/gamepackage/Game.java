package it.polimi.ingsw.server.controller.gamepackage;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.controller.gamestates.*;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.personalboardpackage.SinglePlayerFaithTrack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * This class represents a game and controls all the turns/game logic
 */
public class Game {
    private final int gameID;
    private final DevelopmentCardsBoard developmentCardsBoard;
    private final Market market;
    private final LeaderCardsDeck leaderCardsDeck;
    private final ActionTokenDeck actionTokenDeck;
    private AbstractGameState currentState;
    private Turn currentTurn;
    private Queue<Player> players;
    private final int size;

    /**
     * @param gameID an unique ID that identifies the game
     */
    public Game(int gameID, int size) {
        this.gameID = gameID;
        this.developmentCardsBoard = new DevelopmentCardsBoard();
        this.market = new Market();
        this.leaderCardsDeck = new LeaderCardsDeck();
        this.actionTokenDeck = new ActionTokenDeck();
        this.size = size;
        this.players = new LinkedList<>();
    }

    /**
     *This method sets a new Turn for the next Player
    */
    public void nextTurn() {
        currentTurn = new Turn(this, players.peek());
        players.add(players.remove());
    }

    /**
     *This method sets a new Turn for the next player
     */
    public void nextSinglePlayerTurn() {
        currentTurn = new SinglePlayerTurn(this, players.peek());
    }

    /**
     * This method moves each players' fait marker by one except the on who just discarded a resource
    * @param excludedPlayer the player who discarded the resource
    */
    public void moveOtherMarkers(Player excludedPlayer) {
        if(players.size()==1)
            ((SinglePlayerFaithTrack) players.peek().getPersonalBoard().getFaithTrack()).moveBlackCross();
        else
            players.stream().filter(
                    player -> !player.equals(excludedPlayer)
            ).forEach(
                    player -> player.getPersonalBoard().getFaithTrack().moveMarker(1)
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
    public void startVaticanReport(int position) {
        players.forEach(
                player -> player.getPersonalBoard().getFaithTrack().flipPopesFavor(position)
        );
    }

    /**
     * @param nickname the nickname of the Player that needs to be added
     */
    public void addPlayer(Player player){  //todo: add View
        players.add(
                player
        );
    }

    /**
     * This method starts the Game and keeps it running as long as it isn't finished
     */
    public void run(){
        setNextState(new Pregame(this));
        while (!(currentState instanceof GameFinished))
            currentState.run();
        //todo: e poi?
        //calcolo punteggi?
    }

    /**
     * This method is used to reorder the Players after each one of them is assigned to a position
     */
    public void sortPlayers(){
        players = (Queue<Player>) players.stream().sorted().collect(Collectors.toList());
    }

    public int getGameSize(){
        return size;
    }

    public int getGameID() {
        return gameID;
    }

    public DevelopmentCardsBoard getDevelopmentCardsBoard() {
        return developmentCardsBoard;
    }

    public Market getMarket() {
        return market;
    }

    public LeaderCardsDeck getLeaderCardsDeck() {
        return leaderCardsDeck;
    }

    public ActionTokenDeck getActionTokenDeck() {
        return actionTokenDeck;
    }

    public AbstractGameState getCurrentState() {
        return currentState;
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

}