package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.CommandExecutor;
import it.polimi.ingsw.client.utils.constants.Commands;
import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.client.utils.exceptions.*;
import it.polimi.ingsw.network.clienttoserver.messages.AuthenticationMessage;
import it.polimi.ingsw.network.clienttoserver.messages.CreateLobbyMessage;
import it.polimi.ingsw.network.servertoclient.renderable.Renderable;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.GameState;
import it.polimi.ingsw.server.model.utils.Resource;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Flow;
import java.util.stream.IntStream;

/**
 * This is a cli client for master of renaissance
 */

public class CLI implements UI {
    private final Scanner in;
    private final PrintWriter out;
    private ClientSocket clientSocket;
    private String nickname;
    private Flow.Subscription subscription;
    private final DumbModel dumbModel;
    private OnScreenElement onScreenElement;
    private volatile boolean activeGame;
    private final CommandExecutor commandExecutor;
    private final ConcurrentLinkedQueue<Renderable> renderQueue;

    public CLI() {
        in = new Scanner(System.in);
        out = new PrintWriter(System.out);
        dumbModel = new DumbModel(this);
        commandExecutor = new CommandExecutor(dumbModel, clientSocket);
        renderQueue = new ConcurrentLinkedQueue<>();
    }

    public static void main(String[] args) {
        System.out.println(Constants.ANSI_CLEAR);
        System.out.println(Constants.MASTER_OF_RENAISSANCE);
        //System.out.println(Constants.AUTHORS);
        //System.out.println(Constants.RULES + "\n");
        CLI cli = new CLI();
        cli.connectToServer();
    }

    /**
     * This method asks the player which server and port he wants to connect to and creates the network socket
     */
    private void connectToServer(){
        //Ask for server IP
        printToCLI(">Insert the server IP address");
        resetCommandPosition();
        in.reset();
        String ip = in.next();

        //Ask for server port
        printToCLI(">Insert the server port");
        resetCommandPosition();
        in.reset();
        int port = in.nextInt();
        in.nextLine();

        //Ask for reconnection to existing game
        if(dumbModel.getGameID()!=-1) {
            printToCLI(">Unfinished game found, do you want to reconnect? (y or n)");
            resetCommandPosition();
            in.reset();
            String answer = in.next();
            if (answer.equals("n"))
                dumbModel.updateGameID(-1);
        }

        //Connect socket
        this.clientSocket = new ClientSocket(ip, port, dumbModel.getUpdatesHandler());
        clientSocket.connect();
        setOnScreenElement(OnScreenElement.COMMONS);
        commandExecutor.setClientSocket(clientSocket);
    }

    /**
     * Start running the cli client
     */
    private void loop() throws InterruptedException {
        while(isActiveGame()){
            resetCommandPosition();
            String insertedCommand = in.nextLine();

            try {
                if(!insertedCommand.equals("\n") && !insertedCommand.equals(""))
                    commandExecutor.executeCommand(insertedCommand);
            } catch (PersonalBoardException e) {
                renderPersonalBoard(e.getDumbPersonalBoard());
            } catch (CommonsException e) {
                renderCommons();
            } catch (WrongCommandException | InvalidArgsException e) {
                renderErrorMessage(e.getMessage());
            } catch (HelpException e) {
                renderHelp();
            } catch (CommandHelpException e) {
                renderCommandHelpException(e.getCommand());
            } catch (QuitException e) {
                setActiveGame(false);
            }

        }

        clientSocket.close();
    }

    private void renderCommandHelpException(Commands command) {

    }

    private void renderHelp() {
        String printableString = Constants.ANSI_CLEAR + "HELP";
        printToCLI(printableString);
    }

