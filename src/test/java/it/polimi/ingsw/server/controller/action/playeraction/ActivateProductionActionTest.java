package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbProduceLeaderCard;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.leadercard.ProduceLeaderCard;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.ProductionCombo;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class ActivateProductionActionTest {
    ActivateProductionAction activateProductionAction;
    ProductionCombo productionCombo;
    Game game;
    String nick1;
    String nick2;
    ProduceLeaderCard produceLeaderCard;
    DevelopmentCard developmentCard;
    Server server;
    ChangesHandler changesHandler;

    @BeforeEach
    void setUp(){
        changesHandler = new ChangesHandler(1);
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nick1 = "qwe";
        nick2 = "asd";
        try {
            game = new Game(server, 1, List.of(nick1, nick2));
        } catch (IOException e) {
            e.printStackTrace();
        }

        produceLeaderCard = new ProduceLeaderCard(1,
                new LeaderCardRequirements(Map.of(Color.GREEN, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), null),
                Resource.COIN, 1);//-1 coin from strongbox

        developmentCard = new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.SHIELD, 1), Map.of(Resource.STONE, 1), 1);
        //-1 stone from depots +1 shield

        productionCombo = new ProductionCombo();

        productionCombo.setDefaultSlotOutput(Map.of(Resource.STONE, 1));//+1 stone -2 shield

        productionCombo.setLeaderCardProduction(Map.of(new DumbProduceLeaderCard(produceLeaderCard), Resource.SERVANT));//+1 servant

        productionCombo.setDevelopmentCards(List.of(new DumbDevelopmentCard(developmentCard)));

        productionCombo.setDiscardedResourcesFromDepots(Map.of(Resource.STONE, 1, Resource.SHIELD, 2));

        productionCombo.setDiscardedResourcesFromStrongbox(Map.of(Resource.COIN, 1));

        activateProductionAction = new ActivateProductionAction(productionCombo);

        activateProductionAction.setNickname(nick1);

        activateProductionAction.setGame(game);

        game.getPlayer(nick1).getPersonalBoard().setLeaderCards(List.of(produceLeaderCard));

        game.getPlayer(nick1).getPersonalBoard().activateLeaderCard(produceLeaderCard);

        game.getPlayer(nick1).getPersonalBoard().placeDevelopmentCard(developmentCard, 1);

        game.getPlayer(nick1).startTurn();

        game.getPlayer(nick1).addAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);

        game.getPlayer(nick1).getPersonalBoard().storeInDepots(Map.of(Resource.STONE, 1, Resource.SHIELD, 2));

        game.getPlayer(nick1).getPersonalBoard().storeInStrongbox(Map.of(Resource.COIN, 1));

    }

    @Test
    void    executeTest() {
        activateProductionAction.execute();

        assertAll(
                ()-> assertTrue(game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().getStoredResources().isEmpty()),
                ()-> assertEquals(1, game.getPlayer(nick1).getPersonalBoard().getStrongbox().getStoredResources().get(Resource.STONE)),
                ()-> assertEquals(1, game.getPlayer(nick1).getPersonalBoard().getStrongbox().getStoredResources().get(Resource.SERVANT)),
                ()-> assertEquals(1, game.getPlayer(nick1).getPersonalBoard().getStrongbox().getStoredResources().get(Resource.SHIELD)),//depotsCanContain(Map.of(Resource.SERVANT, 2))),
                ()->assertNull(game.getPlayer(nick1).getPersonalBoard().getStrongbox().getStoredResources().get(Resource.COIN))
        );
    }

    @Nested
    @DisplayName("Tests for runChecks method")
    class runChecksTest {

        @Test
        @DisplayName("Player that try to do an action not during his turn is rejected")
        void wrongTurnTest(){
            game.getPlayer(nick1).finishTurn();
            game.getPlayer(nick2).startTurn();
            try {
                activateProductionAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("Not your turn")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Not allowed action is rejected")
        void notAllowedActionTest() {
            game.getPlayer(nick1).addAction(ExecutedActions.ACTIVATED_PRODUCTION_ACTION);
            try {
                activateProductionAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot activate a production at this time.")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("The action is not performed cause production combo is null")
        void emptyProductionComboTest() {
            productionCombo.setLeaderCardProduction(null);
            productionCombo.setDefaultSlotOutput(null);
            productionCombo.setDevelopmentCards(null);
            try {
                activateProductionAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You supplied an empty Production Combo")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("The attempt to produce more than one resource with a default slot activation is rejected")
        void moreThanOneDefaultSlotOutput() {
            productionCombo.setDefaultSlotOutput(Map.of(Resource.SHIELD, 1, Resource.STONE, 1));
            try {
                activateProductionAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot produce more than one resource with your default slot activation")){
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("A not owned development production is rejected")
        void noMatchingDevelopmentCardTest() {
            DevelopmentCard notOwnedDevelopmentCard = new DevelopmentCard(Color.BLUE, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.SHIELD, 1), Map.of(Resource.STONE, 1), 1);
            productionCombo.setDevelopmentCards(List.of(new DumbDevelopmentCard(notOwnedDevelopmentCard)));
            try {
                activateProductionAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("The development cards you have supplied do not " +
                        "match the ones available in your Development Slots.")){
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("A not owned production Leader Card is rejected")
        void noLeaderCardMatchingTest() {
            ProduceLeaderCard newLeaderCard = new ProduceLeaderCard(1,
                    new LeaderCardRequirements(Map.of(Color.PURPLE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.SERVANT, 1)),
                    Resource.COIN, 1);
            game.getPlayer(nick1).getPersonalBoard().setLeaderCards(List.of(newLeaderCard));
            game.getPlayer(nick1).getPersonalBoard().activateLeaderCard(newLeaderCard);
            try {
                activateProductionAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You have supplied Leader Cards that are not " +
                        "available on your personal board.")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Selected discarded resources from depots and production cost don't match")
        void noMatchBetweenProductionCostAndSelectedDiscardedDepotsTest() {
            productionCombo.setDiscardedResourcesFromDepots(Map.of(Resource.SHIELD, 1));

            try {
                activateProductionAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("The resources that you have selected to discard do " +
                        "not match production costs.")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Selected discarded resources and production cost don't match")
        void noMatchBetweenProductionCostAndSelectedDiscardedStrongboxTest() {
            productionCombo.setDiscardedResourcesFromStrongbox(Map.of(Resource.SERVANT, 1));

            try {
                activateProductionAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("The resources that you have selected to discard do " +
                        "not match production costs.")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Not enough resources are in depots, so the action is rejected")
        void notEnoughResourcesInDepotsTest() {
            game.getPlayer(nick1).getPersonalBoard().discardFromDepots(Map.of(Resource.SHIELD, 1));
            try {
                activateProductionAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You are trying to discard from your storages " +
                        "resources you do not have.")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Not enough resources are in strongbox, so the action is rejected")
        void notEnoughResourcesInStrongboxTest() {
            game.getPlayer(nick1).getPersonalBoard().discardFromStrongbox(Map.of(Resource.COIN, 1));
            try {
                activateProductionAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You are trying to discard from your storages " +
                        "resources you do not have.")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("All checks are passed")
        void allChecksPassedTest() {
            try {
                activateProductionAction.runChecks();
            } catch (InvalidActionException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        changesHandler.publishGameOutcome(game);
        sleep(100);
    }
}
