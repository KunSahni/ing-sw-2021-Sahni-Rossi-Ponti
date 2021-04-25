package it.polimi.ingsw.server.controller.message.choice;

import it.polimi.ingsw.server.controller.message.choice.ResourceMarketConvertMessage;
import it.polimi.ingsw.server.model.market.MarketMarble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceMarketConvertMessageTest {
    ResourceMarketConvertMessage resourceMarketConvertMessage;
    Map<MarketMarble, Integer> chosenMarbles;

    @BeforeEach
    void init(){
        chosenMarbles = Map.of(MarketMarble.GREY, 1);
        resourceMarketConvertMessage = new ResourceMarketConvertMessage(chosenMarbles);
    }

    @Test
    @DisplayName("Chosen marbles have been set correctly")
    void setChosenMarblesTest(){
        assertEquals(chosenMarbles, resourceMarketConvertMessage.getChosenMarbles());
    }
}
