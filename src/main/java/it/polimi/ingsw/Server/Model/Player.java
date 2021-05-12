package it.polimi.ingsw.server.model;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import it.polimi.ingsw.server.controller.action.playeraction.*;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;
import it.polimi.ingsw.server.model.utils.Actions;
import it.polimi.ingsw.server.model.utils.ChangesHandler;

/**
 * This class represents a Player
 */

public class Player implements Comparable<Player> {
    private final String nickname;
    private int position;
    private final PersonalBoard personalBoard;
    private List<LeaderCard> tempLeaderCards;
    private List<Actions> performedActions;
    private Map<MarketMarble, Integer> tempMarbles;
    private boolean isPlayersTurn;
    private boolean isConnected;
    private final ChangesHandler changesHandler;

    public Player(ChangesHandler changesHandler, String nickname)
            throws FileNotFoundException {
        this.nickname = nickname;
        this.personalBoard = new PersonalBoard(changesHandler, nickname);
        this.performedActions = changesHandler.readTurnActions(nickname);
        this.tempMarbles = changesHandler.readTempMarbles(nickname);
        this.isPlayersTurn = changesHandler.readPlayerTurnFlag(nickname);
        this.tempLeaderCards = changesHandler.readPlayerTempLeaderCards(nickname);
        this.position = changesHandler.readPlayerPosition(nickname);
        this.isConnected = false;
        this.changesHandler = changesHandler;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPosition() {
        return position;
    }

    public PersonalBoard getPersonalBoard() {
        return personalBoard;
    }

    public List<LeaderCard> getTempLeaderCards() {
        return List.copyOf(tempLeaderCards);
    }

    public Map<MarketMarble, Integer> getTempMarbles() {
        return Map.copyOf(tempMarbles);
    }

    public boolean isPlayersTurn() {
        return isPlayersTurn;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void connect() {
        isConnected = true;
        changesHandler.playerConnected(nickname);
    }

    public void disconnect() {
        isConnected = false;
        changesHandler.playerDisconnected(nickname);
    }

    /**
     * This method assigns 4 LeaderCards to the Player from which he picks two
     *
     * @param leaderCards a list of 4 LeaderCards
     */
    public void setTempLeaderCards(List<LeaderCard> leaderCards) {
        this.tempLeaderCards = leaderCards;
        changesHandler.writePlayerTempLeaderCards(nickname, tempLeaderCards);
    }

    /**
     * This method takes in input the chosen cards and creates the personalBoard accordingly
     *
     * @param tempLeaderCards a list of two LeaderCards chosen by the Player
     */
    public void chooseTwoLeaderCards(List<LeaderCard> tempLeaderCards) {
        personalBoard.setLeaderCards(tempLeaderCards);
        this.tempLeaderCards.clear();
        changesHandler.writePlayerTempLeaderCards(nickname, tempLeaderCards);
    }

    /**
     * This method sets the Player's position in the game
     *
     * @param position an integer between 1 and 4
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @param action an action that has been chosen by the Player and performed,
     *               so therefore can be stored as performed
     */
    public void addAction(Actions action) {
        performedActions.add(action);
    }

    /**
     * Creates a map of temporarily stored MarketMarbles
     *
     * @param tempMarbles map of MarketMarbles taken from the Market.
     */
    public void setTempMarbles(Map<MarketMarble, Integer> tempMarbles) {
        this.tempMarbles = new HashMap<>(tempMarbles);
    }

    /**
     * This method returns a list of all the states in which the Player could go next
     *
     * @return a list of all the possible Actions that the Player can choose
     */
    public List<Actions> availableNextStates() {
        List<Actions> availableNextStates = new ArrayList<>();

        //Check if the Turn has just started
        if (performedActions.isEmpty() || hasOnlyPerformedLeaderCardActions()) {
            //The player can always choose to take resources from the market since the Turn just
            // started
            availableNextStates.add(Actions.TAKERESOURCEACTION);

            //Checks if the player has any inactive LeaderCard which can be activated
            if (canActivateLeaderCard())
                availableNextStates.add(Actions.ACTIVATED_LEADER_CARD_ACTION);

            //Checks if the player has any inactive LeaderCard which can be discarded
            if (canDiscardLeaderCard())
                availableNextStates.add(Actions.DISCARDED_LEADER_CARD_ACTION);

            //Checks if the player can afford any of the available DevelopmentCards
            if (canAffordDevelopmentCard())
                availableNextStates.add(Actions.BOUGHT_DEVELOPMENT_CARD_ACTION);

            //Checks if the player can afford any of the possible productions
            if (canAffordProduction())
                availableNextStates.add(Actions.ACTIVATED_PRODUCTION_ACTION);

            return availableNextStates;
        }

        //Checks if the Turn is in a state where the user has performed one of the compulsory action
        if (hasPerformedCompulsoryAction()) {
            //The player can always end the turn since he has already done one of the compulsory
            // action
            availableNextStates.add(Actions.ENDACTION);

            //Checks if the player has any inactive LeaderCard which can be activated
            if (canActivateLeaderCard())
                availableNextStates.add(Actions.ACTIVATED_LEADER_CARD_ACTION);

            //Checks if the player has any inactive LeaderCard which can be discarded
            if (canDiscardLeaderCard())
                availableNextStates.add(Actions.DISCARDED_LEADER_CARD_ACTION);

            return availableNextStates;
        }

        //In every other case the player can't choose any action, so the only option is to end
        // the turn
        availableNextStates.add(Actions.ENDACTION);
        return availableNextStates;
    }

    /**
     * Checks if the supplied action can be added to the executed
     * Actions list without violating action-order related game
     * logic.
     */
    public boolean isValidNextAction(Actions action) {
        if (performedActions.get(performedActions.size() - 1)
                .equals(Actions.STORED_TEMP_MARBLES_ACTION)) {
            return action.equals(Actions.STORED_MARKET_RESOURCES_ACTION);
        } else {
            if (action.isCompulsory())
                return !hasPerformedCompulsoryAction();
            else return true;
        }
    }

    /**
     * This method checks if the Player can currently afford to buy any of the DevelopmentCard
     * available in the DevelopmentCardsBoard
     *
     * @return true if the Player can afford any, false otherwise
     */
    private boolean canAffordDevelopmentCard() {
        return Arrays.stream(game.getDevelopmentCardsBoard().peekBoard()).anyMatch(
                developmentCards -> Arrays.stream(developmentCards).anyMatch(
                        developmentCard -> personalBoard.containsResources(developmentCard.peek().getCost())
                )
        );
    }

    /**
     * This method checks if the Player can currently afford to use any of his DevelopmentSlots
     * to produce
     *
     * @return true if the Player can afford any, false otherwise
     */
    private boolean canAffordProduction() {
        boolean canProduceFromDevelopmentCards =
                personalBoard.getDevelopmentCardSlots().stream().anyMatch(
                        slot -> personalBoard.containsResources(slot.peek().getInputResources())
                );

        boolean canProduceFromDefaultSlot = personalBoard.getResourceCount() >= 2;

        boolean canProduceFromLeaderCards = personalBoard.getLeaderCards().stream().anyMatch(
                leaderCard -> leaderCard.isActive() && leaderCard.getAbility().equals(LeaderCardAbility.PRODUCE)
        ) && personalBoard.getResourceCount() >= 1;

        return canProduceFromDevelopmentCards || canProduceFromDefaultSlot || canProduceFromLeaderCards;
    }

    /**
     * This method checks if the Player can currently afford to activate any of the LeaderCards
     * that he has
     *
     * @return true if the Player can afford any, false otherwise
     */
    private boolean canActivateLeaderCard() {
        List<LeaderCard> leaderCards = personalBoard.getLeaderCards();
        Optional<LeaderCard> card = leaderCards.stream().filter(
                leaderCard -> personalBoard.containsLeaderCardRequirements(leaderCard.getLeaderCardRequirements())
        ).findAny();

        return card.isPresent();
    }

    /**
     * This method checks if the Player can currently discard any of the LeaderCards that he has
     *
     * @return true if the Player can discard any, false otherwise
     */
    private boolean canDiscardLeaderCard() {
        return personalBoard.getLeaderCards().stream().anyMatch(
                leaderCard -> !leaderCard.isActive()
        );
    }

    /**
     * This method checks if the Player has only performed LeaderCard activation or discard
     * related action
     *
     * @return true if the Player has only performed such action, false otherwise
     */
    private boolean hasOnlyPerformedLeaderCardActions() {
        return performedActions.stream().filter(
                performedAction -> performedAction instanceof ActivateLeaderCardAction || performedAction instanceof DiscardLeaderCardAction
        ).collect(Collectors.toList()).containsAll(performedActions);
    }

    /**
     * This method checks if the Player has performed any of the compulsory Actions
     * (TakeResourceAction, BuyDevelopmentCardAction, ActivateProductionAction)
     *
     * @return true if the Player has performed any of such action, false otherwise
     */
    private boolean hasPerformedCompulsoryAction() {
        return performedActions.stream().anyMatch(Actions::isCompulsory);
    }

    /**
     * sets the isPlayersTurn flag to true
     */
    public void startTurn() {
        isPlayersTurn = true;
    }

    /**
     * sets the isPlayersTurn flag to false
     */
    public void finishTurn() {
        isPlayersTurn = false;
    }


    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param player the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Player player) {
        return this.getPosition() - player.getPosition();
    }
}