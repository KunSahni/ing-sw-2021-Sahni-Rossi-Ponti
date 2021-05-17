package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.server.model.market.MarketMarble;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CLI extends UI {
    private Scanner in;
    private PrintWriter out;
    private Client client;

    public CLI() {
        in = new Scanner(System.in);
        out = new PrintWriter(System.out);
    }

    @Override
    public void renderPersonalBoard(DumbPersonalBoard personalBoard) {

    }

    @Override
    public void renderDevelopmentCardsBoard(DumbDevelopmentCardsBoard developmentCardsBoard) {

    }

    @Override
    public void renderActionTokenDeck(DumbActionTokenDeck actionTokenDeck) {

    }

    @Override
    public void renderLeaderCardsChoice(List<DumbLeaderCard> leaderCards) {

    }

    @Override
    public void renderTempMarbles(Map<MarketMarble, Integer> updateMarbles) {

    }

    @Override
    public void renderResourcePregameChoice(int numberOfResources) {

    }

    @Override
    public void renderMarket(DumbMarket market) {

    }

    @Override
    public void renderMessage(String message) {

    }

    @Override
    public void renderErrorMessage(String message) {

    }

    @Override
    public void renderAuthenticationRequest(String message) {

    }

    @Override
    public void renderCreateLobbyRequest(String message) {

    }

    public void setClient(Client client) {
        this.client = client;
    }
}