package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.GameState;

public class StartGameAction extends GameAction {
    public StartGameAction(Game game) {
        super(game);
    }

    @Override
    public GameAction execute() {
        game.sortPlayers();
        game.getPlayerList().get(0).startTurn();
        game.setNextState(GameState.IN_GAME_TURN_STARTED);
        return null;
    }
}
