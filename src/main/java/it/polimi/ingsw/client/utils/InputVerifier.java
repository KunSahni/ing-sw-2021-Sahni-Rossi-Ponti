package it.polimi.ingsw.client.utils;

import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.server.controller.action.playeraction.PlayerAction;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.ProductionCombo;
import it.polimi.ingsw.server.model.utils.Resource;

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

    public boolean canProduce(ProductionCombo chosenProductionCombo){
        return true;
    }

    public boolean canTake(String place, int index){
        return true;
    }

    public boolean canBuy(Level chosenLevel, Color chosenColor, int developmentCardSlotIndex, Map<Resource, Integer> depotsResources, Map<Resource, Integer> strongboxResources){
        return legalResourcesChoice(dumbModel.getOwnPersonalBoard().getDepots().getStoredResources(), depotsResources)
                && legalResourcesChoice(dumbModel.getOwnPersonalBoard().getStrongbox().getStoredResources(), strongboxResources)
                && dumbModel.getDevelopmentCardsBoard().peekCard(chosenLevel, chosenColor) != null;
                //&& dumbModel.getDevelopmentCardsBoard().peekCard(chosenLevel, chosenColor).getCost()
    }

    public boolean canActivate(int index){
        return dumbModel.getOwnPersonalBoard().getLeaderCards().get(index) != null
                && !dumbModel.getOwnPersonalBoard().getLeaderCards().get(index).isActive()
                && canDoAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
    }

    public boolean canDiscard(int index){
        return dumbModel.getOwnPersonalBoard().getLeaderCards().get(index) != null
                && !dumbModel.getOwnPersonalBoard().getLeaderCards().get(index).isActive()
                && canDoAction(ExecutedActions.DISCARDED_LEADER_CARD_ACTION);
    }

    public boolean canPickResources(Map<Resource, Integer> pickedResources){
        return pickedResources
                .values()
                .stream()
                .mapToInt(
                        value -> value
                )
                .sum() == dumbModel.getOwnPersonalBoard().getPosition()/2;
    }

    public boolean canPickLeaderCards(List<DumbLeaderCard> chosenLeaderCards){
        return chosenLeaderCards.size()==2 &&
                dumbModel.getTempLeaderCards().containsAll(chosenLeaderCards) &&
                dumbModel.getGameState().equals(GameState.DEALT_LEADER_CARDS);
    }

    public boolean canPickTempMarbles(){
        return true;
    }

    private boolean canDoAction(ExecutedActions nextAction){
        List<ExecutedActions> executedActionsList;
        return true;
    }

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
