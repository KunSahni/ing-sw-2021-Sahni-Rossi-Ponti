package it.polimi.ingsw.server.model.personalboardpackage;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.leadercard.LeaderCardsDeck;
import it.polimi.ingsw.server.model.leadercard.StoreLeaderCard;
import it.polimi.ingsw.server.model.personalboard.DevelopmentCardSlot;
import it.polimi.ingsw.server.model.personalboard.FaithTrack;
import it.polimi.ingsw.server.model.personalboard.PersonalBoard;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PersonalBoardTest {
    PersonalBoard personalBoard;
    Player player;
    Game game;
    ChangesHandler changesHandler;

    @BeforeEach
    void setUp() throws IOException {
        changesHandler = new ChangesHandler(1);
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Mario");
        nicknames.add("Luigi");
        game = new Game(new Server(),1, nicknames);
        player = game.getPlayer("Mario");
        personalBoard = game.getPlayer("Mario").getPersonalBoard();
    }

    @AfterEach
    void tearDown() {
        changesHandler.publishGameOutcome(game);
    }

    @Nested
    class LeaderCardFeaturesTest {
        List<LeaderCard> leaderCards;

        @BeforeEach
        void setUp() {
            player.setTempLeaderCards(game.getLeaderCardsDeck().popFour());
            leaderCards = player.getTempLeaderCards();
            leaderCards.remove(3);
            leaderCards.remove(2);
            personalBoard.setLeaderCards(leaderCards);
        }

        @Test
        @DisplayName("Test setLeaderCards and getLeaderCards")
        void setLeaderCardsAndGetLeaderCardsTest() {
            List<LeaderCard> actualLeaderCards = personalBoard.getLeaderCards();
            assertEquals(leaderCards, actualLeaderCards, "Error: was expecting " + leaderCards + ", but received " + actualLeaderCards);
        }

        @Test
        @DisplayName("activateLeaderCard method test")
        void activateLeaderCardTest(){
            assertFalse(personalBoard.getLeaderCards().get(0).isActive(), "Error: leader card should not be active");
            personalBoard.activateLeaderCard(personalBoard.getLeaderCards().get(0));
            assertTrue(personalBoard.getLeaderCards().get(0).isActive(), "Error: leader card should be active");
        }

        @Test
        @DisplayName("Test discardLeaderCard")
        void discardLeaderCardTest() {
            LeaderCard removableCard = leaderCards.remove(1);
            personalBoard.discardLeaderCard(removableCard);
            List<LeaderCard> actualLeaderCards = personalBoard.getLeaderCards();
            assertEquals(leaderCards, actualLeaderCards, "Error: was expecting " + leaderCards + ", but received " + actualLeaderCards);
        }


        //todo: double check test
        @ParameterizedTest
        @EnumSource(LeaderCardAbility.class)
        @DisplayName("Test containsLeaderCardRequirements when the PersonalBoard does contain such requirements")
        void containsLeaderCardRequirementsWhenTrueTest(LeaderCardAbility leaderCardAbility) throws FileNotFoundException {
            DevelopmentCardsBoard developmentCardsBoard = game.getDevelopmentCardsBoard();
            LeaderCard leaderCard = getLeaderCardWithAbility(leaderCardAbility);

            //Sets the board so that it will surely contain all the requirements needed by the above created leaderCard
            switch (leaderCardAbility){
                case STORE -> personalBoard.storeInStrongbox(leaderCard.getLeaderCardRequirements().getRequiredResources());
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
                //DISCOUNT and CONVERT LeaderCards have similar requirements, so this places all the required DevelopmentCards on the on the personalBoard
                default -> leaderCard.getLeaderCardRequirements()
                        .getRequiredDevelopmentCards()
                        .forEach((key, value) -> IntStream.range(1, value.getQuantity()+1)
                                .forEach(
                                        i -> personalBoard.placeDevelopmentCard(
                                                developmentCardsBoard.pick(
                                                        value.getLevel(), key
                                                ),
                                                i
                                        )
                                ));
            }

            //assertTrue because the board surely contains all the requirements
            assertTrue(personalBoard.containsLeaderCardRequirements(leaderCard.getLeaderCardRequirements()), "Error: was expecting true, but received false instead");
        }

        @ParameterizedTest
        @EnumSource(LeaderCardAbility.class)
        @DisplayName("Test containsLeaderCardRequirements when the PersonalBoard does not contain such requirements")
        void containsLeaderCardRequirementsWhenFalseTest(LeaderCardAbility leaderCardAbility) throws FileNotFoundException {
            LeaderCard leaderCard = getLeaderCardWithAbility(leaderCardAbility);
            //assertTrue because the board surely does not contain all the requirements
            assertFalse(personalBoard.containsLeaderCardRequirements(leaderCard.getLeaderCardRequirements()), "Error: was expecting false, but received true instead");
        }

    }

    @Nested
    class TestDevelopmentCardFeatures {
        DevelopmentCardsBoard developmentCardsBoard;
        List<DevelopmentCardSlot> developmentCardSlots;

        @BeforeEach
        void setUp() throws FileNotFoundException {
            developmentCardsBoard = changesHandler.readDevelopmentCardsBoard();
            developmentCardSlots = new ArrayList<>();
            developmentCardSlots.add(changesHandler.readDevelopmentCardSlot("Mario", 1));
            developmentCardSlots.get(0).placeCard(developmentCardsBoard.pick(Level.LEVEL1, Color.YELLOW));
            developmentCardSlots.add(changesHandler.readDevelopmentCardSlot("Mario", 2));
            developmentCardSlots.get(1).placeCard(developmentCardsBoard.pick(Level.LEVEL1, Color.BLUE));
            developmentCardSlots.get(1).placeCard(developmentCardsBoard.pick(Level.LEVEL2, Color.GREEN));
            developmentCardSlots.get(1).placeCard(developmentCardsBoard.pick(Level.LEVEL3, Color.PURPLE));
            developmentCardSlots.add(changesHandler.readDevelopmentCardSlot("Mario", 3));
            developmentCardSlots.get(2).placeCard(developmentCardsBoard.pick(Level.LEVEL1, Color.YELLOW));

            //Adds the DevelopmentCards to the personalBoard
            IntStream.range(1, 4)
                    .forEach(
                            position -> developmentCardSlots.get(position-1).getDevelopmentCards()
                                            .stream()
                                            .sorted(Comparator.comparing(DevelopmentCard::getLevel))
                                            .forEach(
                                                    developmentCard -> personalBoard.placeDevelopmentCard(developmentCard, position)
                                            )
                    );
        }

        @Test
        @DisplayName("Test placeDevelopmentCard and getDevelopmentCardSlots")
        void placeAndGetDevelopmentCardSlotsTest() {
            List<DevelopmentCardSlot> containedDevelopmentCardSlots = personalBoard.getDevelopmentCardSlots();

            assertAll(
                    () -> assertEquals(developmentCardSlots.get(0).getDevelopmentCards(), containedDevelopmentCardSlots.get(0).getDevelopmentCards(), "Error: the first DevelopmentCardSlot does not contain the right cards"),
                    () -> assertEquals(developmentCardSlots.get(1).getDevelopmentCards(), containedDevelopmentCardSlots.get(1).getDevelopmentCards(), "Error: the second DevelopmentCardSlot does not contain the right cards"),
                    () -> assertEquals(developmentCardSlots.get(2).getDevelopmentCards(), containedDevelopmentCardSlots.get(2).getDevelopmentCards(), "Error: the third DevelopmentCardSlot does not contain the right cards")
            );
        }

        @Test
        @DisplayName("Test getDevelopmentCardsCount")
        void getDevelopmentCardsCountTest() {
            int expectedCount = developmentCardSlots.stream().mapToInt(
                    DevelopmentCardSlot::getCardsNumber
            ).sum();
            int actualCount = personalBoard.getDevelopmentCardsCount();
            assertEquals(expectedCount, actualCount, "Error: was expecting a count of " + expectedCount + " development cards, but received " + actualCount);
        }
    }

    @Nested
    class TestResourceFeatures {
        StoreLeaderCard leaderCard1;
        StoreLeaderCard leaderCard2;
        Map<Resource, Integer> strongboxResources;
        Map<Resource, Integer> depotsResources;
        Map<Resource, Integer> leaderCard1Resources;
        Map<Resource, Integer> leaderCard2Resources;

        @BeforeEach
        void setUp() throws FileNotFoundException {
            List<LeaderCard> leaderCards = new ArrayList<>();
            leaderCards.add((StoreLeaderCard) getLeaderCardWithAbility(LeaderCardAbility.STORE));
            leaderCards.add((StoreLeaderCard) getLeaderCardWithAbility(LeaderCardAbility.STORE));

            personalBoard.setLeaderCards(leaderCards);
            personalBoard.activateLeaderCard(((StoreLeaderCard)personalBoard.getLeaderCards().get(0)));
            personalBoard.activateLeaderCard(((StoreLeaderCard)personalBoard.getLeaderCards().get(1)));

            //Create the needed Resources
            strongboxResources = new HashMap<>();
            depotsResources = new HashMap<>();
            strongboxResources.put(Resource.SERVANT, 2);
            strongboxResources.put(Resource.STONE, 1);
            //Leader cards resources are managed together with depots
            depotsResources.put(((StoreLeaderCard)personalBoard.getLeaderCards().get(0)).getStoredType(), 4);
            depotsResources.put(((StoreLeaderCard)personalBoard.getLeaderCards().get(1)).getStoredType(), 3);

            //add them to each storage component of the personalBoard
            personalBoard.storeInDepots(depotsResources);
            personalBoard.storeInStrongbox(strongboxResources);
        }//todo: non fa correttamente la separazione tra risorse da salvare in leadercard e depots

        @Test
        @DisplayName("Test getResourceCount")
        void getResourceCountTest() {
            int expectedCount = 13;
            int actualCount = personalBoard.getResourceCount();
            assertEquals(expectedCount, actualCount, "Error: was expecting a count of " + expectedCount + " development cards, but received " + actualCount);

        }

        @Test
        @DisplayName("Test storeResources and getResources")
        void storeAndGetResourcesTest() {
            //Check actual resources
            Map<Resource, Integer> actualLeaderCardResources = new HashMap<>(
                    personalBoard
                            .getLeaderCards()
                            .stream()
                            .filter(
                                    leaderCard -> leaderCard.getAbility().equals(LeaderCardAbility.STORE))
                            .map(
                                    leaderCard -> ((StoreLeaderCard) leaderCard).getStoredResources())
                            .flatMap(
                                    map -> map
                                            .entrySet()
                                            .stream())
                            .collect(
                                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum))
            );

            Map<Resource, Integer> actualResources = new HashMap<>(personalBoard.getStrongbox().getStoredResources());
            personalBoard.getWarehouseDepots().getStoredResources().forEach(
                    (key, value) -> actualResources.merge(key, value, Integer::sum)
            );
            actualLeaderCardResources.forEach(
                    (key, value) -> actualResources.merge(key, value, Integer::sum)
            );

            //Calculate expected resources
            Map<Resource, Integer> expectedResources = new HashMap<>(strongboxResources);
            depotsResources.forEach(
                    (key, value) -> expectedResources.merge( key, value, Integer::sum)
            );

            assertEquals(expectedResources, actualResources, "Error: the personal board does not contain the passed resources(should've been: " + expectedResources + " but received " + actualResources + ")");

        }

        @Test
        @DisplayName("depotsCanContain method test")
        void depotsCanContainTest(){
            Map<Resource, Integer> trialMap1 = new HashMap<>();
            trialMap1.put(Resource.COIN, 1);
            Map<Resource, Integer> trialMap2 = new HashMap<>();
            trialMap2.put(Resource.STONE, 4);
            Map<Resource, Integer> trialMap3 = new HashMap<>();
            trialMap3.put(Resource.SERVANT, 1);

            assertAll(
                    () -> assertTrue(personalBoard.depotsCanContain(trialMap1), "Error: method returned true on a map which can't be contained in the depots"),
                    () -> assertFalse(personalBoard.depotsCanContain(trialMap2), "Error: method returned true on a map which can't be contained in the depots"),
                    () -> assertTrue(personalBoard.depotsCanContain(trialMap3), "Error: method returned false on a map which can be contained in the depots")
            );
        }

        @Test
        @DisplayName("Test depotsContainResources")
        void depotsContainResourcesTest() {
            depotsResources.entrySet().forEach(
                    entry -> entry.setValue(entry.getValue()-2)
            );
            assertAll(
                    () -> assertTrue(personalBoard.depotsContainResources(depotsResources), "Error: method returned false, but the resources should be contained in the depots "),
                    () -> assertFalse(personalBoard.depotsContainResources(strongboxResources), "Error: method returned true, but the resources shouldn't be contained in the depots")
            );
        }

        @Test
        @DisplayName("Test strongboxContainsResources")
        void strongboxContainsResourcesTest() {
            assertAll(
                    () -> assertTrue(personalBoard.strongboxContainsResources(strongboxResources), "Error: method returned false, but the resources should be contained in the strongbox"),
                    () -> assertFalse(personalBoard.strongboxContainsResources(depotsResources), "Error: method returned true, but the resources shouldn't be contained in the strongbox")
            );
        }

        @Test
        @DisplayName("Test containsResources")
        void containsResourcesTest() {
            Map<Resource, Integer> allResources = new HashMap<>(strongboxResources);
            depotsResources.forEach(
                    (key, value) -> allResources.merge( key, value, Integer::sum)
            );

            assertTrue(personalBoard.containsResources(depotsResources), "Error: the requested resources are contained in the depots, but the personal board returned false ");
            assertTrue(personalBoard.containsResources(strongboxResources), "Error: the requested resources are contained in the strongbox, but the personal board returned false ");
            assertTrue(personalBoard.containsResources(allResources), "Error: the requested resources are contained across different storages, but the personal board returned false ");
        }

        @Test
        @DisplayName("Test discardFromDepots and discardFromStrongbox")
        void discardResourcesTest() {
            personalBoard.discardFromDepots(depotsResources);
            personalBoard.discardFromStrongbox(strongboxResources);

            Map<Resource, Integer> actualResources = new HashMap<>(personalBoard.getStrongbox().getStoredResources());
            personalBoard.getWarehouseDepots().getStoredResources().forEach(
                    (key, value) -> actualResources.merge(key, value, Integer::sum)
            );

            assertEquals(0, actualResources.size(), "Error: the personal board did not properly discard resources");
        }
    }

    @Test
    @DisplayName("Test getVictoryPoints")
    void getVictoryPointsTest() throws FileNotFoundException {
        List<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(getLeaderCardWithAbility(LeaderCardAbility.STORE));
        leaderCards.add(getLeaderCardWithAbility(LeaderCardAbility.CONVERT));

        personalBoard.setLeaderCards(leaderCards);

        //Activate leader cards
        personalBoard.activateLeaderCard(personalBoard.getLeaderCards().get(0));
        personalBoard.activateLeaderCard(personalBoard.getLeaderCards().get(1));

        //Create and store resources in the personal board
        Map<Resource, Integer> strongboxResources = new HashMap<>();
        Map<Resource, Integer> depotsResources = new HashMap<>();
        Arrays.stream(Resource.values()).forEach(
                resource -> depotsResources.put(resource, 1)
        );
        strongboxResources.put(Resource.SERVANT, 5);
        strongboxResources.put(Resource.STONE, 1);
        personalBoard.storeInDepots(depotsResources);
        personalBoard.storeInStrongbox(strongboxResources);

        //Add development cards to the personalBoard
        DevelopmentCardsBoard developmentCardsBoard = game.getDevelopmentCardsBoard();
        personalBoard.placeDevelopmentCard(developmentCardsBoard.pick(Level.LEVEL1, Color.YELLOW),1);
        personalBoard.placeDevelopmentCard(developmentCardsBoard.pick(Level.LEVEL1, Color.BLUE),2);
        personalBoard.placeDevelopmentCard(developmentCardsBoard.pick(Level.LEVEL2, Color.GREEN),2);
        personalBoard.placeDevelopmentCard(developmentCardsBoard.pick(Level.LEVEL3, Color.PURPLE),2);
        personalBoard.placeDevelopmentCard(developmentCardsBoard.pick(Level.LEVEL1, Color.YELLOW),3);
        personalBoard.placeDevelopmentCard(developmentCardsBoard.pick(Level.LEVEL2, Color.YELLOW),3);

        //Move on the faith track
        IntStream.range(1, 25).forEach(
                i -> {
                    personalBoard.getFaithTrack().moveMarker();
                    if(i%8==0)
                        personalBoard.getFaithTrack().flipPopesFavor(i/8);
                }
        );

        int expectedVictoryPoints = personalBoard.getLeaderCards().get(0).getVictoryPoints()
                + personalBoard.getLeaderCards().get(1).getVictoryPoints()
                + 2
                + personalBoard.getFaithTrack().getVictoryPoints()
                + personalBoard
                .getDevelopmentCardSlots()
                .stream()
                .mapToInt(
                        DevelopmentCardSlot::getVictoryPoints
                ).sum();

        int actualVictoryPoints = personalBoard.getVictoryPoints();

        assertEquals(expectedVictoryPoints, actualVictoryPoints, "Error: personal board returned a wrong number of victory points, was expecting " + expectedVictoryPoints + " but instead received " + actualVictoryPoints);
    }

    /**
     * This method returns a LeaderCard with a specified LeaderCardAbility
     * @param leaderCardAbility the ability that the returned LeaderCard should have
     * @return a LeaderCard of the specified LeaderCardAbility
     */
    private LeaderCard getLeaderCardWithAbility (LeaderCardAbility leaderCardAbility) throws FileNotFoundException {
        LeaderCardsDeck leaderCardsDeck = game.getLeaderCardsDeck();
        Optional<LeaderCard> leaderCard = leaderCardsDeck.popFour().stream().filter(
                leaderCard1 -> leaderCard1.getAbility().equals(leaderCardAbility)
        ).findFirst();
        return leaderCard.orElseGet(() -> {
            try {
                return getLeaderCardWithAbility(leaderCardAbility);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        });
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
            assertTrue(personalBoard.getLeaderCards().size()<=2);
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
        @DisplayName("Two calls on the same method return equal objects")
        void coherentReturnsTest() {
            assertEquals(personalBoard.getFaithTrack(), personalBoard.getFaithTrack());
        }
    }
}