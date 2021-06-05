package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.utils.constants.Constants;

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

    public DumbPersonalBoard(String nickname, int position, boolean isSinglePlayerGame) {
        super();
        this.nickname = nickname;
        this.position = position;
        this.faithTrack = isSinglePlayerGame? new DumbSinglePlayerFaithTrack() : new DumbFaithTrack();
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


    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    public String formatPrintableStringAt(int x, int y) {
        StringBuilder printableString = new StringBuilder(Constants.ANSI_CLEAR);
        
        printableString.append("\033[1;1HNickname:" + nickname + " \033[1;117Hposition:" + position + "");
        printableString.append(faithTrack.formatPrintableStringAt(2, 1));
        printableString.append(depots.formatPrintableStringAtAsDepots(8, 1));
        printableString.append(strongbox.formatPrintableStringAtAsStrongbox(10, 22));
        if(leaderCards.get(0)!=null)
            printableString.append(leaderCards.get(0).formatPrintableStringAt(8, 31));
        if(leaderCards.get(1)!=null)
            printableString.append(leaderCards.get(1).formatPrintableStringAt(8, 48));

        printableString.append(developmentCardSlots.get(0).formatPrintableStringAt(8, 52));
        printableString.append(developmentCardSlots.get(1).formatPrintableStringAt(8, 69));
        printableString.append(developmentCardSlots.get(2).formatPrintableStringAt(8, 86));

        return printableString.toString();
    }
}
