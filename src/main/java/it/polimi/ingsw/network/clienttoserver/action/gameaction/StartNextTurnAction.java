package it.polimi.ingsw.network.clienttoserver.action.gameaction;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.utils.GameState;

import java.util.List;
import java.util.Optional;

/**
 * Class which, when possible, starts the next turn. Contains the logic to check for a player's
 * winning move, starting the last round of turns or ending the game altogether.
 */
public class StartNextTurnAction extends GameAction {

    public StartNextTurnAction(Game game) {
        super(game);
    }

    @Override
    public GameAction execute() {
        List<Player> players = game.getPlayerList();
        Optional<Player> currentPlayer = game.getCurrentTurnPlayer();
        // If at least one of the players is connected
        if (players.stream().anyMatch(Player::isConnected)) {
            // If the game is not in LAST_ROUND state
            if (!game.getCurrentState().equals(GameState.LAST_ROUND)) {
                players.addAll(game.getPlayerList());
            }
            // If the game is not in LAST_ROUND a player will always be found
            // In LAST_ROUND a player will be found only if there are players
            // that still have to play their turn in LAST_ROUND
            Optional<Player> nextPlayer = findNextConnected(players.subList(currentPlayer.map(player -> players.indexOf(player) + 1).orElse(0)
                    , players.size()));
            if (nextPlayer.isPresent()) {
                currentPlayer.ifPresent(Player::finishTurn);
                nextPlayer.get().startTurn();
            } else {
                game.end();
            }
        }
        return null;
    }

    /**
     * Searches for the first connected player in the given List
     * @param players List of Players.
     * @return empty optional if, in the given list, no player is connected. Otherwise
     * returns the first instance of a connected player in the collection.
     */
    private Optional<Player> findNextConnected(List<Player> players) {
        return players.stream().filter(Player::isConnected).findFirst();
    }
}