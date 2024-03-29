package it.polimi.ingsw.network.clienttoserver.action.playeraction;

import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.network.clienttoserver.action.gameaction.GameAction;
import it.polimi.ingsw.network.servertoclient.renderable.ConfirmationMessageType;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.utils.ExecutedActions;

/**
 * This class represents the action of activating a LeaderCard chosen by a Player
 */
public class ActivateLeaderCardAction extends PlayerAction {
    private final DumbLeaderCard leaderCard;

    /**
     * @param leaderCard the leader card that needs to be activated
     */
    public ActivateLeaderCardAction(DumbLeaderCard leaderCard) {
        this.leaderCard = leaderCard;
    }

    @Override
    public GameAction execute() {
        player.getPersonalBoard().activateLeaderCard(leaderCard.convert());
        player.addAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION);
        return null;
    }

    @Override
    public void runChecks() throws InvalidActionException{
        super.runChecks();
        if (!player.isValidNextAction(ExecutedActions.ACTIVATED_LEADER_CARD_ACTION))
            throw new InvalidActionException("You cannot activate a LeaderCard at this time");
        if (player.getPersonalBoard()
                .getLeaderCards()
                .stream()
                .noneMatch(card -> card.equals(leaderCard.convert())))
            throw new InvalidActionException("You do not have the selected LeaderCard");
        if (player.getPersonalBoard()
                .getLeaderCards()
                .stream()
                .filter(card -> card.equals(leaderCard.convert()))
                .allMatch(LeaderCard::isActive))
            throw new InvalidActionException("LeaderCard is already active");
        if (!player.getPersonalBoard()
                .containsLeaderCardRequirements(leaderCard.getLeaderCardRequirements()))
            throw new InvalidActionException("You cannot afford to activate this LeaderCard");
    }

    @Override
    public ConfirmationMessageType getConfirmationMessage() {
        return ConfirmationMessageType.ACTIVATE_LEADER_CARD;
    }
}