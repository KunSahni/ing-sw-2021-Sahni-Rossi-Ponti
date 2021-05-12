package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.utils.GameState;

public class StartGameAction extends GameAction {
    public StartGameAction(Game game) {
        super(game);
    }

    @Override
    public GameAction execute() {
        game.getPlayerList()
                .stream()
                .filter(player -> player.getPosition() == 1)
                .forEach(Player::startTurn);
        game.setNextState(GameState.IN_GAME_TURN_STARTED);
        return null;
    }
}
