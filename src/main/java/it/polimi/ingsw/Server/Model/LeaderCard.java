package it.polimi.ingsw.server.model;


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
        LeaderCardAbility a;
        a = ability;
        return a;
    }

    public LeaderCardRequirements getLeaderCardRequirements() {
        return leaderCardRequirements;
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
}