package it.polimi.ingsw.server.model.personalboardpackage;

import java.util.*;
import java.time.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gamepackage.Player;

public class PersonalBoard implements VictoryPointsElement, StorageElement {
    private List<DevelopmentSlot> developmentSlots = new ArrayList<>();
    private LeaderCard leaderCard1;
    private LeaderCard leaderCard2;
    private Player player;
    private WarehouseDepots warehouseDepots;
    private Strongbox strongBox;
    private FaithTrack faithTrack;



    public PersonalBoard(LeaderCard leaderCard1, LeaderCard leaderCard2, Player player) {
        this.leaderCard1 = leaderCard1;
        this.leaderCard2 = leaderCard2;
        this.player = player;
    }

    public LeaderCard getLeaderCard1() {
        return leaderCard1;
    }

    public LeaderCard getLeaderCard2() {
        return leaderCard2;
    }

    public Player getPlayer() {
        return player;
    }

    public List<DevelopmentSlot> getDevelopmentSlots() {
        return developmentSlots;
    }
    public WarehouseDepots getWarehouseDepots() {
        return warehouseDepots;
    }

    public Strongbox getStrongBox() {
        return strongBox;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public void linkLeaderCards(LeaderCard _leaderCards) {
        if (_leaderCards != null) {
            getLeaderCards().add(_leaderCards);
        }
    }

    public void linkActiveDevelopmentCards(DevelopmentCard _activeDevelopmentCards) {
        if (_activeDevelopmentCards != null) {
            getActiveDevelopmentCards().add(_activeDevelopmentCards);
        }
    }

    public void linkWarehouseDepots(WarehouseDepots _warehouseDepots) {
        if (_warehouseDepots != null) {
            _warehouseDepots.unlink();
            _warehouseDepots.set(this);
        }

        unlinkWarehouseDepots();
        setWarehouseDepots(_warehouseDepots);
    }

    public void linkStrongBox(Strongbox _strongBox) {
        if (_strongBox != null) {
            _strongBox.unlink();
            _strongBox.set(this);
        }

        unlinkStrongBox();
        setStrongBox(_strongBox);
    }

    public void unlink() {
        if (get() != null) {
            get().set(null);
            set(null);
        }
    }

    public void unlinkLeaderCards(LeaderCard _leaderCards) {
        if (_leaderCards != null) {
            getLeaderCards().remove(_leaderCards);
        }
    }

    public void unlinkLeaderCards(Iterator<LeaderCard> it) {
        it.remove();
    }

    public void unlinkActiveDevelopmentCards(DevelopmentCard _activeDevelopmentCards) {
        if (_activeDevelopmentCards != null) {
            getActiveDevelopmentCards().remove(_activeDevelopmentCards);
        }
    }

    public void unlinkActiveDevelopmentCards(Iterator<DevelopmentCard> it) {
        it.remove();
    }

    public void unlinkWarehouseDepots() {
        if (getWarehouseDepots() != null) {
            getWarehouseDepots().set(null);
            setWarehouseDepots(null);
        }
    }

    public void unlinkStrongBox() {
        if (getStrongBox() != null) {
            getStrongBox().set(null);
            setStrongBox(null);
        }
    }

    @Override
    // ----------- << method.annotations@AAAAAAF4RjrUDX8u+pU= >>
    // ----------- >>
    public int getVictoryPoints() {
    // ----------- << method.body@AAAAAAF4RjrUDX8u+pU= >>
    // ----------- >>
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
    /**
    * @param card
    */

    // ----------- << method.annotations@AAAAAAF4QTY3Z7I65n8= >>
    // ----------- >>
    public void discardLeaderCard(LeaderCard card) {
    // ----------- << method.body@AAAAAAF4QTY3Z7I65n8= >>
    // ----------- >>
    }
    /**
    * @param card 
    * @param position
    */

    // ----------- << method.annotations@AAAAAAF4RebhDbspqKc= >>
    // ----------- >>
    public void placeDevelopmentCard(DevelopmentCard card, int position) {
    // ----------- << method.body@AAAAAAF4RebhDbspqKc= >>
    // ----------- >>
    }
    /**
    * @param card
    */

    // ----------- << method.annotations@AAAAAAF4Rekthd6lY+Y= >>
    // ----------- >>
    public List<Boolean> availableDevelopmentCardSlots(DevelopmentCard card) {
    // ----------- << method.body@AAAAAAF4Rekthd6lY+Y= >>
    // ----------- >>
    }
// ----------- << class.extras@AAAAAAF4No/AYXFbLkU= >>
// ----------- >>
}