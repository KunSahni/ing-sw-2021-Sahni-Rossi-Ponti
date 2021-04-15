package it.polimi.ingsw.server.controller.messages.choices;

import it.polimi.ingsw.server.model.leadercard.LeaderCard;

import java.util.List;

/**
 * This class contains a list of LeaderCards from which the Player needs to pick two
 */
public class LeaderCardsChoiceMessage implements Message{
    private final List<LeaderCard> leaderCards;

    public LeaderCardsChoiceMessage(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }
}
