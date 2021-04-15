package it.polimi.ingsw.server.controller.message.action;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.utils.ResourceBank;
import it.polimi.ingsw.server.controller.gamepackage.Turn;


/**
 * This class represents the action of taking Resources from the Market
 */
public class TakeResourceAction implements Forwardable {
    private final Market market;
    private final Player player;
    private final int value;
    private final boolean row;

    /**
     * @param turn the turn in which this action is happening
     * @param player the Player performing the action
     * @param value the number of the row or column from which the Player wants to take the Resources
     * @param row a boolean representing if the Player wants to take Resources from a row(true) or a column(false)
     */
    public TakeResourceAction(Turn turn, Player player, int value, boolean row) {
        this.market = turn.getGame().getMarket();
        this.player = player;
        this.value = value;
        this.row = row;
    }

    @Override
    public void forward() {
        if (row)
            chooseRow(value);
        else
            chooseColumn(value);
    }

    /**
     * @param row the line from which the player wants to take resources from the market
     */
    public void chooseRow(int row) {
        player.storeResources(
                ResourceBank.getResourceFromMarble(
                        market.chooseRow(row)
                )
        );
    }

    /**
     * @param column the column from which the player wants to take resources from the market
     */
    public void chooseColumn(int column) {
        player.storeResources(
                ResourceBank.getResourceFromMarble(
                        market.chooseColumn(column)
                )
        );
    }
}