package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.Renderable;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboard.FavorStatus;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.SubmissionPublisher;

/**
 * This class contains a copy of some elements of the Model contained on the Server,
 * each element is updated thanks to the Renderable sent by the Server
 * and each element is rendered graphically either on CLI or GUI
 */
public class DumbModel {
    private final List<DumbPersonalBoard> personalBoards;
    private final DumbMarket market;
    private final DumbDevelopmentCardsBoard developmentCardsBoard;
    private final DumbActionTokenDeck actionTokenDeck;  //todo: potrebbe non servire visto che fa tutto il server
    private int size;
    private int gameID;
    private GameState gameState;
    protected String nickname;
    private ArrayList<ExecutedActions> turnActions;
    private final UpdatesHandler updatesHandler;

    public DumbModel(UI ui) {
        personalBoards = new ArrayList<>();
        market = DumbMarket.getInstance();
        developmentCardsBoard = DumbDevelopmentCardsBoard.getInstance();
        actionTokenDeck = (size<=1) ? null: DumbActionTokenDeck.getInstance();
        updatesHandler = new UpdatesHandler(ui);
    }
    /**
     * This method creates a new PersonalBoard inside the DumbModel base on the passed parameters
     * @param nickname the nickname of the player that needs to be added
     * @param position the position of the player on the "table"
     */
    public void addPersonalBoard(String nickname, int position){
        personalBoards.add(new DumbPersonalBoard(nickname, position, size==1));
    }

    /**
     * This method creates a new PersonalBoard inside the DumbModel base on the passed parameters
     * @param nickname the nickname of the player that needs to be added
     * @param position the position of the player on the "table"
     * @param updatedTurnStatus true if the player is starting a turn, false if he's finishing it
     * @param updatedConnectionStatus true if the player is connected to the server, false if he's disconnected from the server
     */
    public void updatePersonalBoard(String nickname, int position, boolean updatedTurnStatus, boolean updatedConnectionStatus){
        Optional.ofNullable(getPersonalBoard(nickname)).ifPresentOrElse(
                dumbPersonalBoard -> {
                    dumbPersonalBoard.updateConnectionStatus(updatedConnectionStatus);
                    dumbPersonalBoard.updateTurnStatus(updatedTurnStatus);
                },
                ()->{
                    addPersonalBoard(nickname, position);
                    updatePersonalBoard(nickname, position, updatedTurnStatus, updatedConnectionStatus);
                }
        );
    }

    /**
     * This method is called every time the client receives an update regarding DumbDevelopmentCardsBoard.
     * @param updatedDevelopmentCardsBoard an updated version of the game's DumbDevelopmentCardsBoard
     */
    public void updateDevelopmentCardsBoard(DumbDevelopmentCard[][] updatedDevelopmentCardsBoard){
        getDevelopmentCardsBoard().updateBoard(updatedDevelopmentCardsBoard);
    }

    /**
     * This method is called every time the client receives an update regarding DumbActionTokenDeck.
     * @param updatedActionTokenDeck an updated version of the game's DumbActionTokenDeck
     */
    public void updateActionTokenDeck(List<ActionToken> updatedActionTokenDeck){
        getActionTokenDeck().updateActionTokens(updatedActionTokenDeck);
    }

    /**
     * This method is called every time the client receives an update regarding DumbMarket.
     * @param updatedMarket an updated version of the game's DumbMarket
     */
    public void updateMarket(MarketMarble[][] updatedMarket, MarketMarble updatedExtraMarble){
        getMarket().updateMarket(updatedMarket);
        getMarket().updateExtraMarble(updatedExtraMarble);
    }

    /**
     * This method is called every time the client receives an update regarding a player's faithTrackPosition.
     * @param nickname the nickname of the player whose position will be updated
     * @param updatedFaithTrackPosition the updated position of his faithMarker
     * @param updatedPopesFavors the updated list of popesFavors of the player
     */
    public void updateFaithTrack(String nickname, int updatedFaithTrackPosition, List<FavorStatus> updatedPopesFavors){
        Optional.ofNullable(getPersonalBoard(nickname)).ifPresent(
                dumbPersonalBoard -> {
                    dumbPersonalBoard.getFaithTrack().updateFaithMarkerPosition(updatedFaithTrackPosition);
                    dumbPersonalBoard.getFaithTrack().updatePopesFavors(updatedPopesFavors);
                }
        );
    }

