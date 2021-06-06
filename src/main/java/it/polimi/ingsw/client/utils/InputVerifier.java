package it.polimi.ingsw.client.utils;

import it.polimi.ingsw.client.utils.dumbobjects.DumbConvertLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.ProductionCombo;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        System.out.println(chosenProductionCombo);
        return dumbModel
                .getOwnPersonalBoard()
                .getDevelopmentCardSlots()
                .stream()
                .map(
                        developmentCardSlot -> developmentCardSlot.getDevelopmentCards().get(0))
                .collect(Collectors.toList())
                .contains(chosenProductionCombo.getDevelopmentCards())
                && dumbModel.getOwnPersonalBoard().getLeaderCards().contains(chosenProductionCombo.getLeaderCardProduction().keySet())
                && legalResourcesChoice(dumbModel.getOwnPersonalBoard().getDepots().getStoredResources(), chosenProductionCombo.getDiscardedResourcesFromDepots())
                && legalResourcesChoice(dumbModel.getOwnPersonalBoard().getStrongbox().getStoredResources(), chosenProductionCombo.getDiscardedResourcesFromStrongbox())
                && chosenProductionCombo.getDiscardedResourcesFromDepots().values().stream().allMatch( resourceCount -> resourceCount>0)
                && chosenProductionCombo.getDiscardedResourcesFromStrongbox().values().stream().allMatch( resourceCount -> resourceCount>0)
                && chosenProductionCombo.getDefaultSlotOutput().values().stream().allMatch( resourceCount -> resourceCount>0)
                && canDoAction(ExecutedActions.ACTIVATED_PRODUCTION_ACTION);
    }

    /**
     * Checks if user can take resource from market
     * @param place
     * @param index
     * @return true if request is valid, false otherwise
     */
    public boolean canTake(String place, int index){
        System.out.println(place);
        System.out.println(index);

        return ((place.equals("row") && index<=3) || (place.equals("column") && index<=4))
                && canDoAction(ExecutedActions.STORED_TEMP_MARBLES_ACTION)
                && index>0;
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
        System.out.println(chosenLevel);
        System.out.println(chosenColor);
        System.out.println(developmentCardSlotIndex);
        System.out.println(depotsResources);
        System.out.println(strongboxResources);

        return chosenColor != null && chosenLevel != null && depotsResources!=null && strongboxResources!=null
                && legalResourcesChoice(dumbModel.getOwnPersonalBoard().getDepots().getStoredResources(), depotsResources)
                && legalResourcesChoice(dumbModel.getOwnPersonalBoard().getStrongbox().getStoredResources(), strongboxResources)
                && dumbModel.getDevelopmentCardsBoard().peekCard(chosenLevel, chosenColor) != null
                && canDoAction(ExecutedActions.BOUGHT_DEVELOPMENT_CARD_ACTION);
    }

    /**
     * Checks if user can discard selected leader card
     * @param leaderCard leader card which user wants to activate
     * @return true if request is valid, false otherwise
     */
    public boolean canActivate(DumbLeaderCard leaderCard){
        System.out.println(leaderCard);


        return leaderCard != null
                && dumbModel.getOwnPersonalBoard().getLeaderCards().contains(leaderCard)
                && !dumbModel.getOwnPersonalBoard().getLeaderCards().get(dumbModel.getOwnPersonalBoard().getLeaderCards().indexOf(leaderCard)).isActive()
                && canDoAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
    }

    /**
     * Checks if user can discard selected leader card
     * @param leaderCard leader card which user wants to discard
     * @return true if request is valid, false otherwise
     */
    public boolean canDiscard(DumbLeaderCard leaderCard){
        System.out.println(leaderCard);

        return leaderCard != null
                && dumbModel.getOwnPersonalBoard().getLeaderCards().contains(leaderCard)
                && !dumbModel.getOwnPersonalBoard().getLeaderCards().get(dumbModel.getOwnPersonalBoard().getLeaderCards().indexOf(leaderCard)).isActive()
                && canDoAction(ExecutedActions.DISCARDED_LEADER_CARD_ACTION);
    }

    /**
     * Checks if selected resources in pregame are correct
     * @param pickedResources resource chosen by user in pregame
     * @return true if request is valid, false otherwise
     */
    public boolean canPickResources(Map<Resource, Integer> pickedResources){
        System.out.println(pickedResources);
        return pickedResources
                .values()
                .stream()
                .mapToInt(
                        value -> value
                )
                .sum() == dumbModel.getOwnPersonalBoard().getPosition()/2
                && dumbModel.getGameState().equals(GameState.ASSIGNED_INKWELL);
    }

    /**
     * Checks if the selected leader cards are correct
     * @param chosenLeaderCards leader cards chosen by user
     * @return true if request is valid, false otherwise
     */
    public boolean canPickLeaderCards(List<DumbLeaderCard> chosenLeaderCards){
        System.out.println(chosenLeaderCards);
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
        System.out.println(chosenMarbles);

        return canDoAction(ExecutedActions.STORED_MARKET_RESOURCES_ACTION)
                && !tempMarblesContainSelectedMarbles(chosenMarbles);
    }

    /**
     * Checks if nextAction is valid based on turn action order logic
     * @param nextAction the nextAction which user wants to execute
     * @return true if it can be executed in that order, false otherwise
     */
    private boolean canDoAction(ExecutedActions nextAction){
        if(!dumbModel.getOwnPersonalBoard().getTurnStatus())
            return false;

        if(!dumbModel.getGameState().equals(GameState.IN_GAME))
            return false;

        Optional<ExecutedActions> mostRecentAction = dumbModel.getTurnActions().stream().findFirst();
        // If no action has been executed, any action is allowed except for ending turn
        // and storing market resources
        return mostRecentAction.map(recentAction -> switch (nextAction) {
            case STORED_MARKET_RESOURCES_ACTION -> recentAction
                    .equals(ExecutedActions.STORED_TEMP_MARBLES_ACTION);
            case STORED_TEMP_MARBLES_ACTION, BOUGHT_DEVELOPMENT_CARD_ACTION,
                    ACTIVATED_PRODUCTION_ACTION -> dumbModel.getTurnActions().stream()
                    .noneMatch(ExecutedActions::isCompulsory);
            case TURN_ENDED_ACTION -> dumbModel.getTurnActions().stream()
                    .anyMatch(ExecutedActions::isCompulsory);
            case ACTIVATED_LEADER_CARD_ACTION, DISCARDED_LEADER_CARD_ACTION -> true;
        }).orElseGet(() -> !nextAction.equals(ExecutedActions.STORED_MARKET_RESOURCES_ACTION)
                && !nextAction.equals(ExecutedActions.TURN_ENDED_ACTION));
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
     * Checks if second map is contained in the first map
     * @param possessedResources  the actual possessed resources by user
     * @param selectedResources the resources selected by user
     * @return true if second map is contained in first map
     */
    private boolean legalResourcesChoice(Map<Resource, Integer> possessedResources, Map<Resource, Integer> selectedResources){
        Map<Resource, Integer> validResources = selectedResources
                .entrySet()
                .stream()
                .filter(
                        entry -> possessedResources.containsKey(entry.getKey()) && possessedResources.get(entry.getKey()) >= entry.getValue()
                )
                .collect(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                );

        return validResources.equals(selectedResources);
    }

    /**
     * Check if the selected marbles are a sub map of the currently stored TempMarbles,
     * also accounting for active ConvertLeaderCards
     */
    private boolean tempMarblesContainSelectedMarbles(Map<MarketMarble, Integer> chosenMarbles) {
        // Convert tempMarbles map to a list of marbles
        Map<MarketMarble, Integer> playerTempMarbles = dumbModel.getTempMarbles();
        playerTempMarbles.remove(MarketMarble.RED);
        List<MarketMarble> tempMarblesList = new ArrayList<>();
        playerTempMarbles.forEach((k, v) -> {
            for (int i = 0; i < v; i++) {
                tempMarblesList.add(k);
            }
        });

        // Convert selectedMarbles map to a list of marbles
        List<MarketMarble> selectedMarblesList = new ArrayList<>();
        chosenMarbles.forEach((k, v) -> {
            for (int i = 0; i < v; i++) {
                selectedMarblesList.add(k);
            }
        });

        // Create a list containing all the active ConvertLeaderCards
        List<DumbConvertLeaderCard> activeConvertCards = dumbModel.getOwnPersonalBoard()
                .getLeaderCards()
                .stream()
                .filter(card -> card.getAbility().equals(LeaderCardAbility.CONVERT))
                .map(card -> (DumbConvertLeaderCard) card)
                .collect(Collectors.toList());
        // Subtract the tempMarbles from the selectedMarbles one by one.
        // If the resulting list is empty the selected marbles do not exceed
        // the tempMarbles.
        tempMarblesList.forEach(marble -> {
            if (!marble.equals(MarketMarble.WHITE)) {
                selectedMarblesList.remove(marble);
            } else {
                // Handle WHITE marbles in case there are some active
                // ConvertLeaderCards
                if (activeConvertCards.get(0) != null) {
                    // If the first ConvertLeaderCard does not remove
                    // any marble from the list, check if there is a
                    // second one active and use that.
                    if (!selectedMarblesList
                            .remove(activeConvertCards.get(0)
                                    .getConvertedResource()
                                    .toMarble())
                            && activeConvertCards.get(1) != null) {
                        selectedMarblesList
                                .remove(activeConvertCards.get(1)
                                        .getConvertedResource()
                                        .toMarble());
                    }
                }
            }
        });
        return selectedMarblesList.size() == 0;
    }

}
