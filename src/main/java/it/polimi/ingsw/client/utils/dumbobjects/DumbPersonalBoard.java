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
    private int position;
    private final DumbFaithTrack faithTrack;
    private final DumbResourceManager depots;
    private final DumbResourceManager strongbox;
    private List<DumbLeaderCard> leaderCards;
    private final List<DumbDevelopmentCardSlot> developmentCardSlots;
    private boolean turnStatus;
    private boolean connectionStatus;

    public DumbPersonalBoard(String nickname, boolean isSinglePlayerGame) {
        super();
        this.nickname = nickname;
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

    public void updatePosition(int updatedPosition){
        this.position = updatedPosition;
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
     * @return a string representation of a personal board
     */
    public String formatPrintableString() {
        StringBuilder printableString = new StringBuilder(Constants.ANSI_CLEAR);
        if(turnStatus)
            printableString.append("\033[1;1H" + Constants.ANSI_ORANGE + "Nickname:" + nickname + "(in turn)" + Constants.ANSI_RESET + " \033[1;117Hposition:" + position + "");
        else if(!connectionStatus)
            printableString.append("\033[1;1H" + Constants.ANSI_GREY + "Nickname:" + nickname + "(offline)" + Constants.ANSI_RESET + " \033[1;117Hposition:" + position + "");
        else
            printableString.append("\033[1;1HNickname:" + nickname + " \033[1;117Hposition:" + position + "");
        printableString.append(faithTrack.formatPrintableStringAt(2, 1));
        printableString.append(depots.formatPrintableStringAtAsDepots(8, 1));
        printableString.append(strongbox.formatPrintableStringAtAsStrongbox(10, 22));
        if(leaderCards.size()>0 && leaderCards.get(0)!=null)
            printableString.append(leaderCards.get(0).formatPrintableStringAt(8, 31));
        if(leaderCards.size()>1 && leaderCards.get(1)!=null)
            printableString.append(leaderCards.get(1).formatPrintableStringAt(8, 48));

        printableString.append(developmentCardSlots.get(0).formatPrintableStringAt(8, 65));
        printableString.append(developmentCardSlots.get(1).formatPrintableStringAt(8, 82));
        printableString.append(developmentCardSlots.get(2).formatPrintableStringAt(8, 99));

        return printableString.toString();
    }
}