    /**
     * This method is called every time the client receives an update regarding the faith track in a single player game
     * @param nickname the nickname of the player whose position will be updated
     * @param updatedFaithTrackPosition the updated position of his faithMarker
     * @param updatedPopesFavors the updated list of popesFavors of the player
     * @param updatedBlackCrossPosition the updated position of the black cross on the faith track
     */
    public void updateSinglePlayerFaithTrack(String nickname, int updatedFaithTrackPosition, List<FavorStatus> updatedPopesFavors, int updatedBlackCrossPosition){
        if(getSize()==1) {
            updateFaithTrack(nickname, updatedFaithTrackPosition, updatedPopesFavors);
            Optional.ofNullable(getPersonalBoard(nickname)).ifPresent(
                    dumbPersonalBoard -> ((DumbSinglePlayerFaithTrack) dumbPersonalBoard.getFaithTrack()).updateBlackCrossPosition(updatedBlackCrossPosition)
            );
        }
    }

    /**
     * This method is called every time the client receives an update regarding hiw own leaderCards.
     * @param updatedLeaderCards the updated list of leaderCards held by the player
     */
    public void updateLeaderCards(List<DumbLeaderCard> updatedLeaderCards){
        Optional.ofNullable(getPersonalBoard(nickname)).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.updateLeaderCards(updatedLeaderCards)
        );
    }

    /**
     * This method is called every time the client receives an update regarding another player's leaderCards.
     * @param nickname the nickname of the player whose leaderCards will be updated
     * @param updatedLeaderCards the updated list of leaderCards held by the player
     */
    public void updateLeaderCards(String nickname, List<DumbLeaderCard> updatedLeaderCards){
        Optional.ofNullable(getPersonalBoard(nickname)).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.updateLeaderCards(updatedLeaderCards)
        );
    }

    /**
     * This method is called every time the client receives an update regarding a player's DumbDevelopmentCardSlot.
     * @param nickname the nickname of the player whose DumbDevelopmentCardSlot will be updated
     * @param position the position of the DumbDevelopmentCardSlot which needs to be updated
     * @param updatedDevelopmentCardSlot an updated list of DumbDevelopmentCard contained in the selected DumbDevelopmentCardSlot
     */
    public void updateDevelopmentCardSlot(String nickname, int position, List<DumbDevelopmentCard> updatedDevelopmentCardSlot){
        Optional.ofNullable(getPersonalBoard(nickname)).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.getDevelopmentCardSlots().get(position-1).updateDevelopmentCards(updatedDevelopmentCardSlot)
        );
    }

    /**
     * This method is called every time the client receives an update regarding a player's store leader card
     * @param nickname the nickname of the player whose store leader card will be updated
     * @param leaderCard the store leader card whose storage changed
     */
    public void updateStoreLeaderCardStorage(String nickname, DumbStoreLeaderCard leaderCard){
        Optional.ofNullable(getPersonalBoard(nickname)).flatMap(dumbPersonalBoard -> dumbPersonalBoard.getLeaderCards()
                .stream()
                .filter(
                        dumbLeaderCard -> dumbLeaderCard instanceof DumbStoreLeaderCard && dumbLeaderCard.equals(leaderCard)
                ).findFirst()).ifPresent(dumbLeaderCard -> ((DumbStoreLeaderCard) dumbLeaderCard).updateStoredResources(leaderCard.getStoredResources()));
    }

    /**
     * This method is called every time the client receives an update regarding a player's depots.
     * @param nickname the nickname of the player whose depots will be updated
     * @param updatedDepots an updated Map of Resources contained in the player's depots
     */
    public void updateDepots(String nickname, Map<Resource, Integer> updatedDepots){
        Optional.ofNullable(getPersonalBoard(nickname)).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.getDepots().updateStoredResources(updatedDepots)
        );
    }


    /**
     * This method is called every time the client receives an update regarding a player's strongbox.
     * @param nickname the nickname of the player whose strongbox will be updated
     * @param updatedStrongbox an updated Map of Resources contained in the player's strongbox
     */
    public void updateStrongbox(String nickname, Map<Resource, Integer> updatedStrongbox){
        Optional.ofNullable(getPersonalBoard(nickname)).ifPresent(
                dumbPersonalBoard -> dumbPersonalBoard.getStrongbox().updateStoredResources(updatedStrongbox)
        );
    }

    /**
     * This method is called every time the client receives an update regarding the state of a game
     * @param updatedGameState the updated state of a game
     */
    public void updateGameState(GameState updatedGameState) {
        this.gameState = updatedGameState;
        if(updatedGameState.equals(GameState.ASSIGNED_INKWELL))
            Optional.ofNullable(getPersonalBoard(nickname)).ifPresent(
                    dumbPersonalBoard -> {
                        if(dumbPersonalBoard.getDepots().getResourceCount() != dumbPersonalBoard.getPosition()/2)
                            return;
                            //todo: come faccio capire alla ui che deve mostrare questa cosa?
                            //renderResourcePregameChoice(dumbPersonalBoard.getPosition()/2);
                    }
            );
    }

    /**
     * This method is called every time the client receives an update regarding the actions he has performed so far
     * @param updatedTurnActions a list of all the valid actions requested by the client
     */
    public void updateTurnActions(List<ExecutedActions> updatedTurnActions){
        this.turnActions = new ArrayList<>(updatedTurnActions);
    }

    /**
     * This method updates the gameID saved in the client's model
     * @param updatedGameID the gameId to which the client is connected
     */
    public void updateGameID(int updatedGameID) {
        this.gameID = updatedGameID;
    }

    /**
     * This method updates the size of the game in the client's model
     * @param updatedSize the size of the game to which the client is connected
     */
    public void updateGameSize(int updatedSize) {
        this.size = updatedSize;
    }

    /**
     * @param nickname a player whose DumbPersonalBoard need to be retrieved
     * @return the DumbPersonalBoard corresponding to the player with the nickname given as a parameter
     */
    public DumbPersonalBoard getPersonalBoard(String nickname){
        return getPersonalBoards()
                .stream()
                .filter(
                        dumbPersonalBoard -> dumbPersonalBoard.getNickname().equals(nickname)
                ).findFirst().orElse(null);
    }


    public List<DumbPersonalBoard> getPersonalBoards() {
        return personalBoards;
    }

    public DumbMarket getMarket() {
        return market;
    }

    public DumbDevelopmentCardsBoard getDevelopmentCardsBoard() {
        return developmentCardsBoard;
    }

    public DumbActionTokenDeck getActionTokenDeck() {
        return actionTokenDeck;
    }

    public int getSize() {
        return size;
    }

    public int getGameID() {
        return gameID;
    }

    public UpdatesHandler getUpdatesHandler() {
        return updatesHandler;
    }

    public GameState getGameState() {
        return gameState;
    }

    public class UpdatesHandler implements Subscriber<Renderable>{
        private final ConcurrentLinkedQueue<Renderable> updatesQueue;
        private Subscription subscription;
        private final SubmissionPublisher<Renderable> publisher;

        public UpdatesHandler(UI ui) {
            updatesQueue = new ConcurrentLinkedQueue<>();
            publisher = new SubmissionPublisher<>();
            publisher.subscribe(ui);
        }

        /**
         * This method call update() on the head of updatesQueue and sends it to the UI
         */
        private synchronized void elaborateUpdatesQueue(){
            Renderable usedItem = updatesQueue.remove();
            usedItem.update(DumbModel.this);
            publisher.submit(usedItem);
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
        public void onNext(Renderable item) {
            new Thread(() -> {
                updatesQueue.add(item);
                elaborateUpdatesQueue();
            }).start();
            subscription.request(1);
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
    }
}
