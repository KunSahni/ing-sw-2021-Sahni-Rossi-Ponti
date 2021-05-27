package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.client.utils.dumbobjects.DumbConvertLeaderCard;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.action.gameaction.AssignInkwellAction;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.ConvertLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.utils.ExecutedActions;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PregameLeaderCardsChoiceActionTest {
    PregameLeaderCardsChoiceAction pregameLeaderCardsChoiceAction;
    Game game;
    String nick1;
    String nick2;
    Server server;
    ConvertLeaderCard convertLeaderCard1;
    ConvertLeaderCard convertLeaderCard2;

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
        game.getPlayer(nick1).addAction(ExecutedActions.BOUGHT_DEVELOPMENT_CARD_ACTION);
        convertLeaderCard1 = new ConvertLeaderCard(1, new LeaderCardRequirements(Map.of(Color.GREEN, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.COIN, 1)), Resource.COIN);
        convertLeaderCard2 = new ConvertLeaderCard(1, new LeaderCardRequirements(Map.of(Color.PURPLE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.COIN, 1)), Resource.COIN);
        pregameLeaderCardsChoiceAction = new PregameLeaderCardsChoiceAction(List.of(new DumbConvertLeaderCard(convertLeaderCard1), new DumbConvertLeaderCard(convertLeaderCard2)));
        pregameLeaderCardsChoiceAction.setNickname(nick1);
        pregameLeaderCardsChoiceAction.setGame(game);
        game.setState(GameState.DEALT_LEADER_CARDS);
        game.getPlayer(nick1).setTempLeaderCards(List.of(convertLeaderCard1, convertLeaderCard2));
    }

    @Nested
    @DisplayName("Tests for execute method")
    class executeTests {

        @Test
        @DisplayName("Not all players have two leader cards")
        void notAllPlayersHave2LeadersCardTest() {
            init(1);
            assertNull(pregameLeaderCardsChoiceAction.execute());
        }

        @Test
        @DisplayName("All players have two leader cards so next action is to assign inkwell")
        void allPlayersWithTwoLeaderCards() {
            init(2);
            game.getPlayer(nick2).getPersonalBoard().setLeaderCards(List.of(convertLeaderCard1, convertLeaderCard2));
            assertAll(
                    ()-> assertTrue(pregameLeaderCardsChoiceAction.execute() instanceof AssignInkwellAction),
                    ()-> assertEquals(GameState.PICKED_LEADER_CARDS, game.getCurrentState())
            );
        }
    }

    @Nested
    @DisplayName("Tests for runChecks method")
    class runChecksTest {

        @Test
        @DisplayName("All checks are passed")
        void allChecksPassedTest() {
            init(3);
            try {
                pregameLeaderCardsChoiceAction.runChecks();
            } catch (InvalidActionException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Test
        @DisplayName("Not allowed action is rejected")
        void notAllowedPickTimeTest() {
            init(4);
            game.setState(GameState.IN_GAME);

            try {
                pregameLeaderCardsChoiceAction.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You cannot pick Leader Cards at this time.")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("The attempt to pick more or less than two leader card is rejected")
        void notTwoLeaderCardsPickingTest() {
            init(5);
            ConvertLeaderCard convertLeaderCard3 = new ConvertLeaderCard(1, new LeaderCardRequirements(Map.of(Color.BLUE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.COIN, 1)), Resource.COIN);
            PregameLeaderCardsChoiceAction pregameLeaderCardsChoiceAction1 = new PregameLeaderCardsChoiceAction(List.of(new DumbConvertLeaderCard(convertLeaderCard1), new DumbConvertLeaderCard(convertLeaderCard2), new DumbConvertLeaderCard(convertLeaderCard3)));
            pregameLeaderCardsChoiceAction1.setNickname(nick1);
            pregameLeaderCardsChoiceAction1.setGame(game);
            game.getPlayer(nick1).setTempLeaderCards(List.of(convertLeaderCard1, convertLeaderCard2, convertLeaderCard3));

            try {
                pregameLeaderCardsChoiceAction1.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("You can only pick 2 Leader Cards")){
                    e.printStackTrace();
                    throw new AssertionError("Wrong exception was thrown");
                }
            }
        }

        @Test
        @DisplayName("The action is rejected cause player doesn't own a selected leader card")
        void notOwnedLeaderCardChooseTest() {
            init(6);
            ConvertLeaderCard convertLeaderCard3 = new ConvertLeaderCard(1, new LeaderCardRequirements(Map.of(Color.BLUE, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1, 1)), Map.of(Resource.COIN, 1)), Resource.COIN);
            PregameLeaderCardsChoiceAction pregameLeaderCardsChoiceAction1 = new PregameLeaderCardsChoiceAction(List.of(new DumbConvertLeaderCard(convertLeaderCard1), new DumbConvertLeaderCard(convertLeaderCard3)));
            pregameLeaderCardsChoiceAction1.setNickname(nick1);
            pregameLeaderCardsChoiceAction1.setGame(game);

            try {
                pregameLeaderCardsChoiceAction1.runChecks();
                throw new AssertionError("Exception was not thrown");
            } catch (InvalidActionException e) {
                if (!e.getMessage().equals("Selected cards are invalid")){
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
