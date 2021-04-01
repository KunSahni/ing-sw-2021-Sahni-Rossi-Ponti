package it.polimi.ingsw.server.controller.gamepackage.actions;

import it.polimi.ingsw.server.model.ProduceLeaderCard;
import it.polimi.ingsw.server.model.personalboardpackage.DevelopmentSlot;

import java.util.List;

/**
 * A class used to represent the possible productions that a player can activate
 */
public class ProductionCombo {
    private final List<DevelopmentSlot> developmentSlots;
    private final List<ProduceLeaderCard> leaderCards;

    public ProductionCombo(List<DevelopmentSlot> developmentSlots, List<ProduceLeaderCard> leaderCards) {
        this.developmentSlots = developmentSlots;
        this.leaderCards = leaderCards;
    }

    public List<DevelopmentSlot> getDevelopmentSlots() {
        return developmentSlots;
    }

    public List<ProduceLeaderCard> getLeaderCards() {
        return leaderCards;
    }
}
