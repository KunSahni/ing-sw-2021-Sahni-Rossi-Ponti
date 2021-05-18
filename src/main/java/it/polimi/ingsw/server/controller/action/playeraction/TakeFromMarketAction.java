package it.polimi.ingsw.server.controller.action.playeraction;

import it.polimi.ingsw.server.controller.action.gameaction.GameAction;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.Actions;

import java.util.Map;
import java.util.Optional;


/**
 * This class represents the action of taking Resources from the Market
 */
public class TakeFromMarketAction extends PlayerAction {
    private final int value;
    private final boolean isRow;

    public TakeFromMarketAction(int value, boolean isRow) {
        this.value = value;
        this.isRow = isRow;
    }

    @Override
    public GameAction execute() {
        Map<MarketMarble, Integer> tempMarbles;
        if (isRow)
            tempMarbles = game.getMarket().chooseRow(value);
        else
            tempMarbles = game.getMarket().chooseColumn(value);
        // Execute Faith Marker moves if RED marbles are present
        if (Optional.ofNullable(tempMarbles.get(MarketMarble.RED)).isPresent()) {
            super.moveFaithMarker(tempMarbles.get(MarketMarble.RED));
            tempMarbles.remove(MarketMarble.RED);
        }
        player.setTempMarbles(tempMarbles);
        player.addAction(Actions.STORED_TEMP_MARBLES_ACTION);
        return null;
    }

    @Override
    public void runChecks() throws InvalidActionException {
        super.runChecks();
        if (!player.isValidNextAction(Actions.STORED_TEMP_MARBLES_ACTION))
            throw new InvalidActionException("You cannot take from market at this time");
    }
}