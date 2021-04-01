package it.polimi.ingsw.server.controller.gamepackage.turnstates;

import it.polimi.ingsw.server.model.LeaderCardAbility;
import it.polimi.ingsw.server.model.ProduceLeaderCard;
import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.controller.gamepackage.actions.Action;
import it.polimi.ingsw.server.controller.gamepackage.actions.ActivateProductionAction;
import it.polimi.ingsw.server.controller.gamepackage.actions.ProductionCombo;
import it.polimi.ingsw.server.model.personalboardpackage.DefaultSlot;
import it.polimi.ingsw.server.model.personalboardpackage.DevelopmentCardSlot;
import it.polimi.ingsw.server.model.personalboardpackage.DevelopmentSlot;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

import java.util.*;

public class ActivateProduction implements AbstractTurnState {
    private PersonalBoard board;
    private ActivateProductionAction action;

    @Override
    public void setup(Turn turn, Action action) {
        this.action = (ActivateProductionAction) action;
        this.board = turn.getPlayer().getPersonalBoard();
    }

    @Override
    public void performAction() {
        activateProduction(action.getProductionCombo().getDevelopmentSlots(), action.getProductionCombo().getLeaderCards());
    }

    //todo: make it static
    /**
     * @return an object representing the possible productions that can be chosen
     */
    public ProductionCombo possibleProductions() {
        //This list contains all the DevelopmentSlots from which the Player can produce
        List<DevelopmentSlot> possibleSlots = board.getDevelopmentSlots().stream().filter(
                developmentSlot -> developmentSlot instanceof DevelopmentCardSlot
        ).filter(
                developmentSlot -> board.hasResources(developmentSlot.getInputResources())
        );

        //Check if the Player can afford to produce from DefaultSlot
        possibleSlots.add(board.getDevelopmentSlots().stream().filter(
                developmentSlot -> developmentSlot instanceof DefaultSlot && board.getResourcesCount() > 2
        ));

        //This list contains all the LeaderCards from which the Player can produce
        List<ProduceLeaderCard> possibleLeaderCards = board.getLeaderCards().stream().filter(
                leaderCard -> leaderCard.getAbility().equals(LeaderCardAbility.PRODUCE)
        ).filter(
                leaderCard -> board.hasResource(leaderCard.getInputResource())
        );

        return new ProductionCombo(possibleSlots, possibleLeaderCards);
    }

    /**
     * @param developmentSlots the development slots chosen for production
     * @param leaderCards the leader cards chosen for production
     */
    private void activateProduction(final List<DevelopmentSlot> developmentSlots, final List<ProduceLeaderCard> leaderCards) {
        developmentSlots.forEach(
                developmentSlot -> board.getStrongbox().storeResources(
                        developmentSlot.produce(
                                board.discardResources(developmentSlot.getInputResources())
                        )
                )
        );
        //todo: ask how would default slot resources be chosen --> controller passes them to ActivateProductionAction
        //todo: don't pass anything to produce, the discard will be managed by controller

        leaderCards.forEach(
                leaderCard -> board.getStrongbox().storeResources(
                        leaderCard.produce(
                                board.discardResource(
                                        leaderCard.getInputResource()
                                )
                        )
                )
        );

    }
}