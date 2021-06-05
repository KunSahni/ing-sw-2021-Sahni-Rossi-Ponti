package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.leadercard.DiscountLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.stream.Collectors;

/**
 * This is a dumber version of a regular DiscountLeaderCard,
 * this class only contains the data stored in a DiscountLeaderCard but has none of its logic.
 */
public class DumbDiscountLeaderCard extends DumbLeaderCard{
    private final Resource discountedResource;

    public DumbDiscountLeaderCard(DiscountLeaderCard leaderCard) {
        super(leaderCard);
        this.discountedResource = leaderCard.getDiscountedResource();
    }

    public Resource getDiscountedResource() {
        return discountedResource;
    }


    @Override
    public DiscountLeaderCard convert() {
        return new DiscountLeaderCard(getVictoryPoints(), getLeaderCardRequirements(), discountedResource);
    }

    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    @Override
    public String formatPrintableStringAt(int x, int y) {
        //contains the color of the first requirement
        Color color1 = this.getLeaderCardRequirements()
                .getRequiredDevelopmentCards()
                .keySet()
                .stream()
                .findFirst()
                .get();

        //contains the color of the second requirement
        Color color2 = this.getLeaderCardRequirements()
                .getRequiredDevelopmentCards()
                .keySet()
                .stream()
                .findFirst()
                .get();

        return    "\033["+ x +";"+ y +"H╔══════════════╗"
                + "\033["+ (x+1) +";"+ y +"H║ 1x " + color1 + Constants.LEVEL + Constants.ANSI_RESET + "         ║"
                + "\033["+ (x+2) +";"+ y +"H║ 1x " + color2 + Constants.LEVEL + Constants.ANSI_RESET + "         ║"
                + "\033["+ (x+3) +";"+ y +"H║              ║"
                + "\033["+ (x+4) +";"+ y +"H║              ║"
                + "\033["+ (x+5) +";"+ y +"H║              ║"
                + "\033["+ (x+6) +";"+ y +"H║    -1 " + discountedResource.formatPrintableString() + "      ║"
                + "\033["+ (x+7) +";"+ y +"H║              ║"
                + "\033["+ (x+8) +";"+ y +"H║      " + Constants.ANSI_YELLOW + getVictoryPoints() + Constants.ANSI_RESET+ "       ║"
                + "\033["+ (x+9) +";"+ y +"H╚══════════════╝";
    }
}
