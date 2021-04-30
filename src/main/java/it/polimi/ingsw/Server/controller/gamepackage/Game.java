package it.polimi.ingsw.server.controller.gamepackage;

import it.polimi.ingsw.server.controller.message.action.*;
import it.polimi.ingsw.server.controller.message.action.gameaction.DiscardTwoDevelopmentCardsAction;
import it.polimi.ingsw.server.controller.message.action.gameaction.MoveBlackCrossAction;
import it.polimi.ingsw.server.controller.message.action.gameaction.ResetAndMoveAction;
import it.polimi.ingsw.server.controller.message.action.playeraction.EndTurnAction;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.controller.gamestates.*;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.personalboardpackage.SinglePlayerFaithTrack;
import it.polimi.ingsw.server.network.ErrorMessage;

import java.util.*;
import java.util.concurrent.Flow.*;
import java.util.stream.Collectors;

/**
 * This class represents a game and controls all the turns/game logic
 */
public class Game implements Subscriber<Action> {
    private final int gameID;
    private final DevelopmentCardsBoard developmentCardsBoard;
    private final Market market;
    private final LeaderCardsDeck leaderCardsDeck;
    private final ActionTokenDeck actionTokenDeck;
    private GameState currentState;
    private Queue<Player> players;
    private final int size;
    private Subscription subscription;
    private MyOwnPublisher myOwnPublisher;
    private int waitingForPlayers;

    /**
     * @param gameID an unique ID that identifies the game
     */
    public Game(int gameID, int size) { //todo: pass gameID to each Model element so that it can restore data
        this.gameID = gameID;
        this.developmentCardsBoard = new DevelopmentCardsBoard();
        this.market = new Market();
        this.leaderCardsDeck = new LeaderCardsDeck();
        this.actionTokenDeck = size==1 ? null : new ActionTokenDeck();
        this.size = size;
        this.players = new LinkedList<>();
    }

    /**
     * This method moves each players' faith marker by one except the one who just discarded a resource
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
    public void setNextState(GameState nextState) {
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
     * @param player the Player that needs to be added
     */
    public void addPlayer(Player player){
        players.add(
                player
        );
    }

    public void managePregame(){
        waitingForPlayers = size;
        dealLeaderCards();
        waitForAllPlayersChoice();
        waitingForPlayers = size;
        assignInkwell();
        waitForAllPlayersChoice();
        setNextState(GameState.IN_GAME);
    }

    /**
     * Deal 4 leader cards to each player in the game
     */
    private void dealLeaderCards() {
        getPlayers().forEach(p -> p.addLeaderCards(leaderCardsDeck.popFour()));
    }

    /**
     * Assign turn position to each player
     */
    private void assignInkwell() {
        List<Player> playerList = getPlayers();
        Collections.shuffle(playerList);
        for(int i = 1; i <= size; i++) {
            playerList.get(i).setPosition(i);
        }
        sortPlayers();
    }

    /**
     * Wait for all players' views to have executed their choices.
     * This method will wait for all leader cards to be picked and
     * for all extra resources to be picked.
     */
    private void waitForAllPlayersChoice() {
        while(waitingForPlayers > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void manageGameFinished(){

    }

    /**
     * This method starts the Game and keeps it running as long as it isn't finished
     */
    public void run(){
        currentState = GameState.PRE_GAME;
        managePregame();
    }

    public void setListener(){

    }
    /**
     * This method is used to reorder the Players after each one of them is assigned to a position
     */
    public void sortPlayers(){
        players = (Queue<Player>) players.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Method invoked prior to invoking any other Subscriber
     * methods for the given Subscription. If this method throws
     * an exception, resulting behavior is not guaranteed, but may
     * cause the Subscription not to be established or to be cancelled.
     *
     * <p>Typically, implementations of this method invoke {@code
     * subscription.request} to enable receiving items.
     *
     * @param subscription a new subscription
     */
    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    /**
     * Method invoked with a Subscription's next item.  If this
     * method throws an exception, resulting behavior is not
     * guaranteed, but may cause the Subscription to be cancelled.
     *
     * @param item the item
     */
    @Override
    public void onNext(Action item) {
        switch (item.getClass().getSimpleName()) {
            case "MoveBlackCrossAction":
            case "ResetAndMoveAction":
            case "DiscardDevelopmentCardsAction":
                onNext(new EndTurnAction());
                break;
            case "ResourcePregameAction":
            case "LeaderCardsChoiceAction":
                waitingForPlayers--;
                notifyAll();
            default:
                if(item.isAllowed()){
                    item.execute();
                    /*if(item.getPlayer().getPersonalBoard().getDevelopmentCardsCount() == 7 || item.getPlayer().getPersonalBoard().getFaithTrack().getFaithMarkerPosition() == 20)
                        setNextState(GameState.LAST_ROUND);
                    if(item.getPlayer().getPosition() == size && currentState.equals(GameState.LAST_ROUND)){
                        setNextState(GameState.GAME_FINISHED);
                        manageGameFinished();*/
                }else
                    myOwnPublisher.submit(new ErrorMessage());
        }
        subscription.request(1);
    }

    private Action pickActionToken(){
        ActionToken actionToken = actionTokenDeck.pop();

        if(actionToken.equals(ActionToken.MOVEBYTWO))
            return new MoveBlackCrossAction((SinglePlayerFaithTrack) players.peek().getPersonalBoard().getFaithTrack());
        else if(actionToken.equals((ActionToken.MOVEANDSHUFFLE)))
            return new ResetAndMoveAction(actionTokenDeck, (SinglePlayerFaithTrack) players.peek().getPersonalBoard().getFaithTrack());
        else
            return new DiscardTwoDevelopmentCardsAction(developmentCardsBoard, actionToken);
    }

    /**
     * Method invoked upon an unrecoverable error encountered by a
     * Publisher or Subscription, after which no other Subscriber
     * methods are invoked by the Subscription.  If this method
     * itself throws an exception, resulting behavior is
     * undefined.
     *
     * @param throwable the exception
     */
    @Override
    public void onError(Throwable throwable) {

    }

    /**
     * Method invoked when it is known that no additional
     * Subscriber method invocations will occur for a Subscription
     * that is not already terminated by error, after which no
     * other Subscriber methods are invoked by the Subscription.
     * If this method throws an exception, resulting behavior is
     * undefined.
     */
    @Override
    public void onComplete() {

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

    public GameState getCurrentState() {
        return currentState;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }



}