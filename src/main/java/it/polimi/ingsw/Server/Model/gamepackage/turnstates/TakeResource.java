package it.polimi.ingsw.server.model.gamepackage.turnstates;

import it.polimi.ingsw.server.model.Market;
import it.polimi.ingsw.server.model.ResourceBank;
import it.polimi.ingsw.server.model.gamepackage.Turn;
import it.polimi.ingsw.server.model.gamepackage.actions.Action;
import it.polimi.ingsw.server.model.gamepackage.actions.TakeResourceAction;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;


public class TakeResource implements AbstractTurnState {
    private Market market;
    private TakeResourceAction action;
    private PersonalBoard board;

    @Override
    public void setup(Turn turn, Action action) {
        this.market = turn.getGame().getMarket();
        this.action = (TakeResourceAction) action;
        this.board = turn.getPlayer().getPersonalBoard();
    }

    @Override
    public void performAction() {
        if (action.isRow())
            chooseRow(action.getValue());
        else
            chooseColumn(action.getValue());
    }

    /**
     * @param line the line from which the player wants to take resources from the market
     */
    public void chooseRow(int line) {
        board.storeResources(
                ResourceBank.getResource(
                        market.chooseRow(line)
                )
        );
    }

    /**
     * @param column the column from which the player wants to take resources from the market
     */
    public void chooseColumn(int column) {
        board.storeResources(
                ResourceBank.getResource(
                        market.chooseColumn(column)
                )
        );
    }
}