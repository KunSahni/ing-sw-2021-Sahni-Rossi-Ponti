package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.leadercard.ProduceLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.Objects;
import java.util.TreeMap;

/**
 * This is a dumber version of a regular ProduceLeaderCard,
 * this class only contains the data stored in a ProduceLeaderCard but has none of its logic.
 */
public class DumbProduceLeaderCard extends DumbLeaderCard{
    private final Resource inputResource;
    private final int faithIncrement;

    public DumbProduceLeaderCard(ProduceLeaderCard leaderCard) {
        super(leaderCard);
        inputResource = leaderCard.getInputResource();
        faithIncrement = leaderCard.getFaithIncrement();
    }

    public Resource getInputResource() {
        return inputResource;
    }

    public int getFaithIncrement() {
        return faithIncrement;
    }


    @Override
    public ProduceLeaderCard convert() {
        return new ProduceLeaderCard(getVictoryPoints(), getLeaderCardRequirements(), inputResource, faithIncrement);
    }

    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    @Override
    public String formatPrintableStringAt(int x, int y) {
        //contains the only color requirement
        Color color = new TreeMap<>(this.getLeaderCardRequirements().getRequiredDevelopmentCards()).firstEntry().getKey();

        return    "\033["+ x +";"+ y +"H╔══════════════╗"
                + "\033["+ (x+1) +";"+ y +"H║ 1x " + color.getColoredLevel() + color.getColoredLevel() + Constants.ANSI_RESET + "        ║"
                + (isActive()? "\033["+ (x+2) +";"+ y +"H║   activated  ║" : "\033["+ (x+2) +";"+ y +"H║              ║")
                + "\033["+ (x+3) +";"+ y +"H║              ║"
                + "\033["+ (x+4) +";"+ y +"H║              ║"
                + "\033["+ (x+5) +";"+ y +"H║    " + inputResource.formatPrintableString() + " -> " + Constants.ANY_RESOURCE + "    ║"
                + "\033["+ (x+6) +";"+ y +"H║         " + Constants.FAITH_POINT + "    ║"
                + "\033["+ (x+7) +";"+ y +"H║              ║"
                + "\033["+ (x+8) +";"+ y +"H║      " + Constants.ANSI_YELLOW + getVictoryPoints() + Constants.ANSI_RESET+ "       ║"
                + "\033["+ (x+9) +";"+ y +"H╚══════════════╝";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DumbProduceLeaderCard)) return false;
        if (!super.equals(o)) return false;
        DumbProduceLeaderCard that = (DumbProduceLeaderCard) o;
        return faithIncrement == that.faithIncrement && inputResource == that.inputResource;
    }

    @Override
    public String toImgPath() {
        return super.toImgPath() + "leader_card_produce_" + inputResource + ".png";
    }
}
