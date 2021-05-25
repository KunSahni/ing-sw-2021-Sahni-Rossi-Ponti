package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.GameState;

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
    private final DumbActionTokenDeck actionTokenDeck;  //todo: potrebbe non servire visto che fa tutto il server
    private final int size;
    private final int gameID;
    private GameState gameState;

    public DumbModel(int gameID, int size) {
        this.gameID = gameID;
        this.size = size;
        personalBoards = new ArrayList<>();
        market = DumbMarket.getInstance();
        developmentCardsBoard = DumbDevelopmentCardsBoard.getInstance();
        actionTokenDeck = (size<=1) ? null: DumbActionTokenDeck.getInstance();
    }

    public void addPersonalBoard(String nickname, int position){
        personalBoards.add(new DumbPersonalBoard(nickname, position, size==1));
    }

    public void updateGameState(GameState updatedGameState) {
        this.gameState = updatedGameState;
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

    public GameState getGameState() {
        return gameState;
    }
}
