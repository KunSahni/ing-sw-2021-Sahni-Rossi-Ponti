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

import static org.junit.jupiter.api.Assertions.*;

public class ActivateLeaderCardActionTest {
    ActivateLeaderCardAction activateLeaderCardAction;
    DumbConvertLeaderCard dumbLeaderCard;
    Game game;
    String nick1;
    String nick2;
    ConvertLeaderCard leaderCard;
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
        dumbLeaderCard = new DumbConvertLeaderCard(new ConvertLeaderCard(1, new LeaderCardRequirements(Map.of(Color.GREEN, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 0)), Map.of(Resource.STONE, 0)), Resource.COIN));
        activateLeaderCardAction = new ActivateLeaderCardAction(dumbLeaderCard);
        activateLeaderCardAction.setNickname(nick1);
        activateLeaderCardAction.setGame(game);
        game.getPlayer(nick1).setTempLeaderCards(List.of(dumbLeaderCard.convert()));
        leaderCard = dumbLeaderCard.convert();
        game.getPlayer(nick1).getPersonalBoard().setLeaderCards(List.of(leaderCard));
    }

    @Test
    @DisplayName("Leader card has been activated")
    void executeTest() {
        init(1);
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
        void wrongTurnTest() throws Exception{
            init(2);
            game.getPlayer(nick2).startTurn();
            try{
                activateLeaderCardAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            }catch (InvalidActionException e) {
            }
        }

        @Test
        @DisplayName("A not owned leader card hasn't been activated")
        void invalidLeaderCardTest() {
            init(3);
            game.getPlayer(nick1).startTurn();

            DumbConvertLeaderCard dumbLeaderCard1 = new DumbConvertLeaderCard(new ConvertLeaderCard(1, new LeaderCardRequirements(Map.of(Color.BLUE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 0)), Map.of(Resource.STONE, 0)), Resource.COIN));
            ActivateLeaderCardAction activateLeaderCardAction1 = new ActivateLeaderCardAction(dumbLeaderCard1);
            activateLeaderCardAction1.setNickname(nick1);
            activateLeaderCardAction1.setGame(game);

            game.getPlayer(nick1).addAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
            try{
                activateLeaderCardAction1.runChecks();
                throw new AssertionError("Exception was not thrown");
            }catch (InvalidActionException e) {
            }
        }

        @Test
        @DisplayName("Player hasn't enough resources and can't afford leader card")
        void notEnoughResourcesTest() {
            init(4);
            game.getPlayer(nick1).startTurn();

            DumbConvertLeaderCard dumbLeaderCard1 = new DumbConvertLeaderCard(new ConvertLeaderCard(1, new LeaderCardRequirements(Map.of(Color.GREEN, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 0)), Map.of(Resource.STONE, 1)), Resource.COIN));
            ActivateLeaderCardAction activateLeaderCardAction1 = new ActivateLeaderCardAction(dumbLeaderCard1);
            activateLeaderCardAction1.setNickname(nick1);
            activateLeaderCardAction1.setGame(game);
            game.getPlayer(nick1).setTempLeaderCards(List.of(dumbLeaderCard1.convert()));
            leaderCard = dumbLeaderCard1.convert();
            game.getPlayer(nick1).getPersonalBoard().setLeaderCards(List.of(leaderCard));

            game.getPlayer(nick1).addAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
            try{
                activateLeaderCardAction1.runChecks();
                throw new AssertionError("Exception was not thrown");
            }catch (InvalidActionException e) {
            }
        }
    }
}
