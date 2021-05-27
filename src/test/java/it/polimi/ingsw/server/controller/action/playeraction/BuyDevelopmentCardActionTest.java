package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BuyDevelopmentCardActionTest {
    BuyDevelopmentCardAction buyDevelopmentCardAction;
    Game game;
    String nick1;
    String nick2;
    Map<Resource, Integer> cardCost;
    Server server;

    @BeforeAll
    static void deleteActions(){
        deleteDir("src/main/resources/games");
        new File("src/main/resources/games").mkdirs();
    }

    public void init(Integer gameId){
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
        game.getPlayer(nick1).startTurn();
        game.getPlayer(nick1).addAction(ExecutedActions.STORED_TEMP_MARBLES_ACTION);
        cardCost = game.getDevelopmentCardsBoard().peekCard(Level.LEVEL1, Color.GREEN).getCost();
        buyDevelopmentCardAction = new BuyDevelopmentCardAction(Level.LEVEL1, Color.GREEN, 1, null, cardCost);
        buyDevelopmentCardAction.setNickname(nick1);
        buyDevelopmentCardAction.setGame(game);
        game.getPlayer(nick1).getPersonalBoard().getStrongbox().storeResources(cardCost);
        game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().storeResources(cardCost);
    }

    @Test
    @DisplayName("Action has been executed correctly")
    void executeTest() {
        init(1);
        assertNull(buyDevelopmentCardAction.execute());
    }

    @Nested
    @DisplayName("Tests for runChecks method")
    class runChecksTest {

        @Test
        @DisplayName("All checks are passed")
        void allChecksPassedTest() {
            init(2);
            try {
                buyDevelopmentCardAction.runChecks();
            } catch (InvalidActionException e) {
                e.printStackTrace();
                assertTrue(false);
            }
        } //todo: il controllo delle risorse sufficienti non va , il controllo per poter posizionare la carta in un certo slot non va

        @Test
        @DisplayName("Player that try to do an action not during his turn is rejected")
        void wrongTurnTest() throws Exception {
            init(8);
            game.getPlayer(nick1).finishTurn();
            game.getPlayer(nick2).startTurn();
            try {
                buyDevelopmentCardAction.runChecks();
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
            game.getPlayer(nick1).addAction(ExecutedActions.DISCARDED_LEADER_CARD_ACTION);
            try {
                buyDevelopmentCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot buy a Development Card at this time")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Cards of level and color specified are not available, so the action is rejected")
        void noMoreCardsAvailable() {
            init(4);
            game.getDevelopmentCardsBoard().discardTwo(Color.GREEN);
            game.getDevelopmentCardsBoard().discardTwo(Color.GREEN);
            try {
                buyDevelopmentCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("GREEN LEVEL1Development Cards are not " +
                        "available on the board anymore.")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        void passedResourceDontMatchCostTest() {
            init(5);
            BuyDevelopmentCardAction buyDevelopmentCardAction1 = new BuyDevelopmentCardAction(Level.LEVEL1, Color.GREEN, 1, null, Map.of(Resource.COIN, 1));
            buyDevelopmentCardAction1.setNickname(nick1);
            buyDevelopmentCardAction1.setGame(game);
            try {
                buyDevelopmentCardAction1.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You did not pass the correct amount of resources to" +
                        " purchase the selected Development Card")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("The player doesn't have enough resources, so the action is rejected")
        void notEnoughResourcesTest() {
            init(6);
            game.getPlayer(nick1).getPersonalBoard().getStrongbox().discardResources(cardCost);
            game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().discardResources(cardCost);
            try {
                buyDevelopmentCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("Your storages do not have enough resources.")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }//todo: controllare anche metodo discardResources in Strongbox e WarehouseDepots

        @Test
        void illegalCardPositionTest() {
            init(7);
            cardCost = game.getDevelopmentCardsBoard().peekCard(Level.LEVEL2, Color.GREEN).getCost();
            BuyDevelopmentCardAction buyDevelopmentCardAction1 = new BuyDevelopmentCardAction(Level.LEVEL2, Color.GREEN, 1, null, cardCost);
            buyDevelopmentCardAction1.setNickname(nick1);
            buyDevelopmentCardAction1.setGame(game);
            game.getPlayer(nick1).getPersonalBoard().getStrongbox().storeResources(cardCost);
            game.getPlayer(nick1).getPersonalBoard().getWarehouseDepots().storeResources(cardCost);
            try {
                buyDevelopmentCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot place a LEVEL2 Development Card  in the Development Cards Slot number 1")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }
    }

    static void deleteDir(String pathToBeDeleted) {
        try {
            Files.walk(Path.of(pathToBeDeleted))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
