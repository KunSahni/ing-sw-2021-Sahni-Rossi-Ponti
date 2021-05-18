package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.controller.action.gameaction.GameAction;
import it.polimi.ingsw.server.controller.action.gameaction.StartGameAction;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.Map;

/**
 * This class represents the action of choosing some Resources at the beginning of the game and storing them in the depots
 */
public class PregameResourceChoiceAction extends PlayerAction {
    private final Map<Resource, Integer> resources;

    /**
     * @param resources the Resources which the Player has picked
     */
    public PregameResourceChoiceAction(Map<Resource, Integer> resources){
        this.resources = resources;
    }

    @Override
    public GameAction execute() {
        GameAction consequentAction = null;
        game.getPlayer(nickname)
                .getPersonalBoard()
                .storeInDepots(resources);
        if (game.getPlayerList().stream()
                .filter(player -> player.getPosition() != 0)
                .allMatch(player -> player.getPersonalBoard().getResourceCount() > 0)) {
            consequentAction = new StartGameAction(game);
            game.setState(GameState.PICKED_RESOURCES);
        }
        return consequentAction;
    }

    @Override
    public void runChecks() throws InvalidActionException{
        if (!game.getCurrentState().equals(GameState.ASSIGNED_INKWELL))
            throw new InvalidActionException("You cannot choose Pregame Resources at this time.");
        if (game.getPlayer(nickname).getPersonalBoard().getResourceCount() != 0)
            throw new InvalidActionException("You already have resources in your storages.");
        int resourceCount = resources.values()
                .stream()
                .reduce(0, Integer::sum);
        boolean correctCountFlag = switch (game.getPlayer(nickname).getPosition()) {
            case 1 -> resourceCount == 0;
            case 2, 3 -> resourceCount == 1;
            case 4 -> resourceCount == 2;
            default -> false;
        };
        if (!correctCountFlag)
            throw new InvalidActionException("Invalid number of resources supplied");
    }
}
