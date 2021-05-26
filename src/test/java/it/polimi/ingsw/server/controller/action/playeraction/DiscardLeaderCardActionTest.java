package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.client.utils.dumbobjects.DumbConvertLeaderCard;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.ConvertLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiscardLeaderCardActionTest {
    DiscardLeaderCardAction discardLeaderCardAction;
    Game game;
    String nick1;
    String nick2;
    ConvertLeaderCard convertLeaderCard;
    Integer faithMarkerPosition;
    Server server;

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
        game.getPlayer(nick1).addAction(ExecutedActions.TURN_ENDED_ACTION);
        convertLeaderCard = new ConvertLeaderCard(1, new LeaderCardRequirements(Map.of(Color.GREEN, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.COIN, 1)), Resource.COIN);
        discardLeaderCardAction = new DiscardLeaderCardAction(new DumbConvertLeaderCard(convertLeaderCard));
        discardLeaderCardAction.setNickname(nick1);
        discardLeaderCardAction.setGame(game);
        game.getPlayer(nick1).setTempLeaderCards(List.of(convertLeaderCard));
        game.getPlayer(nick1).getPersonalBoard().setLeaderCards(List.of(convertLeaderCard));
        faithMarkerPosition = game.getPlayer(nick1).getPersonalBoard().getFaithTrack().getFaithMarkerPosition();
    }

    @Test
    @DisplayName("Selected Leader Card is discarded correctly and the faith marker has been moved correctly")
    void executeTest() {
        init(1);
        discardLeaderCardAction.execute();
        assertEquals(faithMarkerPosition+1, game.getPlayer(nick1).getPersonalBoard().getFaithTrack().getFaithMarkerPosition());
    }

    @Nested
    @DisplayName("Tests for runChecks method")
    class runChecksTest {

        @Test
        @DisplayName("All checks are passed")
        void allChecksPassedTest() {
            init(2);
            try {
                discardLeaderCardAction.runChecks();
            } catch (InvalidActionException e) {
                e.printStackTrace();
                assertTrue(false);
            }
        } //todo: il controllo per vedere se si ha la leader card non va, la leader card viene scartata anche se è attiva

        @Test
        @DisplayName("Player that try to do an action not during his turn is rejected")
        void wrongTurnTest(){
            init(3);
            game.getPlayer(nick1).finishTurn();
            game.getPlayer(nick2).startTurn();
            try {
                discardLeaderCardAction.runChecks();
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
            init(4);
            game.getPlayer(nick1).addAction(ExecutedActions.STORED_TEMP_MARBLES_ACTION);
            try {
                discardLeaderCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot discard a LeaderCard at this time")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        void notOwnedLeaderCardTest() {
            init(5);
            game.getPlayer(nick1).getPersonalBoard().discardLeaderCard(convertLeaderCard);

            try {
                discardLeaderCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You do not have the selected LeaderCard in your hand")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        void discardAnActiveLeaderCardTest() {
            init(6);
            game.getPlayer(nick1).getPersonalBoard().activateLeaderCard(convertLeaderCard);

            try {
                discardLeaderCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("Cannot discard an active LeaderCard")) {
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }
    }
}
