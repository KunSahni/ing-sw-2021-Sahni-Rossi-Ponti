package it.polimi.ingsw.server.controller.message.choice;

import it.polimi.ingsw.server.model.market.MarketMarble;

import java.util.Map;

/**
 * This class contains a copy of the chosen MarketMarbles from which the user will need to choose which ones to keep and how to convert the white marbles
 */
public class ResourceMarketConvertMessage {
    private final Map<MarketMarble, Integer> chosenMarbles;

    public ResourceMarketConvertMessage(Map<MarketMarble, Integer> chosenMarbles) {
        this.chosenMarbles = chosenMarbles;
    }

    public Map<MarketMarble, Integer> getChosenMarbles() {
        return Map.copyOf(chosenMarbles);
    }
}
