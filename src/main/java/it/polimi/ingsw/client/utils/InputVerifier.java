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
        return true;
    }

    public boolean canActivate(int index){
        return true;
    }

    public boolean canDiscard(int index){
        return true;
    }

    public boolean canPickResources(Map<Resource, Integer> pickedResources){
        return true;
    }

    public boolean canPickLeaderCards(List<DumbLeaderCard> chosenLeaderCards){
        return chosenLeaderCards.size()==2 &&
                dumbModel.getTempLeaderCards().containsAll(chosenLeaderCards) &&
                dumbModel.getGameState().equals(GameState.DEALT_LEADER_CARDS);
    }

    public boolean canPickTempMarbles(){
        return true;
    }

    private boolean canDoAction(List<ExecutedActions> executedActionsList, ExecutedActions nextAction){
        return true;
    }

    public boolean canSeePersonalBoard(int index){
        return index>0 && index<=dumbModel.getSize();
    }


}
