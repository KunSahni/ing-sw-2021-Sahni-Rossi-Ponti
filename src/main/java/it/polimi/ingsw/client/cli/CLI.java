package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.network.message.messages.AuthenticationMessage;
import it.polimi.ingsw.network.message.messages.CreateLobbyMessage;
import it.polimi.ingsw.server.controller.action.playeraction.PregameResourceChoiceAction;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.Resource;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.stream.IntStream;

//WARNING: THIS CLI IS INCOMPLETE AND THEREFORE DOESN'T WORK

public class CLI implements UI {
    private final Scanner in;
    private final PrintWriter out;
    private ClientSocket clientSocket;
    private String nickname;
    private Flow.Subscription subscription;
    private DumbModel dumbModel;
    private OnScreenElement onScreenElement;

    public CLI() {
        in = new Scanner(System.in);
        out = new PrintWriter(System.out);
    }

    @Override
    public void renderPersonalBoard(String nickname) {
        String printableString = dumbModel.getPersonalBoard(nickname).formatPrintableStringAt(2,2);
        printToCLI(printableString);
        onScreenElement = OnScreenElement.valueOf(nickname);
    }

    @Override
    public void renderCommons() {
        String printableString = dumbModel.getDevelopmentCardsBoard().formatPrintableStringAt(2,2);
        printableString.concat(dumbModel.getMarket().formatPrintableStringAt(15, 72));
        printToCLI(printableString);
        onScreenElement = OnScreenElement.COMMONS;
    }

    @Override
    public void renderActionTokenDeck(DumbActionTokenDeck actionTokenDeck) {
        //needed?
    }

    @Override
    public void renderGameOutcome(int finalScore) {
        String printableString = Constants.ANSI_CLEAR + Constants.ANSI_BOLD + "\033[2;1HYour final score is: " + finalScore + " points";
        printToCLI(printableString);
    }

    @Override
    public void renderGameOutcome(TreeMap<Integer, String> finalScores) {
        String printableString = Constants.ANSI_CLEAR + Constants.ANSI_BOLD + "\033[2;1HThe final scores are:";
        finalScores.forEach(
                (integer, s) -> printableString.concat("\n" + integer + ") " + s)
        );
        printToCLI(printableString);
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
        String printableString = formatPrintableMarblesMapStringAt(updateMarbles, 2 , 2 );
        printToCLI(printableString);
    }

    @Override
    public void renderResourcePregameChoice() {
        int numberOfResources = dumbModel.getPersonalBoard(nickname).getPosition()/2;
        Map<Resource, Integer> chosenResources = new HashMap<>();

        for(int i=0; i<numberOfResources; i++){
            String printableString = "\033[2;1HPick a resource (type the correspondent number):";
            printableString.concat("\n1) " + Resource.values()[0].label + " " + Constants.COIN);
            printableString.concat("\n2) " + Resource.values()[1].label + " " + Constants.SERVANT);
            printableString.concat("\n3) " + Resource.values()[2].label + " " + Constants.SHIELD);
            printableString.concat("\n4) " + Resource.values()[3].label + " " + Constants.STONE);

            printToCLI(printableString);

            int readIndex = in.nextInt();
            if(readIndex>0 && readIndex<=4)
                chosenResources.put(Resource.values()[readIndex], 1);
            else
                i--;
        }

        clientSocket.sendAction(new PregameResourceChoiceAction(chosenResources));
    }

    @Override
    public void renderNotification(String message) {
        String printableString = Constants.ANSI_CLEAR + "\033[2;1H";
        printableString.concat(message);
        printToCLI(printableString);
    }

    @Override
    public void renderErrorMessage(String message) {
        String printableString = Constants.ANSI_CLEAR + "\033[2;1H";
        printableString.concat(message);
        printToCLI(printableString);
    }

    @Override
    public void renderAuthenticationRequest(String message) {
        String printableString = Constants.ANSI_CLEAR + "\033[2;1H";
        printableString.concat(message);
        printToCLI(printableString);

        nickname = in.nextLine();
        clientSocket.sendMessage(new AuthenticationMessage(this.nickname, -1)); //todo: implement file save of gameID
    }

    @Override
    public void renderCreateLobbyRequest(String message) {
        String printableString = Constants.ANSI_CLEAR + "\033[2;1H";
        printableString.concat(message);
        printToCLI(printableString);

        int size = in.nextInt();

        if(size>0 && size<=4)
            clientSocket.sendMessage(new CreateLobbyMessage(size));
        else
            renderCreateLobbyRequest(message);
    }

    @Override
    public DumbModel getDumbModel() {
        return dumbModel;
    }

    @Override
    public OnScreenElement getOnScreenElement() {
        return onScreenElement;
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

    /**
     * @param marbles the map of marbles which will be converted
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string representing a map of marbles formatted with the top left corner in position x,y
     */
    public String formatPrintableMarblesMapStringAt(Map<MarketMarble, Integer> marbles, int x, int y) {
        String printableString = "";

        List<MarketMarble> marblesList = new ArrayList<>();
        marbles.forEach((key, value) -> IntStream.range(0, value).forEach(
                $ -> marblesList.add(key)
        ));

        IntStream.range(0, marblesList.size()).forEach(
                i -> {
                    printableString.concat("\033[" + x + ";" + (y+i*6) + "H╔═══╗ ");
                    printableString.concat("\033[" + (x+1) + ";" + (y+i*6) + "H║ "+ marblesList.get(i).toString() +" ║ ");
                    printableString.concat("\033[" + x + ";" + (y+i*6) + "H╚═══╝ ");
                }
        );

        return printableString;
    }
}