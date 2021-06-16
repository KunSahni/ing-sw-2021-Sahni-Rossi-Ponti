package it.polimi.ingsw.network.clienttoserver.action.playeraction;

import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.network.clienttoserver.action.gameaction.AssignInkwellAction;
import it.polimi.ingsw.network.clienttoserver.action.gameaction.GameAction;
import it.polimi.ingsw.network.servertoclient.renderable.ConfirmationMessageType;
import it.polimi.ingsw.server.model.utils.GameState;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the action of choosing two LeaderCards at the beginning of the game
 */
public class PregameLeaderCardsChoiceAction extends PlayerAction {
    private final List<DumbLeaderCard> leaderCards;

    /**
     * @param leaderCards a list of two LeaderCards which will be kept,
     *                    while the other LeaderCards will be discarded
     */
    public PregameLeaderCardsChoiceAction(List<DumbLeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    @Override
    public GameAction execute() {
        GameAction consequentAction = null;
        game.getPlayer(nickname).chooseTwoLeaderCards(leaderCards.stream()
                .map(DumbLeaderCard::convert).collect(Collectors.toList()));
        if (game.getPlayerList()
                .stream()
                .allMatch(player ->
                        player.getPersonalBoard().getLeaderCards().size() == 2)) {
            consequentAction = new AssignInkwellAction(game);
            game.setState(GameState.PICKED_LEADER_CARDS);
        }
        return consequentAction;
    }

    @Override
    public void runChecks() throws InvalidActionException {
        if (!game.getCurrentState().equals(GameState.DEALT_LEADER_CARDS))
            throw new InvalidActionException("You cannot pick Leader Cards at this time.");
        if (leaderCards.size() != 2)
            throw new InvalidActionException("You can only pick 2 Leader Cards");
        if (!game.getPlayer(nickname).getTempLeaderCards().containsAll(
                leaderCards.stream().map(DumbLeaderCard::convert).collect(Collectors.toList())))
            throw new InvalidActionException("Selected cards are invalid");
    }


    @Override
    public ConfirmationMessageType getConfirmationMessage() {
        return ConfirmationMessageType.PREGAME_LEADER_CARDS_CHOICE;
    }
}
