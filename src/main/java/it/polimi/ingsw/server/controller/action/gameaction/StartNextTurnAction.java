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
        GameAction consequentAction = null;
        List<Player> players = game.getPlayerList();
        Player currentPlayer = game.getCurrentTurnPlayer();
        if (players.stream().anyMatch(Player::isConnected)) {
            if (!game.getCurrentState().equals(GameState.LAST_ROUND)) {
                players.addAll(game.getPlayerList());
            }
            Optional<Player> nextPlayer =
                    findNextConnected(players.subList(players.indexOf(currentPlayer) + 1,
                            players.size() + 1));
            if (nextPlayer.isPresent()) {
                currentPlayer.finishTurn();
                nextPlayer.get().startTurn();
            } else {
                game.setState(GameState.GAME_FINISHED);
                consequentAction = new EndGameAction(game);
            }
        }
        return consequentAction;
    }

    private Optional<Player> findNextConnected(List<Player> players) {
        return players.stream().filter(Player::isConnected).findFirst();
    }
}