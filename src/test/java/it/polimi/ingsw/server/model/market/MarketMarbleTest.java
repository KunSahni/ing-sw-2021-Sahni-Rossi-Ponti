package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class MarketMarbleTest {

    @ParameterizedTest
    @MethodSource("marblesColors")
    @DisplayName("getColor method test")
    void getColor(MarketMarble marketMarble, String color) {
        assertEquals(color, marketMarble.getMarbleColor(), "getColor returned different marble than expected");
    }

    private static Stream<Arguments> marblesColors() {
        return Stream.of(
                arguments(MarketMarble.WHITE, Constants.ANSI_WHITE),
                arguments(MarketMarble.RED, Constants.ANSI_RED),
                arguments(MarketMarble.BLUE, Constants.ANSI_BLUE),
                arguments(MarketMarble.GREY, Constants.ANSI_GREY),
                arguments(MarketMarble.PURPLE, Constants.ANSI_PURPLE),
                arguments(MarketMarble.YELLOW, Constants.ANSI_YELLOW)
        );
    }

    @ParameterizedTest
    @MethodSource("marblesResources")
    @DisplayName("toResource method test")
    void toResource(MarketMarble marketMarble, Resource resource) {
        assertEquals(resource, marketMarble.toResource(), "toResource returned different resource than expected");
    }

    private static Stream<Arguments> marblesResources() {
        return Stream.of(
                arguments(MarketMarble.WHITE, null),
                arguments(MarketMarble.RED, null),
                arguments(MarketMarble.BLUE, Resource.SHIELD),
                arguments(MarketMarble.GREY, Resource.STONE),
                arguments(MarketMarble.PURPLE, Resource.SERVANT),
                arguments(MarketMarble.YELLOW, Resource.COIN)
        );
    }
}