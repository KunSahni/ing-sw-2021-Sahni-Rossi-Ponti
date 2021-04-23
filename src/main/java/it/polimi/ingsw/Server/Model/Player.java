package it.polimi.ingsw.server.model;

import java.util.*;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Collectors;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.message.action.*;
import it.polimi.ingsw.server.controller.message.choice.LeaderCardsChoiceMessage;
import it.polimi.ingsw.server.controller.message.choice.NextActionMessage;
import it.polimi.ingsw.server.controller.message.choice.ResourceMarketConvertMessage;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

/**
 * This class represents a Player
 */

public class Player implements Comparator<Player>{
    private final Game game;
    private final String nickname;
    private int position;
    private final PersonalBoard personalBoard;
    private List<LeaderCard> leaderCards;
    private List<Forwardable> performedActions;
    private Map<MarketMarble, Integer> tempMarbles;
    private boolean isPlayersTurn;
    private final SubmissionPublisher<Object> publisher = new SubmissionPublisher<>();
    private int rank;
    private int victoryPoints;

    /**
     * @param nickname an unique nickname associated to the Player, can't be changed during the game
     * @param game the Game in which the Player will be playing
     */
    public Player(String nickname, Game game) {  //todo: add View
        this.nickname = nickname;
        this.game = game;
        this.personalBoard = new PersonalBoard(this);
        this.performedActions = new ArrayList<>();
        this.tempMarbles = new HashMap<>();
        this.isPlayersTurn = false;
        //publisher.subscribe(view);
        rank = victoryPoints = 0;
    }

    /**
     * This method assigns 4 LeaderCards to the Player from which he picks two
     * @param leaderCards a list of 4 LeaderCards
     */
    public void addLeaderCards(List<LeaderCard> leaderCards){
        this.leaderCards = leaderCards;
        publisher.submit(
                new LeaderCardsChoiceMessage(leaderCards)
        );
    }

    /**
     * This method takes in input the chosen cards and creates the personalBoard accordingly
     * @param leaderCards a list of two LeaderCards chosen by the Player
     */
    public void setLeaderCards(List<LeaderCard> leaderCards){
        personalBoard.setLeaderCards(leaderCards);
        leaderCards.clear();
    }

    /**
     * This method sets the Player's position in the game
     * @param position an integer between 1 and 4 representing a position in the fame
     */
    public void setPosition(int position) {
        this.position = position;
        if(position>2)
            getPersonalBoard().getFaithTrack().moveMarker(1);
    }

    /**
     * @param action an action that has been chosen by the Player and performed, so therefore can be stored as performed
     */
    public void addAction(Forwardable action){
        performedActions.add(action);
        publisher.submit(
                new NextActionMessage(availableNextStates())
        );
    }

    /**
     * This method sets the Player's position in the game
     * @param tempMarbles a copy of the MarketMarbles picked from the Market
     */
    public void setTempMarbles(Map<MarketMarble, Integer> tempMarbles) {
        this.tempMarbles = tempMarbles;
        publisher.submit(new ResourceMarketConvertMessage(tempMarbles));
    }

    /** This method returns a list of all the states in which the Player could go next
            * @return a list of all the possible Actions that the Player can choose
    */
    public List<Actions> availableNextStates() {
        List<Actions> availableNextStates = new ArrayList<>();

        //Check if the Turn has just started
        if(performedActions.isEmpty() || hasOnlyPerformedLeaderCardActions()){
            //The player can always choose to take resources from the market since the Turn just started
            availableNextStates.add(Actions.TAKERESOURCEACTION);

            //Checks if the player has any inactive LeaderCard which can be activated
            if(canActivateLeaderCard())
                availableNextStates.add(Actions.ACTIVATELEADERCARDACTION);

            //Checks if the player has any inactive LeaderCard which can be discarded
            if(canDiscardLeaderCard())
                availableNextStates.add(Actions.DISCARDLEADERCARDACTION);

            //Checks if the player can afford any of the available DevelopmentCards
            if(canAffordDevelopmentCard())
                availableNextStates.add(Actions.BUYDEVELOPMENTCARDACTION);

            //Checks if the player can afford any of the possible productions
            if(canAffordProduction())
                availableNextStates.add(Actions.ACTIVATEPRODUCTIONACTION);

            return  availableNextStates;
        }

        //Checks if the Turn is in a state where the user has performed one of the compulsory action
        if (hasPerformedCompulsoryAction()){
            //The player can always end the turn since he has already done one of the compulsory action
            availableNextStates.add(Actions.ENDACTION);

            //Checks if the player has any inactive LeaderCard which can be activated
            if(canActivateLeaderCard())
                availableNextStates.add(Actions.ACTIVATELEADERCARDACTION);

            //Checks if the player has any inactive LeaderCard which can be discarded
            if(canDiscardLeaderCard())
                availableNextStates.add(Actions.DISCARDLEADERCARDACTION);

            return availableNextStates;
        }

        //In every other case the player can't choose any action, so the only option is to end the turn
        availableNextStates.add(Actions.ENDACTION);
        return availableNextStates;
    }

