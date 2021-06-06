package it.polimi.ingsw.network.clienttoserver.action.gameaction;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.GameState;

public class StartGameAction extends GameAction {
    public StartGameAction(Game game) {
        super(game);
    }

    @Override
    public GameAction execute() {
        game.getPlayerList().get(0).startTurn();
        game.setState(GameState.IN_GAME);
        return null;
    }
}
