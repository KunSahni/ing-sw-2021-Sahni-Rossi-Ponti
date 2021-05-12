package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.model.Game;

public class InitFaithPositionsAction extends GameAction{
    public InitFaithPositionsAction(Game game) {
        super(game);
    }

    @Override
    public GameAction execute() {
        game.getPlayerList().stream()
                .filter(player -> player.getPosition() > 2)
                .forEach(player -> player.getPersonalBoard().getFaithTrack().moveMarker());
        return new StartGameAction(game);
    }
}
