package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.personalboard.FavorStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.client.utils.constants.Constants;

/**
 * This is a dumber version of a regular FaithTrack,
 * this class only contains data contained in a FaithTrack but has none of its logic.
 */
public class DumbFaithTrack implements Serializable {
    private int faithMarkerPosition;
    private List<FavorStatus> popesFavors;

    public DumbFaithTrack() {
        super();
    }

    public void updateFaithMarkerPosition(int faithMarkerPosition) {
        this.faithMarkerPosition = faithMarkerPosition;
    }

    public void updatePopesFavors(List<FavorStatus> popesFavors) {
        this.popesFavors = new ArrayList<>(popesFavors);
    }

    public int getFaithMarkerPosition() {
        return faithMarkerPosition;
    }

    public List<FavorStatus> getPopesFavors() {
        return popesFavors;
    }

    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    public String formatPrintableStringAt(int x, int y){
        final int[] faithTrackVictoryPoints = {0, 0, 0, 0, 1, 0, 0 ,0, 2, 0, 0 ,0, 4, 0, 0 ,0, 6, 0, 0 ,0, 9, 0, 0 ,0, 12, 0, 0 ,0, 16, 0, 0 ,0, 20};

        StringBuilder printableString = new StringBuilder("\033[" + x + ";" + y + "H╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        printableString.append("║");
        for(int i =0; i<=24; i++)
            if(i%8==0 && i>0)
                printableString.append(String.format(" %s%-2d%s ║", popesFavors.get(i/8-1).getStatusColor(), i, Constants.ANSI_RESET));
            else if(i >= 5 && i < 8 || i >= 12 && i < 16 || i >= 19)
                printableString.append(String.format(" %s%-2d%s ║", Constants.ANSI_ORANGE, i, Constants.ANSI_RESET));
            else
                printableString.append(String.format(" %-2d ║", i));
        printableString.append("\n║");
        for(int i =1; i<=24; i++)
            if(i == faithMarkerPosition)
                printableString.append(String.format( " %s  ║", Constants.FAITH_MARKER));
            else
                printableString.append("    ║");
        printableString.append("\n║");
        for(int i =0; i<=24; i++)
            if(faithTrackVictoryPoints[i]>0)
                printableString.append(String.format(" %s%-2d%s ║", Constants.ANSI_YELLOW, faithTrackVictoryPoints[i], Constants.ANSI_RESET));
            else
                printableString.append("    ║");
        printableString.append("\n╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");

        return printableString.toString();
    }
}
