package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.utils.constants.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This is a dumber version of a regular DevelopmentCardSlot,
 * this class only contains the data stored in a DevelopmentCardSlot but has none of its logic.
 */
public class DumbDevelopmentCardSlot implements Serializable {
    private final Stack<DumbDevelopmentCard> developmentCards;
    private final int slotIndex;

    public DumbDevelopmentCardSlot(int slotIndex) {
        this.developmentCards = new Stack<>();
        this.slotIndex = slotIndex;
    }

    public void updateDevelopmentCards(List<DumbDevelopmentCard> updateDevelopmentCards) {
        this.developmentCards.clear();
        this.developmentCards.addAll(updateDevelopmentCards);
    }

    public List<DumbDevelopmentCard> getDevelopmentCards() {
        return new ArrayList<>(developmentCards);
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    public String formatPrintableStringAt(int x, int y) {
        if(developmentCards.empty())
            return    "\033[" + x + ";" + y + "H╔══════════════╗"
                    + "\033[" + (x+1) + ";" + y + "H║              ║"
                    + "\033[" + (x+2) + ";" + y + "H║              ║"
                    + "\033[" + (x+3) + ";" + y + "H║              ║"
                    + "\033[" + (x+4) + ";" + y + "H║              ║"
                    + "\033[" + (x+5) + ";" + y + "H║              ║"
                    + "\033[" + (x+6) + ";" + y + "H║              ║"
                    + "\033[" + (x+7) + ";" + y + "H║              ║"
                    + "\033[" + (x+8) + ";" + y + "H║              ║"
                    + "\033[" + (x+9) + ";" + y + "H║              ║"
                    + "\033[" + (x+10) + ";" + y + "H╚══════════════╝";

        //if not empty, draw the top card and the victoryPoints from the bottom ones
        StringBuilder printableString = new StringBuilder(developmentCards.peek().formatPrintableStringAt(x, y));
        if(developmentCards.size()>=2)
            printableString.append(
                    "\033[" + (x+11) + ";" + y + "H║    " + Constants.ANSI_YELLOW + developmentCards.get(1).getVictoryPoints() + Constants.ANSI_RESET+ "     ║"
                    + "\033[" + (x+12) + ";" + y + "H╚══════════════╝"
            );
        if(developmentCards.size()==3)
            printableString.append(
                    "\033[" + (x+13) + ";" + y + "H║    " + Constants.ANSI_YELLOW + developmentCards.get(2).getVictoryPoints() + Constants.ANSI_RESET+ "     ║"
                            + "\033[" + (x+14) + ";" + y + "H╚══════════════╝"
            );

        return printableString.toString();
    }
}
