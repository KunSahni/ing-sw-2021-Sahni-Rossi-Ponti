package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.market.MarketMarble;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a copy of some elements of the Model contained on the Server,
 * each element is updated thanks to the Renderable sent by the Server
 * and each element is rendered graphically either on CLI or GUI
 */
public class DumbModel {
    private final List<DumbPersonalBoard> personalBoards;
    private final DumbMarket market;
    private final DumbDevelopmentCardsBoard developmentCardsBoard;
    private final DumbActionTokenDeck actionTokenDeck;
    private final int size;
    private final int gameID;

    public DumbModel(int gameID, int size) {
        this.gameID = gameID;
        this.size = size;
        personalBoards = new ArrayList<>();
        market = DumbMarket.getInstance();
        developmentCardsBoard = new DumbDevelopmentCardsBoard();
        actionTokenDeck = (size<=1) ? null: new DumbActionTokenDeck();
    }

    public void addPersonalBoard(String nickname, int position){
        personalBoards.add(new DumbPersonalBoard(nickname, position, size==1));
    }

    public void updateMarket(List<MarketMarble> updatedMarketMarbles) {
        this.market.updateMarket(updatedMarketMarbles);
    }

    public void updateDevelopmentCardsBoard(DumbDevelopmentCard[] updatedDevelopmentCards) {
        this.developmentCardsBoard.updateBoard(updatedDevelopmentCards);
    }

    public void updateActionTokenDeck(List<ActionToken> updatedActionTokens) {
        this.actionTokenDeck.updateActionTokens(updatedActionTokens);
    }

    public List<DumbPersonalBoard> getPersonalBoards() {
        return personalBoards;
    }

    public DumbMarket getMarket() {
        return market;
    }

    public DumbDevelopmentCardsBoard getDevelopmentCardsBoard() {
        return developmentCardsBoard;
    }

    public DumbActionTokenDeck getActionTokenDeck() {
        return actionTokenDeck;
    }

    public int getSize() {
        return size;
    }

    public int getGameID() {
        return gameID;
    }
}
