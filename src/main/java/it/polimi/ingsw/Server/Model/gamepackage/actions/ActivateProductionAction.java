package it.polimi.ingsw.server.model.gamepackage.actions;

import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.ProduceLeaderCard;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.personalboardpackage.DevelopmentSlot;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActivateProductionAction implements Action {
    PersonalBoard board;

    public ActivateProductionAction(PersonalBoard board) {
        this.board = board;
    }

    @Override
    public void performAction() {
    }

    public List<List<Boolean>> possibleProductionCombinations() {

    }

    /**
    * @param developmentSlots the development slots chosen for production
    * @param leaderCards the leader cards chosen for production
    */
    public void performAction(final List<DevelopmentSlot> developmentSlots, final List<ProduceLeaderCard> leaderCards) {
        developmentSlots.forEach(
                developmentSlot -> board.getStrongBox().storeResources(
                        developmentSlot.produce(
                                developmentSlot.getInputResources()
                        )
                )
        );

        leaderCards.forEach(
                leaderCard -> board.getStrongBox().storeResources(
                        leaderCard.produce(
                                board.discardResource(
                                        leaderCard.getInputResource()
                                )
                        )
                )
        );

    }

    /**
    * @param request the resources that should be saved in the Strongbox
    */
    private void saveResources(Map<Resource, Integer> request) {
        board.getStrongBox().storeResources(request);
    }

}