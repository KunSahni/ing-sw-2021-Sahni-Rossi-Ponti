package it.polimi.ingsw.server.controller.messages.actions;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;

import java.util.List;

/**
 * This class represents the action of choosing two LeaderCards at the beginning of the game
 */
public class LeaderCardsChoiceAction implements Forwardable{
    private final List<LeaderCard> leaderCards;
    private final Player player;

    /**
     * @param leaderCards a list of two LeaderCards which will be kept, while the other LeaderCards will be discarded
     * @param player the Player performing the action
     */
    public LeaderCardsChoiceAction(List<LeaderCard> leaderCards, Player player) {
        this.leaderCards = leaderCards;
        this.player = player;
    }

    @Override
    public void forward() {
        player.setLeaderCards(leaderCards);
    }
}
