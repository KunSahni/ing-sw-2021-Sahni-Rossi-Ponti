package it.polimi.ingsw.server.model;

import java.io.FileNotFoundException;
import java.util.*;

import it.polimi.ingsw.network.clienttoserver.action.playeraction.*;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboard.FaithTrack;
import it.polimi.ingsw.server.model.personalboard.PersonalBoard;
import it.polimi.ingsw.server.model.utils.ExecutedActions;

/**
 * This class represents a Player
 */
public class Player implements Comparable<Player> {
    private transient String nickname;
    private int position;
    private transient PersonalBoard personalBoard;
    private List<LeaderCard> tempLeaderCards;
    private final List<ExecutedActions> performedActions;
    private Map<MarketMarble, Integer> tempMarbles;
    private boolean isTurn;
    private transient boolean isConnected;
    private transient ChangesHandler changesHandler;

    private Player() {
        this.performedActions = new ArrayList<>();
    }

    /**
     * Completes Player Object initialization by injecting all elements that
     * are not read from disk.
     *
     * @param changesHandler ChangesHandler Object of the game which the Player is part of.
     * @param nickname       player identifier.
     * @throws FileNotFoundException thrown when PersonalBoard cannot be read properly from disk.
     */
    public void init(ChangesHandler changesHandler, String nickname)
            throws FileNotFoundException {
        this.nickname = nickname;
        this.personalBoard = new PersonalBoard(changesHandler, nickname);
        this.isConnected = false;
        this.changesHandler = changesHandler;
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the positional index (1-4) of the player. If the Inkwell has
     * not been assigned yet, the returned value is 0.
     *
     * @return position index integer.
     */
    public int getPosition() {
        return position;
    }

    public PersonalBoard getPersonalBoard() {
        return personalBoard;
    }

    /**
     * If the Player has not picked its Leader Cards yet, returns a list of 4 cards.
     * Otherwise returns an empty list.
     */
    public List<LeaderCard> getTempLeaderCards() {
        return new ArrayList<>(tempLeaderCards);
    }

    /**
     * Returns a copy of the MarketMarbles temporarily stored inside of the Player during
     * a {@link TakeFromMarketAction}. This map can contain any type of MarketMarble except
     * RED ones, which are used to move the Faith Marker on the {@link FaithTrack}, therefore
     * not getting stored.
     *
     * @return a map of MarketMarbles.
     */
    public Map<MarketMarble, Integer> getTempMarbles() {
        return new HashMap<>(tempMarbles);
    }

    public List<ExecutedActions> getPerformedActions() {
        return performedActions;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void connect() {
        isConnected = true;
    }

    public void disconnect() {
        isConnected = false;
    }

    /**
     * Assigns a List of LeaderCards which the Player can pick from.
     *
     * @param leaderCards With the current game rules, a list of 4 LeaderCards should be passed.
     */
    public void setTempLeaderCards(List<LeaderCard> leaderCards) {
        this.tempLeaderCards = new ArrayList<>(leaderCards);
        changesHandler.writePlayer(this);
    }

    /**
     * Sets the List of Leader Cards passed as parameter as the final cards that
     * the Player will use for the entire game.
     *
     * @param chosenCards With the current game rules, a list of 2 LeaderCards, matching
     *                    the ones temporarily stored, should be passed.
     */
    public void chooseTwoLeaderCards(List<LeaderCard> chosenCards) {
        personalBoard.setLeaderCards(chosenCards);
        this.tempLeaderCards.clear();
    }

    /**
     * This method sets the Player's position in the game
     *
     * @param position an integer between 1 and 4
     */
    public void setPosition(int position) {
        this.position = position;
        changesHandler.writePlayer(this);
    }

    /**
     * @param action an action that has been chosen by the Player and performed,
     *               so therefore can be stored as performed
     */
    public void addAction(ExecutedActions action) {
        performedActions.add(action);
        changesHandler.writePlayer(this);
        changesHandler.flushBufferToDisk();
    }

    /**
     * Creates a map of temporarily stored MarketMarbles
     *
     * @param tempMarbles map of MarketMarbles taken from the Market.
     */
    public void setTempMarbles(Map<MarketMarble, Integer> tempMarbles) {
        this.tempMarbles = new HashMap<>(tempMarbles);
        changesHandler.writePlayer(this);
    }

    public void clearTempMarbles() {
        tempMarbles.clear();
        addAction(ExecutedActions.STORED_MARKET_RESOURCES_ACTION);
        changesHandler.writePlayer(this);
    }

//    /**
//     * This method returns a list of all the states in which the Player could go next
//     *
//     * @return a list of all the possible Actions that the Player can choose
//     */
//    public List<Actions> availableNextStates() {
//        List<Actions> availableNextStates = new ArrayList<>();
//
//        //Check if the Turn has just started
//        if (performedActions.isEmpty() || hasOnlyPerformedLeaderCardActions()) {
//            //The player can always choose to take resources from the market since the Turn just
//            // started
//            availableNextStates.add(Actions.TAKERESOURCEACTION);
//
//            //Checks if the player has any inactive LeaderCard which can be activated
//            if (canActivateLeaderCard())
//                availableNextStates.add(Actions.ACTIVATED_LEADER_CARD_ACTION);
//
//            //Checks if the player has any inactive LeaderCard which can be discarded
//            if (canDiscardLeaderCard())
//                availableNextStates.add(Actions.DISCARDED_LEADER_CARD_ACTION);
//
//            //Checks if the player can afford any of the available DevelopmentCards
//            if (canAffordDevelopmentCard())
//                availableNextStates.add(Actions.BOUGHT_DEVELOPMENT_CARD_ACTION);
//
//            //Checks if the player can afford any of the possible productions
//            if (canAffordProduction())
//                availableNextStates.add(Actions.ACTIVATED_PRODUCTION_ACTION);
//
//            return availableNextStates;
//        }
//
//        //Checks if the Turn is in a state where the user has performed one of the compulsory
//        action
//        if (hasPerformedCompulsoryAction()) {
//            //The player can always end the turn since he has already done one of the compulsory
//            // action
//            availableNextStates.add(Actions.ENDACTION);
//
//            //Checks if the player has any inactive LeaderCard which can be activated
//            if (canActivateLeaderCard())
//                availableNextStates.add(Actions.ACTIVATED_LEADER_CARD_ACTION);
//
//            //Checks if the player has any inactive LeaderCard which can be discarded
//            if (canDiscardLeaderCard())
//                availableNextStates.add(Actions.DISCARDED_LEADER_CARD_ACTION);
//
//            return availableNextStates;
//        }
//
//        //In every other case the player can't choose any action, so the only option is to end
//        // the turn
//        availableNextStates.add(Actions.ENDACTION);
//        return availableNextStates;
//    }

    /**
     * Checks if the supplied action can be added to the executed
     * Actions list without violating action-order related game
     * logic.
     */
    public boolean isValidNextAction(ExecutedActions action) {
        ExecutedActions mostRecentAction = null;
        if (performedActions.size() > 0)
            mostRecentAction = performedActions.get(performedActions.size() - 1);
        if (mostRecentAction != null) {
            if (mostRecentAction.equals(ExecutedActions.TURN_ENDED_ACTION)) {
                return false;
            } else {
                return switch (action) {
                    case STORED_MARKET_RESOURCES_ACTION -> mostRecentAction
                            .equals(ExecutedActions.STORED_TEMP_MARBLES_ACTION);
                    case STORED_TEMP_MARBLES_ACTION, BOUGHT_DEVELOPMENT_CARD_ACTION,
                            ACTIVATED_PRODUCTION_ACTION -> !hasPerformedCompulsoryAction();
                    case TURN_ENDED_ACTION -> hasPerformedCompulsoryAction();
                    case ACTIVATED_LEADER_CARD_ACTION, DISCARDED_LEADER_CARD_ACTION ->
                            !mostRecentAction.equals(ExecutedActions.STORED_TEMP_MARBLES_ACTION);
                };
            }
        } else {
            // If no action has been executed, any action is allowed except for ending turn
            // and storing market resources
            return !action.equals(ExecutedActions.STORED_MARKET_RESOURCES_ACTION)
                    && !action.equals(ExecutedActions.TURN_ENDED_ACTION);
        }
    }

//    /**
//     * This method checks if the Player can currently afford to buy any of the DevelopmentCard
//     * available in the DevelopmentCardsBoard
//     *
//     * @return true if the Player can afford any, false otherwise
//     */
//    private boolean canAffordDevelopmentCard() {
//        return Arrays.stream(game.getDevelopmentCardsBoard().peekBoard()).anyMatch(
//                developmentCards -> Arrays.stream(developmentCards).anyMatch(
//                        developmentCard -> personalBoard.containsResources(developmentCard.peek
//                        ().getCost())
//                )
//        );
//    }
//
//    /**
//     * This method checks if the Player can currently afford to use any of his DevelopmentSlots
//     * to produce
//     *
//     * @return true if the Player can afford any, false otherwise
//     */
//    private boolean canAffordProduction() {
//        boolean canProduceFromDevelopmentCards =
//                personalBoard.getDevelopmentCardSlots().stream().anyMatch(
//                        slot -> personalBoard.containsResources(slot.peek().getInputResources())
//                );
//
//        boolean canProduceFromDefaultSlot = personalBoard.getResourceCount() >= 2;
//
//        boolean canProduceFromLeaderCards = personalBoard.getLeaderCards().stream().anyMatch(
//                leaderCard -> leaderCard.isActive() && leaderCard.getAbility().equals
//                (LeaderCardAbility.PRODUCE)
//        ) && personalBoard.getResourceCount() >= 1;
//
//        return canProduceFromDevelopmentCards || canProduceFromDefaultSlot ||
//        canProduceFromLeaderCards;
//    }
//
//    /**
//     * This method checks if the Player can currently afford to activate any of the LeaderCards
//     * that he has
//     *
//     * @return true if the Player can afford any, false otherwise
//     */
//    private boolean canActivateLeaderCard() {
//        List<LeaderCard> leaderCards = personalBoard.getLeaderCards();
//        Optional<LeaderCard> card = leaderCards.stream().filter(
//                leaderCard -> personalBoard.containsLeaderCardRequirements(leaderCard
//                .getLeaderCardRequirements())
//        ).findAny();
//
//        return card.isPresent();
//    }
//
//    /**
//     * This method checks if the Player can currently discard any of the LeaderCards that he has
//     *
//     * @return true if the Player can discard any, false otherwise
//     */
//    private boolean canDiscardLeaderCard() {
//        return personalBoard.getLeaderCards().stream().anyMatch(
//                leaderCard -> !leaderCard.isActive()
//        );
//    }
//
//    /**
//     * This method checks if the Player has only performed LeaderCard activation or discard
//     * related action
//     *
//     * @return true if the Player has only performed such action, false otherwise
//     */
//    private boolean hasOnlyPerformedLeaderCardActions() {
//        return performedActions.stream().filter(
//                performedAction -> performedAction instanceof ActivateLeaderCardAction ||
//                performedAction instanceof DiscardLeaderCardAction
//        ).collect(Collectors.toList()).containsAll(performedActions);
//    }

    /**
     * This method checks if the Player has successfully completed any of the
     * compulsory Actions:
     * {@link TakeFromMarketAction},
     * {@link BuyDevelopmentCardAction},
     * {@link ActivateProductionAction}
     *
     * @return true if the Player has performed one of these, false otherwise
     */
    private boolean hasPerformedCompulsoryAction() {
        return performedActions.stream().anyMatch(ExecutedActions::isCompulsory);
    }

    public void startTurn() {
        isTurn = true;
        changesHandler.writePlayer(this);
        changesHandler.flushBufferToDisk();
    }

    /**
     * Executes end-of-turn routines on the Player Object.
     */
    public void finishTurn() {
        isTurn = false;
        performedActions.clear();
        changesHandler.writePlayer(this);
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