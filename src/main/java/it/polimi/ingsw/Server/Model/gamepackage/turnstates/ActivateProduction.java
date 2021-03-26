package it.polimi.ingsw.server.model.gamepackage.turnstates;

import it.polimi.ingsw.server.model.LeaderCardAbility;
import it.polimi.ingsw.server.model.ProduceLeaderCard;
import it.polimi.ingsw.server.model.gamepackage.Turn;
import it.polimi.ingsw.server.model.gamepackage.actions.Action;
import it.polimi.ingsw.server.model.gamepackage.actions.ActivateProductionAction;
import it.polimi.ingsw.server.model.gamepackage.actions.ProductionCombo;
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

    /**
     * @return an object representing the possible productions that can be chosen
     */
    public ProductionCombo possibleProductions() {
        List<DevelopmentSlot> possibleSlots = board.getDevelopmentSlots().stream().filter(
                developmentSlot -> board.hasResources(developmentSlot.getInputResources())
        );

        List<ProduceLeaderCard> possibleLeaderCards = new ArrayList<ProduceLeaderCard>(){{
            add((ProduceLeaderCard) board.getLeaderCard1());
            add((ProduceLeaderCard) board.getLeaderCard2());
        }}.stream().filter(
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
}