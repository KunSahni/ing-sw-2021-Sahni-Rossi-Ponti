package it.polimi.ingsw.server.model.personalboardpackage;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.leadercard.StoreLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;
import it.polimi.ingsw.server.model.utils.ResourceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PersonalBoardTest {
    PersonalBoard personalBoard;
    Player player;

    @BeforeEach
    void setUp() {
        Game game = new Game(1, 1);
        player = new Player("Mario",game);
        personalBoard = new PersonalBoard(player);
    }


    @Nested
    class TestLeaderCardFeautures{
        List<LeaderCard> leaderCards;
        LeaderCardsDeck leaderCardsDeck;

        @BeforeEach
        void setUp() {
            leaderCardsDeck = new LeaderCardsDeck();
            leaderCards = leaderCardsDeck.popFour();
            leaderCards.remove(3);
            leaderCards.remove(2);
            personalBoard.setLeaderCards(leaderCards);
        }

        @Test
        void testSetLeaderCardsAndGetLeaderCards() {
            List<LeaderCard> actualLeaderCards = personalBoard.getLeaderCards();
            assertEquals(leaderCards, actualLeaderCards, "Error: was expecting " + leaderCards + ", but received " + actualLeaderCards);
        }

        @Test
        void testDiscardLeaderCard() {
            LeaderCard removableCard = leaderCards.remove(1);
            personalBoard.discardLeaderCard(removableCard);
            List<LeaderCard> actualLeaderCards = personalBoard.getLeaderCards();
            assertEquals(leaderCards, actualLeaderCards, "Error: was expecting " + leaderCards + ", but received " + actualLeaderCards);
        }

        @ParameterizedTest
        @EnumSource(LeaderCardAbility.class)
        void testContainsLeaderCardRequirementsWhenTrue(LeaderCardAbility leaderCardAbility) {
            leaderCards.forEach(
                    leaderCard -> personalBoard.discardLeaderCard(leaderCard)
            );
            DevelopmentCardsBoard developmentCardsBoard = new DevelopmentCardsBoard();
            LeaderCard leaderCard = getLeaderCardWithAbility(leaderCardAbility);

            //Sets the board so that it will surely contain all the requirements needed by the above created leaderCard
            switch (leaderCardAbility){
                case CONVERT -> personalBoard.storeInDepots(leaderCard.getLeaderCardRequirements().getRequiredResources());
                case PRODUCE -> {
                    //Extract the Color of the level 2 DevelopmentCard needed to activate this ProduceLeaderCard
                    Color requiredColor = leaderCard.getLeaderCardRequirements()
                            .getRequiredDevelopmentCards()
                            .entrySet()
                            .stream()
                            .findFirst()
                            .get()
                            .getKey();
                    //Places a level 1 and a level 2 DevelopmentCard of the requiredColor on the personalBoard
                    Stream.of(Level.values())
                            .filter(
                                    level -> !level.equals(Level.LEVEL3)
                            ).forEachOrdered(
                            level -> personalBoard.placeDevelopmentCard(
                                    developmentCardsBoard.pick(
                                            level, requiredColor
                                    ),
                                    1
                            )
                    );
                }
                //STORE and CONVERT LeaderCards have similar requirements, so this places all the required DevelopmentCards on the on the personalBoard
                default -> leaderCard.getLeaderCardRequirements()
                        .getRequiredDevelopmentCards()
                        .entrySet()
                        .stream()
                        .forEach(
                        colorLevelQuantityPairEntry -> IntStream.range(0, colorLevelQuantityPairEntry.getValue().getQuantity())
                                .forEach(
                                $ -> {
                                    int position = 0;
                                    personalBoard.placeDevelopmentCard(
                                        developmentCardsBoard.pick(
                                                colorLevelQuantityPairEntry.getValue().getLevel(), colorLevelQuantityPairEntry.getKey()
                                        ),
                                        position++
                                    );
                                }
                        )
                );
            }

            //assertTrue because the board surely contains all the requirements
            assertTrue(personalBoard.containsLeaderCardRequirements(leaderCard.getLeaderCardRequirements()), "Error: was expecting true, but received false instead");
        }

        @ParameterizedTest
        @EnumSource(LeaderCardAbility.class)
        void testContainsLeaderCardRequirementsWhenFalse(LeaderCardAbility leaderCardAbility) {
            LeaderCard leaderCard = getLeaderCardWithAbility(leaderCardAbility);
            //assertTrue because the board surely does not contain all the requirements
            assertFalse(personalBoard.containsLeaderCardRequirements(leaderCard.getLeaderCardRequirements()), "Error: was expecting false, but received true instead");
        }

    }

    @Nested
    class TestDevelopmentCardFeautures{
        DevelopmentCardsBoard developmentCardsBoard;
        List<DevelopmentCardSlot> developmentCardSlots;

        @BeforeEach
        void setUp() {
            developmentCardsBoard = new DevelopmentCardsBoard();
            developmentCardSlots = new ArrayList<>();
            developmentCardSlots.add(new DevelopmentCardSlot());
            developmentCardSlots.get(0).placeCard(developmentCardsBoard.pick(Level.LEVEL1, Color.YELLOW));
            developmentCardSlots.add(new DevelopmentCardSlot());
            developmentCardSlots.get(1).placeCard(developmentCardsBoard.pick(Level.LEVEL1, Color.BLUE));
            developmentCardSlots.get(1).placeCard(developmentCardsBoard.pick(Level.LEVEL2, Color.GREEN));
            developmentCardSlots.get(1).placeCard(developmentCardsBoard.pick(Level.LEVEL3, Color.PURPLE));
            developmentCardSlots.add(new DevelopmentCardSlot());
            developmentCardSlots.get(2).placeCard(developmentCardsBoard.pick(Level.LEVEL1, Color.YELLOW));

            //Adds the DevelopmentCards to the personalBoard
            IntStream.range(0, 2)
                    .forEach(
                            position -> developmentCardSlots.get(position).getDevelopmentCards()
                                            .stream()
                                            .sorted(Comparator.comparing(DevelopmentCard::getLevel))
                                            .forEach(
                                                    developmentCard -> personalBoard.placeDevelopmentCard(developmentCard, position)
                                            )
                    );
        }

        @Test
        void testPlaceAndGetDevelopmentCardSlots() {
            List<DevelopmentCardSlot> containedDevelopmentCards = personalBoard.getDevelopmentCardSlots();
            boolean containsAllDevelopmentCards = containedDevelopmentCards.equals(developmentCardSlots);
            assertEquals(developmentCardSlots, containedDevelopmentCards, "Error: the personalBoard does not contain the passed DevelopmentCards");
        }

        @Test
        void testGetDevelopmentCardsCount() {
            int expectedCount = developmentCardSlots.stream().mapToInt(
                    developmentCardSlot -> developmentCardSlot.getCardsNumber()
            ).sum();
            int actualCount = personalBoard.getDevelopmentCardsCount();
            assertEquals(expectedCount, actualCount, "Error: was expecting a count of " + expectedCount + " development cards, but received " + actualCount);
        }
    }

    @Nested
    class TestResourceFeautures{
        StoreLeaderCard leaderCard1;
        StoreLeaderCard leaderCard2;
        Map<Resource, Integer> strongboxResources;
        Map<Resource, Integer> depotsResources;
        Map<Resource, Integer> leaderCard1Resources;
        Map<Resource, Integer> leaderCard2Resources;

        @BeforeEach
        void setUp() {
            leaderCard1 = (StoreLeaderCard) getLeaderCardWithAbility(LeaderCardAbility.STORE);
            leaderCard2 = (StoreLeaderCard) getLeaderCardWithAbility(LeaderCardAbility.STORE);

            //Create the needed Resources
            strongboxResources = new HashMap<>();
            depotsResources = new HashMap<>();
            leaderCard1Resources = new HashMap<>();
            leaderCard2Resources = new HashMap<>();
            depotsResources.put(Resource.COIN, 3);
            depotsResources.put(Resource.SHIELD, 2);
            strongboxResources.put(Resource.SERVANT, 5);
            strongboxResources.put(Resource.STONE, 1);
            leaderCard1Resources.put(leaderCard1.getStoredType(), 2);
            leaderCard2Resources.put(leaderCard2.getStoredType(), 2);

            //add them to each storage component of the personalBoard
            personalBoard.storeInDepots(depotsResources);
            personalBoard.storeInStrongbox(strongboxResources);
            leaderCard1.storeResources(leaderCard1Resources);
            leaderCard2.storeResources(leaderCard2Resources);
        }

        @Test
        @DisplayName("Test getResourceCount")
        void getResourceCountTest() {

            int expectedCount = 15;
            int actualCount = personalBoard.getResourceCount();
            assertEquals(expectedCount, actualCount, "Error: was expecting a count of " + expectedCount + " development cards, but received " + actualCount);

        }

        @Test
        @DisplayName("Test storeResources and getResources")
        void StoreAndGetResourcesTest() {
            //Check returned resources
            Map<Resource, Integer> containedResources = personalBoard.getResources();
            Map<Resource, Integer> expectedResources = new HashMap<>(strongboxResources);
            depotsResources.forEach(
                    (key, value) -> expectedResources.merge( key, value, (v1, v2) -> v1+v2)
            );
            leaderCard1Resources.forEach(
                    (key, value) -> expectedResources.merge( key, value, (v1, v2) -> v1+v2)
            );
            leaderCard2Resources.forEach(
                    (key, value) -> expectedResources.merge( key, value, (v1, v2) -> v1+v2)
            );

            assertEquals(expectedResources, containedResources, "Error: the personal board does not contain the passed resources(should've been: " + expectedResources + " but received " + containedResources + ")");

        }

        @Test
        @DisplayName("Test containsResources")
        void containsResourcesTest() {
            Map<Resource, Integer> allResources = new HashMap<>(strongboxResources);
            depotsResources.forEach(
                    (key, value) -> allResources.merge( key, value, (v1, v2) -> v1+v2)
            );
            leaderCard1Resources.forEach(
                    (key, value) -> allResources.merge( key, value, (v1, v2) -> v1+v2)
            );
            leaderCard2Resources.forEach(
                    (key, value) -> allResources.merge( key, value, (v1, v2) -> v1+v2)
            );

            assertTrue(personalBoard.containsResources(depotsResources), "Error: the requested resources are contained in the depots, but the personal board returned false ");
            assertTrue(personalBoard.containsResources(strongboxResources), "Error: the requested resources are contained in the strongbox, but the personal board returned false ");
            assertTrue(personalBoard.containsResources(leaderCard1Resources), "Error: the requested resources are contained in the first leader card, but the personal board returned false ");
            assertTrue(personalBoard.containsResources(leaderCard2Resources), "Error: the requested resources are contained in the second leader card, but the personal board returned false ");
            assertTrue(personalBoard.containsResources(allResources), "Error: the requested resources are contained across different storages, but the personal board returned false ");
        }

        @Test
        @DisplayName("Test discardFromDepots and discardFromStrongbox")
        void discardResourcesTest() {
            personalBoard.discardFromDepots(depotsResources);
            personalBoard.discardFromStrongbox(strongboxResources);

            Map<Resource, Integer> allResources = new HashMap<>(leaderCard1Resources);
            leaderCard2Resources.forEach(
                    (key, value) -> allResources.merge( key, value, (v1, v2) -> v1+v2)
            );

            boolean successfulDiscard = allResources.equals(personalBoard.getResources());

            assertTrue(successfulDiscard, "Error: the personal board did not properly discard resources");
        }
    }

    @Test
    @DisplayName("Test getPlayer")
    void getPlayerTest() {
        assertEquals(player, personalBoard.getPlayer(), "Error: personal board did not return the right player, was expecting " + player.getNickname() + " but instead received " + personalBoard.getPlayer().getNickname());
    }

    @Test
    @DisplayName("Test getVictoryPoints")
    void getVictoryPointsTest() {
        StoreLeaderCard leaderCard1 = (StoreLeaderCard) getLeaderCardWithAbility(LeaderCardAbility.STORE);
        StoreLeaderCard leaderCard2 = (StoreLeaderCard) getLeaderCardWithAbility(LeaderCardAbility.STORE);

        //Create and store resources in the personalboard
        Map<Resource, Integer> strongboxResources = new HashMap<>();
        Map<Resource, Integer> depotsResources = new HashMap<>();
        Map<Resource, Integer> leaderCard1Resources = new HashMap<>();
        Map<Resource, Integer> leaderCard2Resources = new HashMap<>();
        depotsResources.put(Resource.COIN, 3);
        depotsResources.put(Resource.SHIELD, 2);
        strongboxResources.put(Resource.SERVANT, 5);
        strongboxResources.put(Resource.STONE, 1);
        leaderCard1Resources.put(leaderCard1.getStoredType(), 2);
        leaderCard2Resources.put(leaderCard2.getStoredType(), 2);
        personalBoard.storeInDepots(depotsResources);
        personalBoard.storeInStrongbox(strongboxResources);
        leaderCard1.storeResources(leaderCard1Resources);
        leaderCard2.storeResources(leaderCard2Resources);

        //Activate leader cards
        leaderCard1.activate();
        leaderCard2.activate();

        //Add development cards to the personalBoard
        DevelopmentCardsBoard developmentCardsBoard = new DevelopmentCardsBoard();
        List<DevelopmentCardSlot> developmentCardSlots = new ArrayList<>();
        developmentCardSlots.add(new DevelopmentCardSlot());
        developmentCardSlots.get(0).placeCard(developmentCardsBoard.pick(Level.LEVEL1, Color.YELLOW));
        developmentCardSlots.add(new DevelopmentCardSlot());
        developmentCardSlots.get(1).placeCard(developmentCardsBoard.pick(Level.LEVEL1, Color.BLUE));
        developmentCardSlots.get(1).placeCard(developmentCardsBoard.pick(Level.LEVEL2, Color.GREEN));
        developmentCardSlots.get(1).placeCard(developmentCardsBoard.pick(Level.LEVEL3, Color.PURPLE));
        developmentCardSlots.add(new DevelopmentCardSlot());
        developmentCardSlots.get(2).placeCard(developmentCardsBoard.pick(Level.LEVEL1, Color.YELLOW));
        IntStream.range(0, 2)
                .forEach(
                        position -> developmentCardSlots.forEach(
                                developmentCardSlot -> developmentCardSlot.getDevelopmentCards()
                                        .stream()
                                        .sorted(Comparator.comparing(DevelopmentCard::getLevel))
                                        .forEach(
                                                developmentCard -> personalBoard.placeDevelopmentCard(developmentCard, position)
                                        )
                        )
                );

        //Move on the faith track
        personalBoard.getFaithTrack().moveMarker(24);

        int expectedVictoryPoints = leaderCard1.getVictoryPoints()
                + leaderCard2.getVictoryPoints()
                + 3
                + personalBoard.getFaithTrack().getVictoryPoints()
                + developmentCardSlots.stream()
                .mapToInt(
                        developmentCardSlot -> developmentCardSlot.getVictoryPoints()
                ).sum();

        int actualVictoryPoints = personalBoard.getVictoryPoints();

        assertEquals(expectedVictoryPoints, actualVictoryPoints, "Error: personal board returned a wrong number of victory points, was expecting " + expectedVictoryPoints + " but instead received " + actualVictoryPoints);
    }

    /**
     * This methos returns a LeaderCard with a specified LeaderCardAbility
     * @param leaderCardAbility the ability that the returned LeaderCard should have
     * @return a LeaderCard of the specified LeaderCardAbility
     */
    private LeaderCard getLeaderCardWithAbility (LeaderCardAbility leaderCardAbility){
        LeaderCardsDeck leaderCardsDeck = new LeaderCardsDeck();
        Optional<LeaderCard> leaderCard = leaderCardsDeck.popFour().stream().filter(
                leaderCard1 -> leaderCard1.getAbility().equals(leaderCardAbility)
        ).findFirst();
        if(leaderCard.isPresent())
            return leaderCard.get();
        return getLeaderCardWithAbility(leaderCardAbility);
    }

    @Nested
    @DisplayName("getLeaderCards method tests")
    class GetLeaderCardsTests {
        List<LeaderCard> leaderCards;

        @BeforeEach
        void init() {
            leaderCards = personalBoard.getLeaderCards();
        }

        @Test
        @DisplayName("getLeaderCards safety test")
        void getLeaderCardsSafetyTest() {
            assertNotSame(personalBoard.getLeaderCards(), personalBoard.getLeaderCards());
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(leaderCards);
        }

        @Test
        @DisplayName("Two calls on the same state return equal lists")
        void coherentReturnsTest() {
            assertEquals(personalBoard.getLeaderCards(), personalBoard.getLeaderCards());
        }

        @Test
        @DisplayName("Returns correctly sized list")
        void sizeTest() {
            assertTrue(personalBoard.getLeaderCards().size()<=2 && personalBoard.getLeaderCards().size()>=0);
        }
    }

    @Nested
    @DisplayName("getLeaderCards method tests")
    class GetPlayerTests {
        Player player;

        @BeforeEach
        void init() {
            player = personalBoard.getPlayer();
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(player);
        }

        @Test
        @DisplayName("Two calls on the same state return equal lists")
        void coherentReturnsTest() {
            assertEquals(personalBoard.getPlayer(), personalBoard.getPlayer());
        }
    }

    @Nested
    @DisplayName("getDevelopmentCardSlots method tests")
    class GetDevelopmentCardSlots {
        List<DevelopmentCardSlot> developmentCardSlots;

        @BeforeEach
        void init() {
            developmentCardSlots = personalBoard.getDevelopmentCardSlots();
        }

        @Test
        @DisplayName("getDevelopmentCardSlots safety test")
        void getDevelopmentCardSlotsSafetyTest() {
            assertNotSame(personalBoard.getDevelopmentCardSlots(), personalBoard.getDevelopmentCardSlots());
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(developmentCardSlots);
        }

        @Test
        @DisplayName("Two calls on the same state return equal lists")
        void coherentReturnsTest() {
            assertEquals(personalBoard.getDevelopmentCardSlots(), personalBoard.getDevelopmentCardSlots());
        }

        @Test
        @DisplayName("Returns correctly sized list")
        void sizeTest() {
            assertEquals(3, personalBoard.getDevelopmentCardSlots().size());
        }
    }

    @Nested
    @DisplayName("getFaithTrack method tests")
    class getFaithTrackTests {
        FaithTrack faithTrack;

        @BeforeEach
        void init() {
            faithTrack = personalBoard.getFaithTrack();
        }

        @Test
        @DisplayName("Returns a non-null Object")
        void notNullTest() {
            assertNotNull(faithTrack);
        }

        @Test
        @DisplayName("Two calls on the same state return equal lists")
        void coherentReturnsTest() {
            assertEquals(personalBoard.getFaithTrack(), personalBoard.getFaithTrack());
        }
    }
}