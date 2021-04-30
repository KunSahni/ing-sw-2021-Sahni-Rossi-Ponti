package it.polimi.ingsw.server.controller.gamepackage;

import it.polimi.ingsw.server.controller.message.action.ActivateLeaderCardAction;
import it.polimi.ingsw.server.controller.message.action.EndAction;
import it.polimi.ingsw.server.controller.message.action.Forwardable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.ConvertLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SinglePlayerTurnTest {
    Game game;
    SinglePlayerTurn singlePlayerTurn;
    Player player;

    @BeforeEach
    void init(){
        game = new Game(1, 1);
        player = new Player("Nick", game);
        singlePlayerTurn = new SinglePlayerTurn(game, game.getPlayers().get(0));
    }

    @Test
    void nextActionTest(){
        Forwardable nextAction = new ActivateLeaderCardAction(new ConvertLeaderCard(1, new LeaderCardRequirements(null ,null), Resource.COIN));

        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), 1), 1);
        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), 1), 0);
        game.getPlayers().get(0).getPersonalBoard().placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), Map.of(Resource.STONE, 1), 1), 2);
        singlePlayerTurn.nextAction(nextAction);
        assertEquals(singlePlayerTurn.getCurrentAction(), nextAction);
    }

    @Test
    void pickActionToken(){
        Forwardable nextAction = new EndAction();
        List<ActionToken> list = new ArrayList<>();
        List<ActionToken> list1 = new ArrayList<>();

        list.addAll(game.getActionTokenDeck().getCurrentDeck());
        singlePlayerTurn.nextAction(nextAction);
        list1.addAll(game.getActionTokenDeck().getCurrentDeck());
        assertEquals(list.size()-1, list1.size());
    }
}
