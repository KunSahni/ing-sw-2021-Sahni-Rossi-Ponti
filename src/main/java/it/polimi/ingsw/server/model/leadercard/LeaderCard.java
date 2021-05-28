package it.polimi.ingsw.server.model.leadercard;


import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.server.model.utils.VictoryPointsElement;

public abstract class LeaderCard implements VictoryPointsElement {
    private final LeaderCardAbility ability;
    private final int victoryPoints;
    private boolean active;
    private final LeaderCardRequirements leaderCardRequirements;

    public LeaderCard(LeaderCardAbility ability, int victoryPoints, LeaderCardRequirements leaderCardRequirements) {
        this.ability = ability;
        this.victoryPoints = victoryPoints;
        this.leaderCardRequirements = leaderCardRequirements;
        this.active = false;
    }

    public LeaderCardAbility getAbility() {
        return ability;
    }

    public LeaderCardRequirements getLeaderCardRequirements() {
        return new LeaderCardRequirements(this.leaderCardRequirements.getRequiredDevelopmentCards(), this.leaderCardRequirements.getRequiredResources());
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }
    public boolean isActive(){
        return active;
    }

    public void activate(){
        active = true;
    }

    public abstract DumbLeaderCard convertToDumb();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeaderCard)) return false;
        LeaderCard that = (LeaderCard) o;
        return victoryPoints == that.victoryPoints && ability == that.ability && leaderCardRequirements.equals(that.leaderCardRequirements);
    }
}