package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.client.utils.dumbobjects.DumbConvertLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbProduceLeaderCard;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.ConvertLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.leadercard.ProduceLeaderCard;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.ProductionCombo;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    private void init(Integer gameId){
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nick1 = "qwe";
        nick2 = "asd";
        try {
            game = new Game(server, gameId, List.of(nick1, nick2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        produceLeaderCard = new ProduceLeaderCard(1,
                new LeaderCardRequirements(Map.of(Color.GREEN, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.SERVANT, 1)),
                Resource.COIN, 1);//1 coin
        developmentCard = new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), 1);
        //1 stone
        productionCombo = new ProductionCombo();
        productionCombo.setDefaultSlotOutput(Map.of(Resource.STONE, 1));
        productionCombo.setLeaderCardProduction(Map.of(new DumbProduceLeaderCard(produceLeaderCard), Resource.COIN)
        );
        productionCombo.setDevelopmentCards(List.of(new DumbDevelopmentCard(developmentCard)));
        productionCombo.setDiscardedResourcesFromDepots(Map.of(Resource.COIN, 1));
        productionCombo.setDiscardedResourcesFromStrongbox(Map.of(Resource.STONE, 1));
        activateProductionAction = new ActivateProductionAction(productionCombo);
        activateProductionAction.setNickname(nick1);
        activateProductionAction.setGame(game);
        game.getPlayer(nick1).getPersonalBoard().setLeaderCards(List.of(produceLeaderCard));
        game.getPlayer(nick1).getPersonalBoard().activateLeaderCard(produceLeaderCard);
        game.getPlayer(nick1).getPersonalBoard().placeDevelopmentCard(developmentCard, 1);
        game.getPlayer(nick1).startTurn();
        game.getPlayer(nick1).addAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
        game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().storeResources(Map.of(Resource.COIN, 1));
        game.getPlayer(nick1).getPersonalBoard().getStrongbox().storeResources(Map.of(Resource.STONE, 1));
    }

    @Test
    void executeTest() {
        init(1);
        game.getPlayer(nick1).getPersonalBoard().storeInDepots(Map.of(Resource.SERVANT, 3));
        game.getPlayer(nick1).getPersonalBoard().storeInStrongbox(Map.of(Resource.SHIELD, 3));
        activateProductionAction.execute();
        assertAll(
                ()-> assertEquals(2, game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().getStoredResources().get(Resource.SERVANT)), //depotsCanContain(Map.of(Resource.SERVANT, 2))),
                ()->assertEquals(2, game.getPlayer(nick1).getPersonalBoard().getStrongbox().getStoredResources().get(Resource.SHIELD)),
                ()->assertEquals(2, game.getPlayer(nick1).getPersonalBoard().getStrongbox().getStoredResources().get(Resource.STONE)),
                ()->assertEquals(1, game.getPlayer(nick1).getPersonalBoard().getStrongbox().getStoredResources().get(Resource.COIN)) //strongboxContainsResources(Map.of(Resource.SHIELD, 2, Resource.STONE, 2, Resource.COIN, 1)))
        );
    }

    @Nested
    @DisplayName("Tests for runChecks method")
    class runChecksTest {

        @Test
        @DisplayName("Player that try to do an action not during his turn is rejected")
        void wrongTurnTest() throws Exception {
            init(2);
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
            init(3);
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
        @DisplayName("The action is not performed cause leader card production is null")
        void emptyProductionComboTest() {
            init(4);
            productionCombo.setLeaderCardProduction(null);
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
            init(5);
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
            init(6);
            game.getPlayer(nick1).setTempLeaderCards(List.of(new ProduceLeaderCard(1,
                    new LeaderCardRequirements(Map.of(Color.PURPLE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.SERVANT, 1)),
                    Resource.COIN, 1)));
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
        void noLeaderCardMatchingTest() {//todo: quando setto leaderCardProduction in productionCombo viene messo active di leader card a false, quindi le leader cards non possono matchare
            init(7);
            ProduceLeaderCard newLeaderCard = new ProduceLeaderCard(1,
                    new LeaderCardRequirements(Map.of(Color.PURPLE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.SERVANT, 1)),
                    Resource.COIN, 1);
            game.getPlayer(nick1).getPersonalBoard().activateLeaderCard(newLeaderCard);
            game.getPlayer(nick1).getPersonalBoard().setLeaderCards(List.of(newLeaderCard));
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
            init(9);
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
        @DisplayName("Selected discarded resources from strongbox and production cost don't match")
        void noMatchBetweenProductionCostAndSelectedDiscardedStrongboxTest() {
            init(10);
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
            init(11);
            game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().discardResources(Map.of(Resource.COIN, 1));
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
            init(12);
            game.getPlayer(nick1).getPersonalBoard().getStrongbox().discardResources(Map.of(Resource.STONE, 1));
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
            init(13);
            try {
                activateProductionAction.runChecks();
            } catch (InvalidActionException e) {
                e.printStackTrace();
                assertTrue(false);
            }
        }
    }
}
