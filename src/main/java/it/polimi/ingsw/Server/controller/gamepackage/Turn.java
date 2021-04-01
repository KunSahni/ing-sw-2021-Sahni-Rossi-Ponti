package it.polimi.ingsw.server.controller.gamepackage;

import java.util.*;
import java.util.stream.Collectors;

import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.controller.gamepackage.actions.*;
import it.polimi.ingsw.server.controller.gamepackage.turnstates.*;
import it.polimi.ingsw.server.controller.gamestates.LastRound;
import it.polimi.ingsw.server.model.personalboardpackage.DevelopmentCardSlot;

//todo: add to uml, add performAction which controls lastround and calls addaction

/**
 * This class represents a single Turn in a multiplayer game
 */
public class Turn {
    protected AbstractTurnState currentState;
    protected final Game game;
    protected final Player player;
    protected List<Action> performedActions;

    public Turn(Game game, Player player) {
        this.currentState = new Start();
        this.game = game;
        this.player = player;
        this.performedActions = new ArrayList<>();
    }

    //todo: call performaction from here
    /**
    * @param nextState the state corresponding to the Action
    */
    public void setNextState(AbstractTurnState nextState, Action action) {
        currentState = nextState;
        currentState.setup(this, action);
        if(currentState instanceof End) //todo: trigger lastRound
            game.nextTurn();
    }

    /**
     * This method tells the Game that a Player has either completed the FaithTrack or has bought 7 DevelopmentCards and therefore the last round has just begun
     */
    public void triggerLastRound() {
        game.setNextState(new LastRound());
    }

    /**
     * This method returns a list of all the states in which the Player could go next
     * @return a list of all the possible Actions that the Player can choose
     */
    public List<AbstractTurnState> availableNextStates() {
        List<AbstractTurnState> availableNextStates = new ArrayList<>();

        //Check if the Turn has just started
        if(currentState instanceof Start){
            //The player can always choose to take resources from the market since the Turn just started
            availableNextStates.add(new TakeResource());

            //Checks if the player has any inactive LeaderCard which can be activated
            if(canActivateLeaderCard())
                availableNextStates.add(new ActivateLeaderCard());

            //Checks if the player has any inactive LeaderCard which can be discarded
            if(canDiscardLeaderCard())
                availableNextStates.add(new DiscardLeaderCard());

            //Checks if the player can afford any of the available DevelopmentCards
            if(canAffordDevelopmentCard())
                availableNextStates.add(new BuyDevelopmentCard());

            //Checks if the player can afford any of the possible productions
            if(canAffordProduction())
                availableNextStates.add(new ActivateProduction());

            return  availableNextStates;
        }

        //Checks if the Turn is in a state where the user has performed one of the compulsory actions
        if (hasPerformedCompulsoryAction()){
            //The player can always end the turn since he has already done one of the compulsory actions
            availableNextStates.add(new End());

            //Checks if the player has any inactive LeaderCard which can be activated
            if(canActivateLeaderCard())
                availableNextStates.add(new ActivateLeaderCard());

            //Checks if the player has any inactive LeaderCard which can be discarded
            if(canDiscardLeaderCard())
                availableNextStates.add(new DiscardLeaderCard());

            return availableNextStates;
        }

        //Checks if the Turn is in a state where the user has only done actions related to LeaderCard activation or discard
        if(hasOnlyPerformedLeaderCardActions()){
            //The player can always choose to take resources from the market since the Turn just started
            availableNextStates.add(new TakeResource());

            //Checks if the player has any inactive LeaderCard which can be activated
            if(canActivateLeaderCard())
                availableNextStates.add(new ActivateLeaderCard());

            //Checks if the player has any inactive LeaderCard which can be discarded
            if(canDiscardLeaderCard())
                availableNextStates.add(new DiscardLeaderCard());

            //Checks if the player can afford any of the available DevelopmentCards
            if(canAffordDevelopmentCard())
                availableNextStates.add(new BuyDevelopmentCard());

            //Checks if the player can afford any of the possible productions
            if(canAffordProduction())
                availableNextStates.add(new ActivateProduction());

            return  availableNextStates;
        }

        //In every other case the player can't choose any action, so the only option is to end the turn
        availableNextStates.add(new End());
        return availableNextStates;
    }


    /**
     * @param action an action that has been chosen by the Player and therefore can be stored
     */
    public void addAction(Action action){
        performedActions.add(action);
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    /**
     * This method checks if the Player can currently afford to buy any of the DevelopmentCard available in the DevelopmentCardsBoard
     * @return true if the Player can afford any, false otherwise
     */
    private boolean canAffordDevelopmentCard(){
        return Arrays.stream(game.getDevelopmentCardsBoard().peekBoard()).anyMatch(
                developmentCards -> Arrays.stream(developmentCards).anyMatch(
                        developmentCard -> player.getPersonalBoard().hasResources(developmentCard.getCost())
                )
        );
    }

    /**
     * This method checks if the Player can currently afford to use any of his DevelopmentSlots to produce
     * @return true if the Player can afford any, false otherwise
     */
    private boolean canAffordProduction(){
        return player.getPersonalBoard().getDevelopmentSlots().stream().anyMatch(
                slot -> slot instanceof DevelopmentCardSlot && player.getPersonalBoard().hasResources(slot.getInputResources())
        );
    }

    /**
     * This method checks if the Player can currently afford to any of the LeaderCards that he has
     * @return true if the Player can afford any, false otherwise
     */
    private boolean canActivateLeaderCard(){
        List<LeaderCard> leaderCards = player.getPersonalBoard().getLeaderCards();
        Optional<LeaderCard> card = leaderCards.stream().filter(
                leaderCard -> switch (leaderCard.getAbility()){
                    case STORE ->
                        player.getPersonalBoard().hasResources(leaderCard.getRequirement());
                    default ->
                        player.getPersonalBoard().hasDevelopmentCards(leaderCard.getRequirements());
                }
        ).findAny();

        return card.isPresent();
    }

    /**
     * This method checks if the Player can currently discard any of the LeaderCards that he has
     * @return true if the Player can discard any, false otherwise
     */
    private boolean canDiscardLeaderCard(){
        return !player.getPersonalBoard().getLeaderCard1().isActive() || !player.getPersonalBoard().getLeaderCard2().isActive();
    }

    /**
     * This method checks if the Player has only performed LeaderCard activation or discard related actions
     * @return true if the Player has only performed such actions, false otherwise
     */
    private boolean hasOnlyPerformedLeaderCardActions(){
        return performedActions.stream().filter(
                performedAction -> performedAction instanceof ActivateLeaderCardAction || performedAction instanceof DiscardLeaderCardAction
            ).collect(Collectors.toList()).containsAll(performedActions);
    }

    /**
     * This method checks if the Player has performed any of the compulsory Actions (TakeResource, BuyDevelopmentCard, ActivateProduction)
     * @return true if the Player has performed any of such actions, false otherwise
     */
    private boolean hasPerformedCompulsoryAction() {
        return performedActions.stream().anyMatch(
                performedAction -> performedAction instanceof TakeResourceAction || performedAction instanceof BuyDevelopmentCardAction || performedAction instanceof ActivateProductionAction
        );
    }
}