package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.client.utils.dumbobjects.DumbConvertLeaderCard;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ChangesHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.ConvertLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class ActivateLeaderCardActionTest {
    ActivateLeaderCardAction activateLeaderCardAction;
    DumbConvertLeaderCard dumbLeaderCard;
    Game game;
    String nick1;
    String nick2;
    ConvertLeaderCard leaderCard;
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
        dumbLeaderCard = new DumbConvertLeaderCard(new ConvertLeaderCard(1, new LeaderCardRequirements(Map.of(Color.GREEN, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.STONE, 1)), Resource.COIN));
        activateLeaderCardAction = new ActivateLeaderCardAction(dumbLeaderCard);
        activateLeaderCardAction.setNickname(nick1);
        activateLeaderCardAction.setGame(game);
        game.getPlayer(nick1).setTempLeaderCards(List.of(dumbLeaderCard.convert()));
        leaderCard = dumbLeaderCard.convert();
        game.getPlayer(nick1).getPersonalBoard().setLeaderCards(List.of(leaderCard));
        game.getPlayer(nick1).addAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
        game.getPlayer(nick1).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), 1), 1);
        game.getPlayer(nick1).getPersonalBoard().storeInStrongbox(Map.of(Resource.STONE, 1));
        game.getPlayer(nick1).startTurn();
    }

    @Test
    @DisplayName("Leader card has been activated")
    void executeTest() {
        activateLeaderCardAction.execute();
        assertAll(
                ()-> assertEquals(leaderCard, game.getPlayer(nick1).getPersonalBoard().getLeaderCards().get(0)),
                ()-> assertTrue(game.getPlayer(nick1).getPersonalBoard().getLeaderCards().get(0).isActive())
        );
    }

    @Nested
    @DisplayName("Tests for runChecks method")
    class runChecksTest{
        @Test
        @DisplayName("Player that try to do an action not during his turn is rejected")
        void wrongTurnTest(){
            game.getPlayer(nick1).finishTurn();
            game.getPlayer(nick2).startTurn();
            try{
                activateLeaderCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            }catch (InvalidActionException e) {
                if (!e.getMessage().equals("Not your turn")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("A not owned leader card hasn't been activated")
        void invalidLeaderCardTest() {

            DumbConvertLeaderCard dumbLeaderCard1 = new DumbConvertLeaderCard(new ConvertLeaderCard(1, new LeaderCardRequirements(Map.of(Color.BLUE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 0)), Map.of(Resource.STONE, 0)), Resource.COIN));
            ActivateLeaderCardAction activateLeaderCardAction1 = new ActivateLeaderCardAction(dumbLeaderCard1);
            activateLeaderCardAction1.setNickname(nick1);
            activateLeaderCardAction1.setGame(game);

            try{
                activateLeaderCardAction1.runChecks();
                throw new AssertionError("Exception was not thrown");
            }catch (InvalidActionException e) {
                if (!e.getMessage().equals("You do not have the selected LeaderCard")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Player hasn't enough resources and can't afford leader card")
        void notEnoughResourcesTest() {

            game.getPlayer(nick1).getPersonalBoard().discardFromStrongbox(Map.of(Resource.STONE, 1));

            try{
                activateLeaderCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            }catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot afford to activate this LeaderCard")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Player can't perform this action at this time")
        void cantPerformActionTest() {

            game.getPlayer(nick1).addAction(ExecutedActions.STORED_TEMP_MARBLES_ACTION);

            try{
                activateLeaderCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            }catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot activate a LeaderCard at this time")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("Leader card is already active")
        void alreadyActiveLeaderCard() {

            game.getPlayer(nick1).getPersonalBoard().activateLeaderCard(leaderCard);

            try{
                activateLeaderCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            }catch (InvalidActionException e) {
                if (!e.getMessage().equals("LeaderCard is already active")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }
    }

    @Test
    @DisplayName("Player doesn't own request development card")
    void noDevelopmentCardTest() {
        DumbConvertLeaderCard dumbLeaderCard1 = new DumbConvertLeaderCard(new ConvertLeaderCard(1, new LeaderCardRequirements(Map.of(Color.BLUE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), null), Resource.COIN));
        ActivateLeaderCardAction activateLeaderCardAction1 = new ActivateLeaderCardAction(dumbLeaderCard1);
        activateLeaderCardAction1.setNickname(nick1);
        activateLeaderCardAction1.setGame(game);
        game.getPlayer(nick1).setTempLeaderCards(List.of(dumbLeaderCard1.convert()));
        ConvertLeaderCard leaderCard1 = dumbLeaderCard1.convert();
        game.getPlayer(nick1).getPersonalBoard().setLeaderCards(List.of(leaderCard1));

        try{
            activateLeaderCardAction1.runChecks();
            throw new AssertionError("Exception was not thrown");
        }catch (InvalidActionException e) {
            if (!e.getMessage().equals("You cannot afford to activate this LeaderCard")){
                e.printStackTrace();
                throw new AssertionError("Wrong exception was thrown");
            }
        }
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        changesHandler.publishGameOutcome(game);
        sleep(100);
    }
}
