package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.server.controller.action.gameaction.AssignInkwellAction;
import it.polimi.ingsw.server.controller.action.gameaction.GameAction;
import it.polimi.ingsw.server.model.leadercard.LeaderCard;
import it.polimi.ingsw.server.model.utils.GameState;

import java.util.List;

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
        game.getPlayer(nickname).chooseTwoLeaderCards(leaderCards);
        if (game.getPlayerList()
                .stream()
                .allMatch(player ->
                        player.getPersonalBoard().getLeaderCards().size() == 2)) {
            consequentAction = new AssignInkwellAction(game);
            game.setNextState(GameState.PRE_GAME_ASSIGN_INKWELL);
        }
        return consequentAction;
    }

    @Override
    public void runChecks() throws InvalidActionException {
        if (!game.getCurrentState().equals(GameState.PRE_GAME_LEADER_CARDS_CHOICE))
            throw new InvalidActionException("Wrong game state");
        if (leaderCards.size() != 2)
            throw new InvalidActionException("You can only pick 2 Leader Cards");
        if (!game.getPlayer(nickname).getTempLeaderCards().containsAll(leaderCards))
            throw new InvalidActionException("Selected cards are invalid");
    }
}