    /**
     * This method checks if the Player can currently afford to buy any of the DevelopmentCard available in the DevelopmentCardsBoard
     * @return true if the Player can afford any, false otherwise
     */
    private boolean canAffordDevelopmentCard(){
        return Arrays.stream(game.getDevelopmentCardsBoard().peekBoard()).anyMatch(
                developmentCards -> Arrays.stream(developmentCards).anyMatch(
                        developmentCard -> personalBoard.containsResources(developmentCard.peek().getCost())
                )
        );
    }

    /**
     * This method checks if the Player can currently afford to use any of his DevelopmentSlots to produce
     * @return true if the Player can afford any, false otherwise
     */
    private boolean canAffordProduction(){
        boolean canProduceFromDevelopmentCards = personalBoard.getDevelopmentCardSlots().stream().anyMatch(
                slot -> personalBoard.containsResources(slot.peek().getInputResources())
        );

        boolean canProduceFromDefaultSlot = personalBoard.getResourceCount() >= 2;

        boolean canProduceFromLeaderCards = personalBoard.getLeaderCards().stream().anyMatch(
                leaderCard -> leaderCard.isActive() && leaderCard.getAbility().equals(LeaderCardAbility.PRODUCE)
        ) && personalBoard.getResourceCount() >= 1;

        return canProduceFromDevelopmentCards || canProduceFromDefaultSlot || canProduceFromLeaderCards;
    }

    /**
     * This method checks if the Player can currently afford to activate any of the LeaderCards that he has
     * @return true if the Player can afford any, false otherwise
     */
    private boolean canActivateLeaderCard(){
        List<LeaderCard> leaderCards = personalBoard.getLeaderCards();
        Optional<LeaderCard> card = leaderCards.stream().filter(
                leaderCard -> personalBoard.containsLeaderCardRequirements(leaderCard.getLeaderCardRequirements())
        ).findAny();

        return card.isPresent();
    }

    /**
     * This method checks if the Player can currently discard any of the LeaderCards that he has
     * @return true if the Player can discard any, false otherwise
     */
    private boolean canDiscardLeaderCard(){
        return personalBoard.getLeaderCards().stream().anyMatch(
                leaderCard -> !leaderCard.isActive()
        );
    }

    /**
     * This method checks if the Player has only performed LeaderCard activation or discard related action
     * @return true if the Player has only performed such action, false otherwise
     */
    private boolean hasOnlyPerformedLeaderCardActions(){
        return performedActions.stream().filter(
                performedAction -> performedAction instanceof ActivateLeaderCardAction || performedAction instanceof DiscardLeaderCardAction
            ).collect(Collectors.toList()).containsAll(performedActions);
    }

    /**
     * This method checks if the Player has performed any of the compulsory Actions (TakeResourceAction, BuyDevelopmentCardAction, ActivateProductionAction)
     * @return true if the Player has performed any of such action, false otherwise
     */
    private boolean hasPerformedCompulsoryAction() {
        return performedActions.stream().anyMatch(
                performedAction -> performedAction instanceof TakeResourceAction || performedAction instanceof BuyDevelopmentCardAction || performedAction instanceof ActivateProductionAction
        );
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
     * Compares two players based on their position in the game
     * @param firstPlayer the first Player to be compared
     * @param secondPlayer the second Player to be compared
     * @return the value 0 if firstPlayer is in the same position as secondPlayer; a value less than 0 if firstPlayer is before secondPlayer; and a value greater than 0 firstPlayer is after secondPlayer
     */
    @Override
    public int compare(Player firstPlayer, Player secondPlayer) {
        return Integer.compare(firstPlayer.getPosition(), secondPlayer.getPosition());
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
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

    public List<LeaderCard> getLeaderCards() {
        return List.copyOf(leaderCards);
    }

    public Game getGame() {
        return game;
    }

    public int getRank() {
        return rank;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public Map<MarketMarble, Integer> getTempMarbles() {
        return Map.copyOf(tempMarbles);
    }
}