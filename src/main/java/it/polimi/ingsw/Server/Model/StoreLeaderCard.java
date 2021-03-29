package it.polimi.ingsw.server.model;

import java.util.*;
import java.time.*;




public class StoreLeaderCard extends LeaderCard implements StorageElement {

    private Resource requirement;


    private boolean slot1;


    private boolean slot2;

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

    public void storeResources(Map<Resource, Integer> addResources) {

    }
    /**
    * @param delResources
    */

    @Override

    public void discardResources(Map<Resource, Integer> delResources) {

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