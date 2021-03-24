package it.polimi.ingsw.server.model.gamepackage.actions;

import java.util.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

public class TakeResourceAction implements Action {
    private final PersonalBoard board;
    private final Market market;
    private final LeaderCard leaderCard;
    private final ResourceBank resourceBank;

    public TakeResourceAction(PersonalBoard board, Market market, LeaderCard leaderCard, ResourceBank resourceBank) {
        this.board = board;
        this.market = market;
        this.leaderCard = leaderCard;
        this.resourceBank = resourceBank;
    }

    @Override
    public void performAction() {
    }

    /**
    * @param line the line from which the player wants to take resources from the market
    */
    public void chooseRow(int line) {
        board.storeResources(
                resourceBank.getResource(
                        market.chooseRow(line)
                )
        );
    }

    /**
    * @param column the column from which the player wants to take resources from the market
    */
    public void chooseColumn(int column) {
        board.storeResources(
                resourceBank.getResource(
                        market.chooseColumn(column)
                )
        );
    }

}