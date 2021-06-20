package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;

import java.io.Serializable;
import java.util.Objects;


/**
 * This is a dumber version of a regular LeaderCard,
 * this class only contains the data stored in a LeaderCard but has none of its logic.
 */
public abstract class DumbLeaderCard implements Serializable {
    private final LeaderCardAbility ability;
    private final int victoryPoints;
    private boolean active;
    private final LeaderCardRequirements leaderCardRequirements;


    public DumbLeaderCard(LeaderCard leaderCard) {
        this.ability = leaderCard.getAbility();
        this.victoryPoints = leaderCard.getVictoryPoints();
        this.leaderCardRequirements = leaderCard.getLeaderCardRequirements();
        this.active = leaderCard.isActive();
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

    public abstract LeaderCard convert();

    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    public abstract String formatPrintableStringAt(int x, int y); //todo: make it better by not using hardcoded data

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DumbLeaderCard)) return false;
        DumbLeaderCard that = (DumbLeaderCard) o;
        return victoryPoints == that.victoryPoints && active == that.active && ability == that.ability && Objects.equals(leaderCardRequirements, that.leaderCardRequirements);
    }

    public String toImgPath() {
        return "/img/cards/";
    }
}
