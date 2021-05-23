package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.utils.GameState;

import java.util.List;
import java.util.Optional;


public class StartNextTurnAction extends GameAction {

    public StartNextTurnAction(Game game) {
        super(game);
    }

    @Override
    public GameAction execute() {
        List<Player> players = game.getPlayerList();
        Player currentPlayer = game.getCurrentTurnPlayer();
        // If at least one of the players is connected
        if (players.stream().anyMatch(Player::isConnected)) {
            // If the game is not in LAST_ROUND state
            if (!game.getCurrentState().equals(GameState.LAST_ROUND)) {
                players.addAll(game.getPlayerList());
            }
            // If the game is not in LAST_ROUND a player will always be found
            // In LAST_ROUND a player will be found only if there are players
            // that still have to play their turn in LAST_ROUND
            Optional<Player> nextPlayer =
                    findNextConnected(players.subList(players.indexOf(currentPlayer) + 1,
                            players.size() + 1));
            if (nextPlayer.isPresent()) {
                currentPlayer.finishTurn();
                nextPlayer.get().startTurn();
            } else {
                game.end();
            }
        }
        return null;
    }

    private Optional<Player> findNextConnected(List<Player> players) {
        return players.stream().filter(Player::isConnected).findFirst();
    }
}