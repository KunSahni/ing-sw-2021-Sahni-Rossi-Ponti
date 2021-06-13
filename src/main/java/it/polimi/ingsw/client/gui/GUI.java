package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.gui.guicontrollers.ingame.InGameCommonsController;
import it.polimi.ingsw.client.gui.guicontrollers.JFXController;
import it.polimi.ingsw.client.gui.guicontrollers.ingame.InGamePersonalController;
import it.polimi.ingsw.client.gui.guicontrollers.mainmenu.MainMenuController;
import it.polimi.ingsw.client.utils.InputVerifier;
import it.polimi.ingsw.client.utils.dumbobjects.DumbActionTokenDeck;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.DumbPersonalBoard;
import it.polimi.ingsw.network.servertoclient.renderable.Renderable;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.GameState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Flow.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GUI extends Application implements UI {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());
    private DumbModel dumbModel;
    private String personalNickname;
    private InputVerifier inputVerifier;
    private ClientSocket clientSocket;
    private Stage stage;
    private MainMenuController mainMenuController;
    private InGamePersonalController personalController;
    private InGameCommonsController commonsController;
    private Map<String, JFXController> oppsControllers;
    private Scene personalScene;
    private Scene commonsScene;
    private Map<String, Scene> oppsScenes;
    private Subscription subscription;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.stage.setResizable(false);
        this.dumbModel = new DumbModel(this);
        this.inputVerifier = new InputVerifier(dumbModel);
        loadMainMenu();
        this.stage.show();
    }

    public InputVerifier getInputVerifier() {
        return inputVerifier;
    }

    public void setClientSocket(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    public void setPersonalNickname(String personalNickname) {
        this.personalNickname = personalNickname;
        this.dumbModel.setNickname(personalNickname);
    }

    public String getPersonalNickname() {
        return personalNickname;
    }

    public void loadMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLResources.MAIN_MENU.toPathString()));
        changeScene(new Scene(loader.load()));
        this.mainMenuController = loader.getController();
        this.mainMenuController.setGui(this);
    }

    private void updateMainMenuFooter(FXMLResources footer) {
        Platform.runLater(() -> mainMenuController.setFooter(footer));
    }

    private void updateMainMenuLoadingText(String message) {
        Platform.runLater(() -> mainMenuController.setLoadingFooterText(message));

    }

    public void tearDownMainMenu() {
        mainMenuController = null;
    }

    public void loadGame() {
        this.oppsScenes = new HashMap<>();
        this.oppsControllers = new HashMap<>();
        FXMLLoader commonsLoader =
                new FXMLLoader(getClass().getResource(FXMLResources.IN_GAME_COMMONS.toPathString()));
        FXMLLoader personalLoader =
                new FXMLLoader(getClass().getResource(FXMLResources.IN_GAME_PERSONAL.toPathString()));
        // Create a map that links each opponent player to an fxml loader
        Map<String, FXMLLoader> oppsLoaders = new HashMap<>();
        dumbModel.getPersonalBoards().stream()
                .map(DumbPersonalBoard::getNickname)
                .filter(nickname -> !nickname.equals(dumbModel.getNickname()))
                .forEach(nickname -> oppsLoaders.put(nickname,
                        new FXMLLoader(getClass().getResource(FXMLResources.IN_GAME_OPP.toPathString()))));
        // Load all scenes
        try {
            commonsScene = new Scene(commonsLoader.load());
            personalScene = new Scene(personalLoader.load());
            for (String nickname :
                    oppsLoaders.keySet()) {
                oppsScenes.put(nickname, new Scene(oppsLoaders.get(nickname).load()));
                // Use this loop to load controllers as well
                JFXController oppController = oppsLoaders.get(nickname).getController();
                oppController.setGui(this);
                oppsControllers.put(nickname, oppController);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        personalController = personalLoader.getController();
        personalController.setGui(this);
        personalController.renderFaithTrack(dumbModel.getOwnPersonalBoard());
        personalController.initVisitMenu(dumbModel.getPersonalBoards().stream()
                .map(DumbPersonalBoard::getNickname)
                .filter(nick -> !nick.equals(personalNickname))
                .collect(Collectors.toList()));
        commonsController = commonsLoader.getController();
        commonsController.setGui(this);
        goToPersonalView();
        tearDownMainMenu();
    }

    public void tearDownGame() {}

    public void goToCommonsView() {
        changeScene(commonsScene);
    }

    public void goToOppView(String nickname) {
        changeScene(oppsScenes.get(nickname));
    }

    public void goToPersonalView() {
        changeScene(personalScene);
    }

    private void changeScene(Scene scene) {
        Platform.runLater(() -> stage.setScene(scene));
    }

    @Override
    public void renderModelUpdate() {
        loadGame();
        if(dumbModel.getGameState() == GameState.DEALT_LEADER_CARDS && dumbModel.getTempLeaderCards().size()>0)
            renderLeaderCardsChoice(dumbModel.getTempLeaderCards());
    }

    @Override
    public void renderPersonalBoard(String nickname) {
        if (nickname.equals(personalNickname)) {
            personalController.renderPersonalBoard();
        }
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
        personalController.initLeaderCardsSelection(leaderCards);
    }

    @Override
    public void renderTempMarblesChoice(Map<MarketMarble, Integer> updateMarbles) {

    }

    @Override
    public void renderResourcePregameChoice() {
        personalController.initResourceChoice();
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
