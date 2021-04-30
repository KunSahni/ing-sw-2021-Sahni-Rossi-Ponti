package it.polimi.ingsw.server.controller.message.action.gameaction;

import it.polimi.ingsw.server.controller.gamepackage.Game;
import it.polimi.ingsw.server.model.Player;

import java.util.List;

public class DealLeaderCardsAction extends GameAction {
    List<Player> playersList;

    public DealLeaderCardsAction(Game game) {
        this.playersList = game.getPlayers();
    }

    /**
     * Assigns four LeaderCards to each player to allow them to
     * choose two of them.
     */
    @Override
    public void execute() {
        playersList.forEach(player ->
                player.addLeaderCards(player.getGame().getLeaderCardsDeck().popFour()));
    }

    /**
     * Checks if all players do not have temporary nor chosen LeaderCards
     * already assigned to them.
     */
    @Override
    public boolean isAllowed() {
        return playersList.stream()
                .allMatch(player ->
                        player.getLeaderCards().size() == 0 &&
                        player.getPersonalBoard().getLeaderCards().size() == 0);
    }
}
