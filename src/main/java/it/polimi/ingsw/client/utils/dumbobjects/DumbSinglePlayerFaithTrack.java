package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.utils.constants.Constants;

/**
 * This is a dumber version of a regular SinglePlayerFaithTrack,
 * this class only contains data contained in a SinglePlayerFaithTrack but has none of its logic.
 */
public class DumbSinglePlayerFaithTrack extends DumbFaithTrack{
    private int blackCrossPosition;

    public void updateBlackCrossPosition(int blackCrossPosition) {
        this.blackCrossPosition = blackCrossPosition;
    }

    public int getBlackCrossPosition() {
        return blackCrossPosition;
    }

    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    public String formatPrintableStringAt(int x, int y){
        final int[] faithTrackVictoryPoints = {0, 0, 0, 0, 1, 0, 0 ,0, 2, 0, 0 ,0, 4, 0, 0 ,0, 6, 0, 0 ,0, 9, 0, 0 ,0, 12, 0, 0 ,0, 16, 0, 0 ,0, 20};


        String printableString = "\033[" + x + ";" + y + "H╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗";
        printableString.concat("║");
        for(int i =0; i<=24; i++)
            if(i%8==0 && i>0)
                printableString.concat(String.format(" %s%-2d%s ║", this.getPopesFavors().get(i/8).getStatusColor(), i, Constants.ANSI_RESET));
            else if(i >= 5 && i < 8 || i >= 12 && i < 16 || i >= 19)
                printableString.concat(String.format(" %s%-2d%s ║", Constants.ANSI_ORANGE, i, Constants.ANSI_RESET));
            else
                printableString.concat(String.format(" %-2d ║", i));
        printableString.concat("\n║");
        for(int i =1; i<=24; i++)
            if(i == this.getFaithMarkerPosition())
                printableString.concat(String.format( " %s  ║", Constants.FAITH_MARKER));
            else if(i == this.getBlackCrossPosition())
                printableString.concat(String.format( " %s  ║", Constants.BLACK_CROSS));
            else
                printableString.concat("    ║");
        printableString.concat("\n║");
        for(int i =0; i<=24; i++)
            if(faithTrackVictoryPoints[i]>0)
                printableString.concat(String.format(" %s%-2d%s ║", Constants.ANSI_YELLOW, faithTrackVictoryPoints[i], Constants.ANSI_RESET));
            else
                printableString.concat("    ║");
        printableString.concat("\n╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");

        return printableString;
    }
}
