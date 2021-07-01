package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.leadercard.ConvertLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This is a dumber version of a regular ConvertLeaderCard,
 * this class only contains the data stored in a ConvertLeaderCard but has none of its logic.
 */
public class DumbConvertLeaderCard extends DumbLeaderCard{
    private final Resource convertedResource;

    public DumbConvertLeaderCard(ConvertLeaderCard leaderCard) {
        super(leaderCard);
        convertedResource = leaderCard.getConvertedResource();
    }

    public Resource getConvertedResource() {
        return convertedResource;
    }


    @Override
    public ConvertLeaderCard convert() {
        return new ConvertLeaderCard(getVictoryPoints(), getLeaderCardRequirements(), convertedResource);
    }

    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    @Override
    public String formatPrintableStringAt(int x, int y) {
        //contains the colors of both requirements sorted by quantity
        List<Color> colors = this.getLeaderCardRequirements()
                .getRequiredDevelopmentCards()
                .entrySet()
                .stream()
                .sorted(
                        Comparator.comparingInt(
                                obj -> obj.getValue().getQuantity()))
                .map(
                        Map.Entry::getKey
                )
                .collect(Collectors.toList());

        return    "\033["+ x +";"+ y +"H╔══════════════╗"
                + "\033["+ (x+1) +";"+ y +"H║ 2x " + colors.get(1).getColoredLevel() + Constants.ANSI_RESET + "         ║"
                + "\033["+ (x+2) +";"+ y +"H║ 1x " + colors.get(0).getColoredLevel() + Constants.ANSI_RESET + "         ║"
                + "\033["+ (x+3) +";"+ y +"H║              ║"
                + "\033["+ (x+4) +";"+ y +"H║              ║"
                + "\033["+ (x+5) +";"+ y +"H║ 1x " + Constants.WHITE_MARBLE + " -> 1x " + convertedResource.formatPrintableString() + " ║"
                + "\033["+ (x+6) +";"+ y +"H║              ║"
                + "\033["+ (x+7) +";"+ y +"H║              ║"
                + "\033["+ (x+8) +";"+ y +"H║      " + Constants.ANSI_YELLOW + getVictoryPoints() + Constants.ANSI_RESET+ "     " + (isActive()? Constants.GREEN_TICK:Constants.RED_CROSS) + " ║"
                + "\033["+ (x+9) +";"+ y +"H╚══════════════╝";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DumbConvertLeaderCard)) return false;
        if (!super.equals(o)) return false;
        DumbConvertLeaderCard that = (DumbConvertLeaderCard) o;
        return convertedResource == that.convertedResource;
    }

    @Override
    public String toImgPath() {
        return super.toImgPath() + "leader_card_convert_" + convertedResource + ".png";
    }
}
