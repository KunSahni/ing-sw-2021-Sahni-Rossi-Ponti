package it.polimi.ingsw.client.utils.dumbobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a dumber version of a regular PersonalBoard,
 * this class contains data contained in a PersonalBoard but has none of its logic.
 * It also contains some of the data contained in a Player.
 */
public class DumbPersonalBoard {
    private final String nickname;
    private final int position;
    private final DumbFaithTrack faithTrack;
    private final DumbResourceManager depots;
    private final DumbResourceManager strongbox;
    private List<DumbLeaderCard> leaderCards;
    private final List<DumbDevelopmentCardSlot> developmentCardSlots;
    private boolean turnStatus;
    private boolean connectionStatus;

    public DumbPersonalBoard(String nickname, int position) {
        super();
        this.nickname = nickname;
        this.position = position;
        this.faithTrack = new DumbFaithTrack();
        this.depots = new DumbResourceManager();
        this.strongbox = new DumbResourceManager();
        this.developmentCardSlots = new ArrayList<>();
        this.developmentCardSlots.add(new DumbDevelopmentCardSlot(1));
        this.developmentCardSlots.add(new DumbDevelopmentCardSlot(2));
        this.developmentCardSlots.add(new DumbDevelopmentCardSlot(3));
    }

    public void updateLeaderCards(List<DumbLeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public void updateTurnStatus(boolean turn) {
        turnStatus = turn;
    }

    public void updateConnectionStatus(boolean connected) {
        connectionStatus = connected;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPosition() {
        return position;
    }

    public DumbFaithTrack getFaithTrack() {
        return faithTrack;
    }

    public DumbResourceManager getDepots() {
        return depots;
    }

    public DumbResourceManager getStrongbox() {
        return strongbox;
    }

    public List<DumbLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public List<DumbDevelopmentCardSlot> getDevelopmentCardSlots() {
        return developmentCardSlots;
    }

    public boolean getTurnStatus() {
        return turnStatus;
    }

    public boolean getConnectionStatus() {
        return connectionStatus;
    }
}
