/*
* This code has been generated by the Rebel: a code generator for modern Java.
*
* Drop us a line or two at feedback@archetypesoftware.com: we would love to hear from you!
*/
package it.polimi.ingsw.server.model;

import java.util.*;
import java.time.*;



// ----------- << imports@AAAAAAF4O3bmV0hIQow= >>
// ----------- >>

// ----------- << class.annotations@AAAAAAF4O3bmV0hIQow= >>
// ----------- >>
public class StoreLeaderCard extends LeaderCard implements StorageElement {
    // ----------- << attribute.annotations@AAAAAAF4O4s+P95dLuQ= >>
    // ----------- >>
    private Resource requirement;

    // ----------- << attribute.annotations@AAAAAAF4O5FAdzlb1Mk= >>
    // ----------- >>
    private boolean slot1;

    // ----------- << attribute.annotations@AAAAAAF4O5JZTz5J3ak= >>
    // ----------- >>
    private boolean slot2;

    public Resource getRequirement() {
        return requirement;
    }

    public boolean isSlot1() {
        return slot1;
    }

    public boolean isSlot2() {
        return slot2;
    }

    public void setSlot1(boolean slot1) {
        this.slot1 = slot1;
    }

    public void setSlot2(boolean slot2) {
        this.slot2 = slot2;
    }

    /**
    * @param addResources
    */

    @Override
    // ----------- << method.annotations@AAAAAAF4Wk109UDSeb8= >>
    // ----------- >>
    public void storeResources(Map<Resource, Integer> addResources) {
    // ----------- << method.body@AAAAAAF4Wk109UDSeb8= >>
    // ----------- >>
    }
    /**
    * @param delResources
    */

    @Override
    // ----------- << method.annotations@AAAAAAF4Wk3BUEnAsZ4= >>
    // ----------- >>
    public void discardResources(Map<Resource, Integer> delResources) {
    // ----------- << method.body@AAAAAAF4Wk3BUEnAsZ4= >>
    // ----------- >>
    }
    @Override
    // ----------- << method.annotations@AAAAAAF4Wk4Ui2QVgjg= >>
    // ----------- >>
    public Map<Resource, Integer> peekResources() {
    // ----------- << method.body@AAAAAAF4Wk4Ui2QVgjg= >>
    // ----------- >>
    }
    @Override
    // ----------- << method.annotations@AAAAAAF4RjrUDX8u+pU= >>
    // ----------- >>
    public int getVictoryPoints() {
    // ----------- << method.body@AAAAAAF4RjrUDX8u+pU= >>
    // ----------- >>
    }
// ----------- << class.extras@AAAAAAF4O3bmV0hIQow= >>
// ----------- >>
}