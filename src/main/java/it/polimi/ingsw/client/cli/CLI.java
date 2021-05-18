package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.network.message.messages.AuthenticationMessage;
import it.polimi.ingsw.network.message.messages.CreateLobbyMessage;
import it.polimi.ingsw.server.controller.action.playeraction.PregameLeaderCardsChoiceAction;
import it.polimi.ingsw.server.controller.message.choice.LeaderCardsChoiceMessage;
import it.polimi.ingsw.server.model.market.MarketMarble;

import java.io.PrintWriter;
import java.util.ArrayList;
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
        System.out.println(String.valueOf(leaderCards));
        PregameLeaderCardsChoiceAction pregameLeaderCardsChoiceAction = new PregameLeaderCardsChoiceAction(new ArrayList<>(leaderCards.subList(0,2)));
        client.sendAction(pregameLeaderCardsChoiceAction);
    }

    @Override
    public void renderTempMarbles(Map<MarketMarble, Integer> updateMarbles) {

    }

    @Override
    public void renderResourcePregameChoice(int numberOfResources) {
        System.out.println("You need to choose " + numberOfResources + " resources.");
    }

    @Override
    public void renderMarket(DumbMarket market) {

    }

    @Override
    public void renderMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void renderErrorMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void renderAuthenticationRequest(String message) {
        System.out.println(message);
        String nickname = in.nextLine();
        client.sendMessage(new AuthenticationMessage(nickname, -1));
    }

    @Override
    public void renderCreateLobbyRequest(String message) {
        System.out.println(message);
        int size = in.nextInt();
        client.sendMessage(new CreateLobbyMessage(size));
    }

    public void setClient(Client client) {
        this.client = client;
    }
}