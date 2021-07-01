package it.polimi.ingsw.network.clienttoserver.action.gameaction;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.GameState;

/**
 * Class representing the act of dealing 4 leader cards to each player,
 * giving each one of them the ability to pick 2 out of the 4.
 */
public class DealLeaderCardsAction extends GameAction {
    public DealLeaderCardsAction(Game game) {
        super(game);
    }

    /**
     * Assigns four LeaderCards to each player to allow them to
     * choose two of them.
     */
    @Override
    public GameAction execute() {
        game.getPlayerList().forEach(player ->
                player.setTempLeaderCards(game.getLeaderCardsDeck().popFour()));
        game.setState(GameState.DEALT_LEADER_CARDS);
        return null;
    }
}
