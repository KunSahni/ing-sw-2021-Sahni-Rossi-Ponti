package it.polimi.ingsw.server.model;


public abstract class LeaderCard implements VictoryPointsElement {
    private LeaderCardAbility ability;
    private int victoryPoints;
    private boolean active;

    public LeaderCardAbility getAbility() {
        LeaderCardAbility a;
        a = ability;
        return a;
    }

    public int getVictoryPoints() {
        int v;
        v = victoryPoints;
        return v;
    }
    public boolean isActive(){
        return active;
    }

    public void activate(){
        active = true;
    }
}