    @Override
    public void renderModelUpdate() {
        if(dumbModel.getGameState() == GameState.DEALT_LEADER_CARDS && dumbModel.getTempLeaderCards().size()>0)
            renderLeaderCardsChoice(dumbModel.getTempLeaderCards());
        else
            renderCommons();

        new Thread(() -> {
            setActiveGame(true);
            try {
                loop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void renderPersonalBoard(String nickname) {
        clearScreen();
        DumbPersonalBoard dumbPersonalBoard = dumbModel.getPersonalBoard(nickname);
        String printableString = dumbPersonalBoard.formatPrintableStringAt(2,2);
        printToCLI(printableString);
        resetCommandPosition();
        setOnScreenElement(OnScreenElement.valueOf(dumbPersonalBoard.getPosition()));
    }

    public void renderPersonalBoard(DumbPersonalBoard dumbPersonalBoard) {
        clearScreen();
        String printableString = dumbPersonalBoard.formatPrintableStringAt(2,2);
        printToCLI(printableString);
        resetCommandPosition();
        setOnScreenElement(OnScreenElement.valueOf(dumbPersonalBoard.getPosition()));
    }

    @Override
    public void renderCommons() {
        clearScreen();
        String printableString = dumbModel.getDevelopmentCardsBoard().formatPrintableStringAt(2, 2) +
                dumbModel.getMarket().formatPrintableStringAt(15, 72);
        printToCLI(printableString);
        resetCommandPosition();
        setOnScreenElement(OnScreenElement.COMMONS);
    }

    @Override
    public void renderActivateLeaderCardConfirmation() {

    }

    @Override
    public void renderActivateProductionConfirmation() {

    }

    @Override
    public void renderBuyDevelopmentCardConfirmation() {

    }

    @Override
    public void renderDiscardLeaderCardConfirmation() {

    }

    @Override
    public void renderEndTurnConfirmation() {

    }

    @Override
    public void renderPregameLeaderCardsChoiceConfirmation() {

    }

    @Override
    public void renderPregameResourceChoiceConfirmation() {

    }

    @Override
    public void renderSelectMarblesConfirmation() {

    }

    @Override
    public void renderTakeFromMarketConfirmation() {

    }

    @Override
    public void renderActionToken(ActionToken actionToken) {
        printToCLI(actionToken.getMessage());
        resetCommandPosition();
    }

    @Override
    public void renderGameOutcome(int finalScore) {
        clearScreen();
        String printableString = Constants.ANSI_BOLD + "\033[2;1HYour final score is: " + finalScore + " points";
        printToCLI(printableString);
    }

    @Override
    public void renderGameOutcome(TreeMap<Integer, String> finalScores) {
        clearScreen();
        StringBuilder printableString = new StringBuilder(Constants.ANSI_BOLD + "\033[2;1HThe final scores are:");
        finalScores.forEach(
                (integer, s) -> printableString.append("\n" + integer + ") " + s)
        );
        printToCLI(printableString.toString());
    }

    @Override
    public void renderLeaderCardsChoice(List<DumbLeaderCard> leaderCards) {
        clearScreen();
        setActiveGame(true);
        StringBuilder printableString = new StringBuilder();
        IntStream.range(0,leaderCards.size()).forEach(
                i -> printableString.append(leaderCards.get(i).formatPrintableStringAt(2, 1+17*i))
        );
        printToCLI(printableString.toString());
        resetCommandPosition();
    }

    @Override
    public void renderTempMarblesChoice(Map<MarketMarble, Integer> updateMarbles) {
        String printableString = formatPrintableMarblesMapStringAt(updateMarbles, 2 , 2 );
        printToCLI(printableString);
        resetCommandPosition();
    }

    @Override
    public void renderResourcePregameChoice() {
        int numberOfResources = dumbModel.getPersonalBoard(nickname).getPosition()/2;
        Map<Resource, Integer> chosenResources = new HashMap<>();
        String printableString;
        if(numberOfResources == 0 )
            return;
        if(numberOfResources <= 1)
            printableString = "\033[2;1H>Pick " + numberOfResources + " resource\n";
        else
            printableString = "\033[2;1H>Pick " + numberOfResources + " resources\n";
        printToCLI(printableString);
        resetCommandPosition();
    }

    @Override
    public void renderErrorMessage(String message) {
        String printableString = Constants.ANSI_CLEAR + "\033[2;1H>" + message;
        printToCLI(printableString);
        resetCommandPosition();
    }

    @Override
    public void renderAuthenticationRequest(String message) {
        printToCLI(">"+ message + "\n");

        resetCommandPosition();
        in.reset();
        nickname = in.next();
        dumbModel.setNickname(nickname);
        clientSocket.sendMessage(new AuthenticationMessage(this.nickname, dumbModel.getGameID()));
    }

    @Override
    public void renderCreateLobbyRequest(String message) {
        printToCLI(">"+ message + "\n");

        resetCommandPosition();
        int size = in.nextInt();
        in.nextLine();

        if(size>0 && size<=4)
            clientSocket.sendMessage(new CreateLobbyMessage(size));
        else
            renderCreateLobbyRequest(message);
    }

    @Override
    public void renderConnectionTerminatedNotification(String message) {
        clearScreen();
        String printableString = "\033[2;1H>"+ message + "\n";;
        printToCLI(printableString);
    }

    @Override
    public void renderGameFoundNotification(String message) {
        printToCLI(">"+ message + "\n");
    }

    @Override
    public void renderGameNotFoundNotification(String message) {
        printToCLI(">"+ message + "\n");
    }

    @Override
    public void renderGameStartedNotification(String message) {
        printToCLI(">"+ message + "\n");
    }

    @Override
    public void renderJoinedLobbyNotification(String message) {
        printToCLI(">"+ message + "\n");
    }

    @Override
    public void renderTimeoutWarningNotification(String message) {
        printToCLI(">"+ message + "\n");
    }

    @Override
    public void renderWaitingForPlayersNotification(String message) {
        printToCLI(">"+ message + "\n");
    }

    @Override
    public void renderWrongNicknameNotification(String message) {
        printToCLI(">"+ message + "\n");
    }

    @Override
    public void renderNicknameAlreadyInUseNotification(String message) {
        printToCLI(">"+ message + "\n");
    }

    @Override
    public void renderServerOffline() {
        printToCLI(">"+ Constants.OFFLINE_MESSAGE);
        in.close();
        out.close();
    }

    /**
     * Prints a formatted string to cli
     * @param printableString a string ready to be printed, formatted ascii escape codes
     */
    private void printToCLI(String printableString){
        out.print(printableString);
        out.flush();
    }

    /**
     * Reprints the insert command message in a fixed position
     */
    private void resetCommandPosition(){
        out.print(Constants.INPUT_MESSAGE);
        out.flush();
    }

    /**
     * @param marbles the map of marbles which will be converted
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string representing a map of marbles formatted with the top left corner in position x,y
     */
    public String formatPrintableMarblesMapStringAt(Map<MarketMarble, Integer> marbles, int x, int y) {
        StringBuilder printableString = new StringBuilder();

        List<MarketMarble> marblesList = new ArrayList<>();
        marbles.forEach((k, v) -> {
            for (int i = 0; i < v; i++) {
                marblesList.add(k);
            }
        });

        IntStream.range(0, marblesList.size()).forEach(
                i -> {
                    printableString.append("\033[" + x + ";" + (y+i*6) + "H╔═══╗ ");
                    printableString.append("\033[" + (x+1) + ";" + (y+i*6) + "H║ "+ marblesList.get(i).formatPrintableString() +" ║ ");
                    printableString.append("\033[" + x + ";" + (y+i*6) + "H╚═══╝ ");
                }
        );

        return printableString.toString();
    }

    public synchronized boolean isActiveGame() {
        return activeGame;
    }

    public synchronized void setActiveGame(boolean activeGame) {
        this.activeGame = activeGame;
    }

    /**
     * Method invoked prior to invoking any other Subscriber
     * methods for the given Subscription. If this method throws
     * an exception, resulting behavior is not guaranteed, but may
     * cause the Subscription not to be established or to be cancelled.
     *
     * <p>Typically, implementations of this method invoke {@code
     * subscription.request} to enable receiving items.
     *
     * @param subscription a new subscription
     */
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    /**
     * Method invoked with a Subscription's next item.  If this
     * method throws an exception, resulting behavior is not
     * guaranteed, but may cause the Subscription to be cancelled.
     *
     * @param item the item
     */
    @Override
    public void onNext(Renderable item) {
            item.render(this);
        subscription.request(1);
    }

    /**
     * Method invoked upon an unrecoverable error encountered by a
     * Publisher or Subscription, after which no other Subscriber
     * methods are invoked by the Subscription.  If this method
     * itself throws an exception, resulting behavior is
     * undefined.
     *
     * @param throwable the exception
     */
    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * Method invoked when it is known that no additional
     * Subscriber method invocations will occur for a Subscription
     * that is not already terminated by error, after which no
     * other Subscriber methods are invoked by the Subscription.
     * If this method throws an exception, resulting behavior is
     * undefined.
     */
    @Override
    public void onComplete() {

    }

    /**
     * This method call render() on the head of updatesQueue
     */
    private synchronized void elaborateRenderQueue(){
        Renderable usedItem = renderQueue.remove();
        usedItem.render(this);
    }

    private OnScreenElement getOnScreenElement() {
        return onScreenElement;
    }

    private synchronized void setOnScreenElement(OnScreenElement onScreenElement) {
        this.onScreenElement = onScreenElement;
    }

    private void clearScreen(){
        printToCLI(Constants.ANSI_CLEAR);
    }
}