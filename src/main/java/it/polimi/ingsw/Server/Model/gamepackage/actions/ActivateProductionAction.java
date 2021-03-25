package it.polimi.ingsw.server.model.gamepackage.actions;

import it.polimi.ingsw.server.model.ProduceLeaderCard;
import it.polimi.ingsw.server.model.personalboardpackage.DevelopmentSlot;

import java.util.List;

public class ActivateProductionAction implements Action {
    private final ProductionCombo productionCombo;

    public ActivateProductionAction(List<DevelopmentSlot> developmentSlots, List<ProduceLeaderCard> leaderCards){
        this.productionCombo = new ProductionCombo(developmentSlots, leaderCards);
    }

    public ProductionCombo getProductionCombo() {
        return productionCombo;
    }
}