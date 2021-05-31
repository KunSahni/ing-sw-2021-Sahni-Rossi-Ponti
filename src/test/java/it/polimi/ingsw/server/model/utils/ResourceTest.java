package it.polimi.ingsw.server.model.utils;

import it.polimi.ingsw.server.model.market.MarketMarble;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ResourceTest {

    @ParameterizedTest
    @MethodSource("marblesResources")
    @DisplayName("toMarble method test")
    void toMarble(MarketMarble marketMarble, Resource resource) {
        assertEquals(marketMarble, resource.toMarble(), "toResource returned different resource than expected");
    }

    private static Stream<Arguments> marblesResources() {
        return Stream.of(
                arguments(MarketMarble.BLUE, Resource.SHIELD),
                arguments(MarketMarble.GREY, Resource.STONE),
                arguments(MarketMarble.PURPLE, Resource.SERVANT),
                arguments(MarketMarble.YELLOW, Resource.COIN)
        );
    }
}