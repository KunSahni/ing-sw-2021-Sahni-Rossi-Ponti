package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.controller.message.action.playeraction.ActivateLeaderCardAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.developmentcard.Color;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCard;
import it.polimi.ingsw.server.model.developmentcard.Level;
import it.polimi.ingsw.server.model.leadercard.DiscountLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardRequirements;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;
import it.polimi.ingsw.server.model.utils.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ActivateLeaderCardActionTest {
    ActivateLeaderCardAction activateLeaderCardAction;
    LeaderCard leaderCard;
    Game game;
    Player player;
    PersonalBoard personalBoard;

    @BeforeEach
    void init(){
        leaderCard = new DiscountLeaderCard(1,new LeaderCardRequirements(java.util.Map.of(Color.GREEN, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1,1)), java.util.Map.of(Resource.COIN, 1)), Resource.STONE);
        activateLeaderCardAction = new ActivateLeaderCardAction(leaderCard);
        game = new Game(1, 1);
        player = new Player("Nick", game);
        personalBoard = new PersonalBoard(player);
        personalBoard.setLeaderCards(Arrays.asList(leaderCard));
        personalBoard.placeDevelopmentCard(new DevelopmentCard(Color.GREEN, Level.LEVEL1, 1, Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), Map.of(Resource.COIN, 1), 1), 1);
        personalBoard.storeInStrongbox(Map.of(Resource.COIN, 1));
    }

    @Test
    @DisplayName("Right LeaderCard has been activated")
    void RightLeaderCardActivated() {
        activateLeaderCardAction.execute();
        assertTrue(leaderCard.isActive());
    }

    @Test
    @DisplayName("Only the right LeaderCard has been activated")
    void ActivatedOnlyTheRightLeaderCard() {
        LeaderCard leaderCard1 = new DiscountLeaderCard(1,new LeaderCardRequirements(java.util.Map.of(Color.GREEN, new LeaderCardRequirements.LevelQuantityPair(Level.LEVEL1,1)), java.util.Map.of(Resource.COIN, 1)), Resource.STONE);
        personalBoard.setLeaderCards(Arrays.asList(leaderCard, leaderCard1));
        activateLeaderCardAction.execute();
        assertAll(
                ()-> assertFalse(leaderCard1.isActive()),
                ()->assertTrue(leaderCard.isActive())
        );
    }
}
