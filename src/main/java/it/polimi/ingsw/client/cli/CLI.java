package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.network.message.messages.AuthenticationMessage;
import it.polimi.ingsw.network.message.messages.CreateLobbyMessage;
import it.polimi.ingsw.server.model.market.MarketMarble;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.stream.IntStream;

//WARNING: THIS CLI IS INCOMPLETE AND THEREFORE DOESN'T WORK

public class CLI implements UI {
    private final Scanner in;
    private final PrintWriter out;
    private ClientSocket clientSocket;
    private final int size;
    private final String nickname;
    private Flow.Subscription subscription;
    private DumbModel dumbModel;

    public CLI(String nickname, int size) {
        this.nickname = nickname;
        this.size = size;
        in = new Scanner(System.in);
        out = new PrintWriter(System.out);
    }

    @Override
    public void renderPersonalBoard(String nickname) {
        String printableString = dumbModel.getPersonalBoard(nickname).formatPrintableStringAt(2,2);
        printToCLI(printableString);
    }

    @Override
    public void renderCommons() {
        String printableString = dumbModel.getDevelopmentCardsBoard().formatPrintableStringAt(2,2);
        printableString.concat(dumbModel.getMarket().formatPrintableStringAt(15, 72));
        printToCLI(printableString);
    }

    @Override
    public void renderActionTokenDeck(DumbActionTokenDeck actionTokenDeck) {

    }

    @Override
    public void renderGameOutcome(int finalScore) {

    }

    @Override
    public void renderGameOutcome(TreeMap<Integer, String> finalScores) {

    }

    @Override
    public void renderLeaderCardsChoice(List<DumbLeaderCard> leaderCards) {
        String printableString = "";
        IntStream.range(0,leaderCards.size()).forEach(
                i -> printableString.concat(leaderCards.get(i).formatPrintableStringAt(2, 1+17*i))
        );
        printToCLI(printableString);
    }

    @Override
    public void renderTempMarblesChoice(Map<MarketMarble, Integer> updateMarbles) {

    }

    @Override
    public void renderResourcePregameChoice() {
        System.out.println("You need to choose resources.");
    }
    @Override
    public void renderNotification(String message) {
        System.out.println(message);
    }

    @Override
    public void renderErrorMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void renderAuthenticationRequest(String message) {
        System.out.println(message);
        clientSocket.sendMessage(new AuthenticationMessage(this.nickname, -1));
    }

    @Override
    public void renderCreateLobbyRequest(String message) {
        System.out.println(message);
        clientSocket.sendMessage(new CreateLobbyMessage(size));
    }

    @Override
    public DumbModel getDumbModel() {
        return null;
    }

    @Override
    public OnScreenElement getOnScreenElement() {
        return null;
    }

    @Override
    public void setSubscription(Flow.Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public Flow.Subscription getSubscription() {
        return subscription;
    }

    private void printToCLI(String printableString){
        out.print(printableString);
        out.flush();
        resetCommandPosition();
    }

    private void resetCommandPosition(){
        out.print(Constants.CMD_MESSAGE);
        out.flush();
    }
}