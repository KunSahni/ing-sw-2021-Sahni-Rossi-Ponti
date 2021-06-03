package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.gui.guicontrollers.JFXController;
import it.polimi.ingsw.client.gui.guicontrollers.MainMenuController;
import it.polimi.ingsw.client.utils.dumbobjects.DumbActionTokenDeck;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.network.message.renderable.Renderable;
import it.polimi.ingsw.server.model.market.MarketMarble;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Flow.*;

public class GUI extends Application implements UI {
    private DumbModel dumbModel;
    private ClientSocket clientSocket;
    private Stage stage;
    private JFXController currentController;
    private Subscription subscription;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.dumbModel = new DumbModel(this);
        enterMainMenu();
        this.stage.show();
    }

    public void setClientSocket(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    public void enterMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLResources.MAIN_MENU.toPathString()));
        stage.setScene(new Scene(loader.load()));
        currentController = loader.getController();
        currentController.setGui(this);
    }

    private void updateMainMenuFooter(FXMLResources footer) {
        Platform.runLater(() -> ((MainMenuController) currentController).setFooter(footer));
    }

    private void updateMainMenuLoadingText(String message) {
        ((MainMenuController) currentController).setLoadingFooterText(message);
    }

    public void exitMainMenu() {

    }

    public void enterGame() {}

    public void exitGame() {}

    @Override
    public void renderPersonalBoard(String nickname) {

    }

    @Override
    public void renderCommons() {

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

    }

    @Override
    public void renderTempMarblesChoice(Map<MarketMarble, Integer> updateMarbles) {

    }

    @Override
    public void renderResourcePregameChoice() {

    }

    @Override
    public void renderErrorMessage(String message) {

    }

    @Override
    public void renderAuthenticationRequest(String message) {
        updateMainMenuFooter(FXMLResources.NICKNAME_FOOTER);
    }

    @Override
    public void renderCreateLobbyRequest(String message) {
        updateMainMenuFooter(FXMLResources.PLAYER_SELECTION_FOOTER);
    }

    @Override
    public void renderConnectionTerminatedNotification(String message) {

    }

    @Override
    public void renderGameFoundNotification(String message) {

    }

    @Override
    public void renderGameNotFoundNotification(String message) {

    }

    @Override
    public void renderGameStartedNotification(String message) {
        updateMainMenuLoadingText(message);
        exitMainMenu();
    }

    @Override
    public void renderJoinedLobbyNotification(String message) {
        updateMainMenuLoadingText(message);
    }

    @Override
    public void renderTimeoutWarningNotification(String message) {

    }

    @Override
    public void renderWaitingForPlayersNotification(String message) {
        updateMainMenuLoadingText(message);
    }

    @Override
    public void renderWrongNicknameNotification(String message) {

    }

    @Override
    public void renderNicknameAlreadyInUseNotification(String message) {
        updateMainMenuLoadingText(message);
        updateMainMenuFooter(FXMLResources.NICKNAME_FOOTER);
    }

    public DumbModel getDumbModel() {
        return dumbModel;
    }


    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Renderable item) {
        item.render(this);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {

    }
}
