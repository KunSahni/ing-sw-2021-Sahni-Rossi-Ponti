package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.actiontoken.ActionTokenDeck;
import it.polimi.ingsw.server.model.developmentcard.DevelopmentCardsBoard;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.personalboardpackage.PersonalBoard;

import java.util.List;
import java.util.Optional;

/**
 * This class contains a copy of some elements of the Model contained on the Server,
 * each element is updated thanks to the Renderables sent by the Server
 * and each element is rendered graphically either on CLI or GUI
 */

public class ModelView {
    private List<PersonalBoard> personalBoards;
    private Market market;
    private DevelopmentCardsBoard developmentCardsBoard;
    private ActionTokenDeck actionTokenDeck;

    /**
     * This method updates the existing personal board in personalBoards with the one passed as parameter
     * @param personalBoard an updated PersonalBoard
     */
    public void updatePersonalBoard(PersonalBoard personalBoard) {
        //Find the reference to the PersonalBoard to update
        Optional<PersonalBoard> existingPersonalBoard = this.personalBoards.stream().filter(
                pb -> pb.getPlayer().equals(personalBoard.getPlayer())
        ).findFirst();
        //Update the PersonalBoard
        existingPersonalBoard.ifPresent(
                pb -> pb = personalBoard
        );
    }

    /**
     * This method updates the market with with the one passed as parameter
     * @param market the updated Market
     */
    public void updateMarket(Market market) {
        this.market = market;
    }

    /**
     * This method updates the developmentCardsBoard with with the one passed as parameter
     * @param developmentCardsBoard the updated developmentCardsBoard
     */
    public void updateDevelopmentCardsBoard(DevelopmentCardsBoard developmentCardsBoard) {
        this.developmentCardsBoard = developmentCardsBoard;
    }

    /**
     * This method updates the developmentCardsBoard with with the one passed as parameter
     * @param actionTokenDeck the updated actionTokenDeck
     */
    public void updateActionTokenDeck(ActionTokenDeck actionTokenDeck) {
        this.actionTokenDeck = actionTokenDeck;
    }

    public List<PersonalBoard> getPersonalBoards() {
        return personalBoards;
    }

    public PersonalBoard getPersonalBoard(Player player) {
        return personalBoards.stream().filter(
                personalBoard -> personalBoard.getPlayer().equals(player)
        ).findFirst().get();
    }

    public Market getMarket() {
        return market;
    }

    public DevelopmentCardsBoard getDevelopmentCardsBoard() {
        return developmentCardsBoard;
    }

    public ActionTokenDeck getActionTokenDeck() {
        return actionTokenDeck;
    }
}
