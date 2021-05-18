package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.controller.action.gameaction.GameAction;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.utils.Actions;

/**
 * This class represents the action of discarding a LeaderCard chosen by a Player
 */
public class DiscardLeaderCardAction extends PlayerAction {
    private final LeaderCard leaderCard;

    /**
     * @param leaderCard the leader card that needs to be activated
     */
    public DiscardLeaderCardAction(LeaderCard leaderCard) {
        this.leaderCard = leaderCard;
    }

    @Override
    public GameAction execute() {
        player.getPersonalBoard().discardLeaderCard(leaderCard);
        super.moveFaithMarker(1);
        player.addAction(Actions.DISCARDED_LEADER_CARD_ACTION);
        return null;
    }

    @Override
    public void runChecks() throws InvalidActionException{
        super.runChecks();
        if (!player.isValidNextAction(Actions.DISCARDED_LEADER_CARD_ACTION))
            throw new InvalidActionException("You cannot discard a LeaderCard at this time");
        if (!player.getPersonalBoard().getLeaderCards().contains(leaderCard))
            throw new InvalidActionException("You do not have the selected LeaderCard in your hand");
        if (player.getPersonalBoard().getLeaderCards().stream()
                .filter(card -> card.equals(leaderCard))
                .anyMatch(LeaderCard::isActive))
            throw new InvalidActionException("Cannot discard an active LeaderCard");
    }
}