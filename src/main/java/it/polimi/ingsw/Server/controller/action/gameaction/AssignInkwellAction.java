package it.polimi.ingsw.server.controller.action.gameaction;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.utils.GameState;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class AssignInkwellAction extends GameAction{
    public AssignInkwellAction(Game game) {
        super(game);
    }

    @Override
    public GameAction execute() {
        List<Player> playerList = game.getPlayerList();
        Collections.shuffle(playerList);
        IntStream.range(1, playerList.size() + 1)
                .forEach(i -> playerList.get(i).setPosition(i));
        // Move Faith Markers by one tile for players 3 and 4
        playerList.stream()
                .filter(player -> player.getPosition() > 2)
                .forEach(player -> player.getPersonalBoard().getFaithTrack().moveMarker());
        game.setState(GameState.ASSIGNED_INKWELL);
        return null;
    }
}
