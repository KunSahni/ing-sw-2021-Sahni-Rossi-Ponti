package it.polimi.ingsw.server.model.market;

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
        assertEquals(color, marketMarble.getColor(), "getColor returned different marble than expected");
    }

    private static Stream<Arguments> marblesColors() {
        return Stream.of(
                arguments(MarketMarble.WHITE, "white"),
                arguments(MarketMarble.RED, "red"),
                arguments(MarketMarble.BLUE, "blue"),
                arguments(MarketMarble.GREY, "grey"),
                arguments(MarketMarble.PURPLE, "purple"),
                arguments(MarketMarble.YELLOW, "yellow")
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