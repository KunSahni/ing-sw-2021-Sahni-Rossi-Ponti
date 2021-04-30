package it.polimi.ingsw.server.controller.message.action.playeraction;

import it.polimi.ingsw.server.model.leadercard.LeaderCard;

import java.util.List;

/**
 * This class represents the action of choosing two LeaderCards at the beginning of the game
 */
public class PregameLeaderCardsChoiceAction extends PlayerAction {
    private final List<LeaderCard> leaderCards;

    /**
     * @param leaderCards a list of two LeaderCards which will be kept,
     *                    while the other LeaderCards will be discarded
     */
    public PregameLeaderCardsChoiceAction(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }


    @Override
    public void execute() {
        player.setLeaderCards(leaderCards);
    }

    @Override
    public boolean isAllowed() {
        return leaderCards.size() == 2 && player.getLeaderCards().containsAll(leaderCards);
    }
}
