package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;


/**
 * This is a dumber version of a regular LeaderCard,
 * this class only contains the data stored in a LeaderCard but has none of its logic.
 */
public abstract class DumbLeaderCard {
    private final LeaderCardAbility ability;
    private final int victoryPoints;
    private boolean active;
    private final LeaderCardRequirements leaderCardRequirements;


    public DumbLeaderCard(LeaderCard leaderCard) {
        this.ability = leaderCard.getAbility();
        this.victoryPoints = leaderCard.getVictoryPoints();
        this.leaderCardRequirements = leaderCard.getLeaderCardRequirements();
    }

    public void activate(){
        active = true;
    }

    public LeaderCardAbility getAbility() {
        return ability;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean isActive() {
        return active;
    }

    public LeaderCardRequirements getLeaderCardRequirements() {
        return leaderCardRequirements;
    }
}
