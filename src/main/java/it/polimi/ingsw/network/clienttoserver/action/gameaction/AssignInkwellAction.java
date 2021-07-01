package it.polimi.ingsw.network.clienttoserver.action.gameaction;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.utils.GameState;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Class representing the assignment of the Inkwell marker and consequent
 * move order for all players.
 */
public class AssignInkwellAction extends GameAction{
    public AssignInkwellAction(Game game) {
        super(game);
    }

    @Override
    public GameAction execute() {
        GameAction consequentAction = null;
        List<Player> playerList = game.getPlayerList();
        Collections.shuffle(playerList);
        IntStream.range(1, playerList.size() + 1)
                .forEach(i -> playerList.get(i - 1).setPosition(i));
        // Move Faith Markers by one tile for players 3 and 4
        playerList.stream()
                .filter(player -> player.getPosition() > 2)
                .forEach(player -> player.getPersonalBoard().getFaithTrack().moveMarker());
        game.sortPlayers();
        if (game.size() == 1) {
            consequentAction = new StartGameAction(game);
            game.setState(GameState.PICKED_RESOURCES);
        } else {
            game.setState(GameState.ASSIGNED_INKWELL);
        }
        return consequentAction;
    }
}
