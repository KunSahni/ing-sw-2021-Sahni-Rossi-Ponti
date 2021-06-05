package it.polimi.ingsw.client.utils;

import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.server.controller.action.playeraction.PlayerAction;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.ProductionCombo;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class has the goal of checking that the selected action by the user is valid
 */
public class InputVerifier {
    private final DumbModel dumbModel;

    public InputVerifier(DumbModel dumbModel) {
        this.dumbModel = dumbModel;
    }

    /**
     * Checks if user can activate a production
     * @param chosenProductionCombo a production combo which includes all choices made by user
     * @return true if request is valid, false otherwise
     */
    public boolean canProduce(ProductionCombo chosenProductionCombo){
        return true;
    }

    /**
     * Checks if user can take resource from market
     * @param place
     * @param index
     * @return true if request is valid, false otherwise
     */
    public boolean canTake(String place, int index){
        return canDoAction(ExecutedActions.STORED_TEMP_MARBLES_ACTION);
    }

    /**
     * Checks if user can buy the selected development card
     * @param chosenLevel level of chosen development card
     * @param chosenColor color of chosen development card
     * @param developmentCardSlotIndex index of development card slot where user wants to place new card
     * @param depotsResources map of resources which will be discarded from depots or store leader cards
     * @param strongboxResources map of resources which will be discarded from strongbox
     * @return true if request is valid, false otherwise
     */
    public boolean canBuy(Level chosenLevel, Color chosenColor, int developmentCardSlotIndex, Map<Resource, Integer> depotsResources, Map<Resource, Integer> strongboxResources){
        return legalResourcesChoice(dumbModel.getOwnPersonalBoard().getDepots().getStoredResources(), depotsResources)
                && legalResourcesChoice(dumbModel.getOwnPersonalBoard().getStrongbox().getStoredResources(), strongboxResources)
                && dumbModel.getDevelopmentCardsBoard().peekCard(chosenLevel, chosenColor) != null;
                //&& dumbModel.getDevelopmentCardsBoard().peekCard(chosenLevel, chosenColor).getCost()
    }

    /**
     * Checks if user can discard selected leader card
     * @param index the index of the leader card which he wants to discard
     * @return true if request is valid, false otherwise
     */
    public boolean canActivate(int index){
        return dumbModel.getOwnPersonalBoard().getLeaderCards().get(index) != null
                && !dumbModel.getOwnPersonalBoard().getLeaderCards().get(index).isActive()
                && canDoAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
    }

    /**
     * Checks if user can discard selected leader card
     * @param index the index of the leader card which he wants to discard
     * @return true if request is valid, false otherwise
     */
    public boolean canDiscard(int index){
        return dumbModel.getOwnPersonalBoard().getLeaderCards().get(index) != null
                && !dumbModel.getOwnPersonalBoard().getLeaderCards().get(index).isActive()
                && canDoAction(ExecutedActions.DISCARDED_LEADER_CARD_ACTION);
    }

    /**
     * Checks if selected resources in pregame are correct
     * @param pickedResources resource chosen by user in pregame
     * @return true if request is valid, false otherwise
     */
    public boolean canPickResources(Map<Resource, Integer> pickedResources){
        return pickedResources
                .values()
                .stream()
                .mapToInt(
                        value -> value
                )
                .sum() == dumbModel.getOwnPersonalBoard().getPosition()/2;
    }

    /**
     * Checks if the selected leader cards are correct
     * @param chosenLeaderCards leader cards chosen by user
     * @return true if request is valid, false otherwise
     */
    public boolean canPickLeaderCards(List<DumbLeaderCard> chosenLeaderCards){
        return chosenLeaderCards.size()==2 &&
                dumbModel.getTempLeaderCards().containsAll(chosenLeaderCards) &&
                dumbModel.getGameState().equals(GameState.DEALT_LEADER_CARDS);
    }

    /**
     * Checks if the chosen market marbles are a valid selection
     * @param chosenMarbles map of chosen market marbles
     * @return true if request is valid, false otherwise
     */
    public boolean canPickTempMarbles(Map<MarketMarble, Integer> chosenMarbles){
        return true;
    }

    /**
     * Checks if nextAction is valid based on turn action order logic
     * @param nextAction the nextAction which user wants to execute
     * @return true if it can be executed in that order, false otherwise
     */
    private boolean canDoAction(ExecutedActions nextAction){
        if(!dumbModel.getOwnPersonalBoard().getTurnStatus())
            return false;
        ExecutedActions mostRecentAction = dumbModel.getTurnActions().get(dumbModel.getTurnActions().size() - 1);
        if (mostRecentAction.equals(ExecutedActions.TURN_ENDED_ACTION)) {
            return false;
        } else if (mostRecentAction.equals(ExecutedActions.STORED_TEMP_MARBLES_ACTION)) {
            return nextAction.equals(ExecutedActions.STORED_MARKET_RESOURCES_ACTION);
        } else {
            if (nextAction.isCompulsory())
                return dumbModel.getTurnActions().stream().noneMatch(ExecutedActions::isCompulsory);
            else if (nextAction.equals(ExecutedActions.TURN_ENDED_ACTION))
                return dumbModel.getTurnActions().stream().anyMatch(ExecutedActions::isCompulsory);
            else return true;
        }
    }

    /**
     * Checks if selected index by user is valid
     * @param index selected index by user
     * @return true if the index is valid, false otherwise
     */
    public boolean canSeePersonalBoard(int index){
        return index>0 && index<=dumbModel.getSize();
    }


    /**
     * Checks if second map is submap of first
     * @param possessedResources  the actual possessed resources by user
     * @param selectedResources the resources selected by user
     * @return true if second map is contained in first map
     */
    private boolean legalResourcesChoice(Map<Resource, Integer> possessedResources, Map<Resource, Integer> selectedResources){
        Map<Resource, Integer> validResources = selectedResources
                .entrySet()
                .stream()
                .filter(
                        entry -> possessedResources.get(entry.getKey()) >= entry.getValue()
                )
                .collect(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                );

        return validResources.equals(selectedResources);
    }

}
