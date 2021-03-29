/*
* This code has been generated by the Rebel: a code generator for modern Java.
*
* Drop us a line or two at feedback@archetypesoftware.com: we would love to hear from you!
*/
package it.polimi.ingsw.server.model;

import java.util.*;
import java.time.*;



public abstract class LeaderCard implements VictoryPointsElement {

    private LeaderCardAbility ability;

    private int victoryPoints;

    private boolean active;

    private LeaderCardsDeck ;

    private LeaderCardAbility ;

    private LeaderCardAbility ;

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

    private boolean isActive() {
        boolean b;
        b = active;
        return b;
    }

    public LeaderCardsDeck get() {
        return ;
    }

    public LeaderCardAbility get() {
        return ;
    }

    public LeaderCardAbility get() {
        return ;
    }

    private void setActive(boolean active) {
        this.active = active;
    }

    public void set(LeaderCardsDeck ) {
        this. = ;
    }

    public void set(LeaderCardAbility ) {
        this. = ;
    }

    public void set(LeaderCardAbility ) {
        this. = ;
    }

    /**
     * set active true when the leader card is activated
     */
    public void activate() {
        active=true;
    }


    /**
     *
     * @return true if the leader card is active
     */
    public boolean isActive() {
        return active;
    }

}