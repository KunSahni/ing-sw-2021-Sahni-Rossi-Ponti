package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;
import it.polimi.ingsw.server.model.utils.ProductionOutput;
import it.polimi.ingsw.server.model.utils.ResourceBank;

import java.util.Map;

/**
 * This class represents the action of choosing how to convert the chosen MarketMarbles into resources
 */
public class ResourceMarketConvertAction implements Forwardable{
    private final Map<MarketMarble, Integer> convertedMarbles;
    private final PersonalBoard personalBoard;

    public ResourceMarketConvertAction(PersonalBoard personalBoard, Map<MarketMarble, Integer> convertedMarbles) {
        this.convertedMarbles = convertedMarbles;
        this.personalBoard = personalBoard;
    }

    public Map<MarketMarble, Integer> getChosenMarbles() {
        return convertedMarbles;
    }

    @Override
    public void forward() {
        ProductionOutput convertedResources = ResourceBank.getResourceFromMarble(convertedMarbles);
        personalBoard.storeInDepots(convertedResources.getResources());
        personalBoard.getFaithTrack().moveMarker(convertedResources.getFaithIncrement());
    }
}
