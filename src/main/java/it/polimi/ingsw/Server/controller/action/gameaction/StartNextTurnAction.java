package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

public class StartNextTurnAction extends GameAction {

    public StartNextTurnAction(Game game) {
        super(game);
    }

    @Override
    public GameAction execute() {
        game.getPlayerList().stream().filter(Player::isPlayersTurn).forEach(
                player -> {
                    player.finishTurn();
                    game.getPlayerList().stream()
                            .filter(nextPlayer -> nextPlayer.getPosition() ==
                                    (player.getPosition() + 1) % game.getPlayerList().size())
                            .forEach(Player::startTurn);
                }
        );
    }

}