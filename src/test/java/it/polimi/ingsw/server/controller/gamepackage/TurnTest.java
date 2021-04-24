package it.polimi.ingsw.server.controller.gamepackage;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.gamepackage.Turn;
import it.polimi.ingsw.server.controller.gamestates.LastRound;
import it.polimi.ingsw.server.controller.message.action.ActivateLeaderCardAction;
import it.polimi.ingsw.server.controller.message.action.Forwardable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.ConvertLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

import static org.junit.jupiter.api.Assertions.*;

public class TurnTest {
    Game game;
    Turn turn;
    Player player;

    @BeforeEach
    void init(){
        game = new Game(1, 2);
        player = new Player("Nick", game);
        turn = new Turn(game, game.getPlayers().get(0));
        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), 1), 1);
        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), 1), 0);
        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), 1), 2);
    }

    @Test
    void turnTest(){
        assertAll(
                ()->assertEquals(turn.getGame(), game),
                ()->assertEquals(turn.getPlayer(), game.getPlayers().get(0))
        );
    }

    @Test
    void nextActionTest(){
        Forwardable nextAction = new ActivateLeaderCardAction(new ConvertLeaderCard(1, new LeaderCardRequirements(null, null), Resource.COIN));

        SubmissionPublisher<Forwardable> publisher = new SubmissionPublisher<>();
        publisher.subscribe(turn);
        publisher.submit(nextAction);
        assertEquals(nextAction, turn.getCurrentAction());
    }

    @Test
    void triggerLastRoundTest(){
        Map<Resource, Integer> map = new HashMap<>();
        Forwardable nextAction = new ActivateLeaderCardAction(new ConvertLeaderCard(1, new LeaderCardRequirements(null, null), Resource.COIN));

        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 3, map, map, map, 1), 0);
        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL2, 3, map, map, map, 1), 0);
        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL3, 3, map, map, map, 1), 0);
        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 3, map, map, map, 1), 1);
        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL2, 3, map, map, map, 1), 1);
        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL3, 3, map, map, map, 1), 1);
        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 3, map, map, map, 1), 2);

        SubmissionPublisher<Forwardable> publisher = new SubmissionPublisher<>();
        publisher.subscribe(turn);
        publisher.submit(nextAction);
        assertEquals(new LastRound(game), game.getCurrentState());

    }

    @Test
    void onSubscribeTest(){

    }

    @Test
    void onNextTest(){
        Forwardable nextAction = new ActivateLeaderCardAction(new ConvertLeaderCard(1, new LeaderCardRequirements(null, null), Resource.COIN));

        turn.onNext(nextAction);
        assertEquals(nextAction, turn.getCurrentAction());
    }
}