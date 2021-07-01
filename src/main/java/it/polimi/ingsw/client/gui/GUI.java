package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.gui.guicontrollers.ingame.*;
import it.polimi.ingsw.client.gui.guicontrollers.mainmenu.MainMenuController;
import it.polimi.ingsw.client.utils.InputVerifier;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.DumbPersonalBoard;
import it.polimi.ingsw.network.servertoclient.renderable.Renderable;
import it.polimi.ingsw.server.model.actiontoken.ActionToken;
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

/**
 * JavaFX Application which acts as the backbone of the graphical user interface of the game.
 */
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
    private FinalScreenController finalScreenController;
    private Map<String, PlayerBoardController> oppsControllers;
    private Scene personalScene;
    private Scene commonsScene;
    private Map<String, Scene> oppsScenes;
    private Subscription subscription;
    private Scene finalScene;

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

    public InGamePersonalController getPersonalController() {
        return personalController;
    }

    /**
     * Loads the entry main menu from files.
     */
    public void loadMainMenu() throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(FXMLResources.MAIN_MENU.toPathString()));
        changeScene(new Scene(loader.load()));
        this.mainMenuController = loader.getController();
        this.mainMenuController.setGui(this);
    }

    /**
     * Changes the footer currently shown in the main menu.
     * @param footer resource to be shown to screen.
     */
    private void updateMainMenuFooter(FXMLResources footer) {
        Platform.runLater(() -> mainMenuController.setFooter(footer));
    }

    /**
     * Updates the text in the main menu loading screen.
     * @param message text that will be rendered under the loading spinning animation.
     */
    private void updateMainMenuLoadingText(String message) {
        Platform.runLater(() -> mainMenuController.setLoadingFooterText(message));

    }

    /**
     * Removes the main menu from the GUI application.
     */
    public void tearDownMainMenu() {
        mainMenuController = null;
    }

    /**
     * Loads personal, common and opponents information about the game. Sets up controllers
     * and scenes that will be used during the actual game.
     */
    public void loadGame() {
        this.oppsScenes = new HashMap<>();
        this.oppsControllers = new HashMap<>();
        FXMLLoader commonsLoader =
                new FXMLLoader(getClass().getResource(FXMLResources.IN_GAME_COMMONS.toPathString()));
        FXMLLoader finalLoader =
                new FXMLLoader(getClass().getResource(FXMLResources.FINAL_SCREEN.toPathString()));
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
            finalScene = new Scene(finalLoader.load());
            for (String nickname :
                    oppsLoaders.keySet()) {
                oppsScenes.put(nickname, new Scene(oppsLoaders.get(nickname).load()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Initialize Personal Controller
        personalController = personalLoader.getController();
        personalController.initialize(this);
        // Initialize Opponents Controllers
        oppsLoaders.forEach((nickname, loader) -> {
            InGameOppController controller = loader.getController();
            oppsControllers.put(nickname, controller);
            controller.initialize(this, nickname);
        });
        // Initialize Commons Controller
        commonsController = commonsLoader.getController();
        commonsController.initialize(this);
        //Initialize Final Controller
        finalScreenController = finalLoader.getController();
        finalScreenController.initialize(this);
        goToPersonalView();
        tearDownMainMenu();
    }

    public void tearDownGame() {
    }

    /**
     * Switches the view to commons.
     */
    public void goToCommonsView() {
        changeScene(commonsScene);
    }

    /**
     * Switches the view to an opponent's board.
     * @param nickname opponent identifier.
     */
    public void goToOppView(String nickname) {
        changeScene(oppsScenes.get(nickname));
    }

    /**
     * Switches the view to the player's board.
     */
    public void goToPersonalView() {
        changeScene(personalScene);
    }

    /**
     * Switches the view to the final scene.
     */
    public void goToFinalView() {
        changeScene(finalScene);
    }

    /**
     * Sets the passed parameter scene as the currently displayed scene.
     */
    private void changeScene(Scene scene) {
        Platform.runLater(() -> stage.setScene(scene));
    }

    /**
     * Renders the first update received from the server containing all of the
     * game's information.
     */
    @Override
    public void renderModelUpdate() {
        loadGame();
        if (dumbModel.getGameState() == GameState.DEALT_LEADER_CARDS && dumbModel.getTempLeaderCards().size() > 0)
            renderLeaderCardsChoice(dumbModel.getTempLeaderCards());
        else {
            dumbModel.getPersonalBoards().stream().map(DumbPersonalBoard::getNickname).forEach(this::renderPersonalBoard);
        }
    }

    /**
     * Renders a player's personal board.
     * @param nickname player identifier.
     */
    @Override
    public void renderPersonalBoard(String nickname) {
        if (nickname.equals(personalNickname)) {
            personalController.renderPersonalBoard(getDumbModel().getOwnPersonalBoard());
        } else {
            oppsControllers.get(nickname).renderPersonalBoard(dumbModel.getPersonalBoard(nickname));
        }
    }

    /**
     * Renders the commons scene.
     */
    @Override
    public void renderCommons() {
        commonsController.renderCommonsBoard();
    }

    @Override
    public void renderActivateLeaderCardConfirmation() {
        personalController.endActivateLeaderCard();
    }

    @Override
    public void renderActivateProductionConfirmation() {
        personalController.endProductionChoices();
        personalController.populateInfoLabel("Production completed successfully!");
    }

    @Override
    public void renderBuyDevelopmentCardConfirmation() {
        personalController.endDevelopmentCardPurchase();
    }

    @Override
    public void renderDiscardLeaderCardConfirmation() {
        personalController.endDiscardLeaderCard();
    }

    @Override
    public void renderEndTurnConfirmation() {
        personalController.populateInfoLabel("Your turn has finished!");
    }

    @Override
    public void renderPregameLeaderCardsChoiceConfirmation() {
        personalController.endLeaderCardsChoice();
    }

    @Override
    public void renderPregameResourceChoiceConfirmation() {
        personalController.endPreGameResourceChoice();
    }

    @Override
    public void renderSelectMarblesConfirmation() {
        personalController.endTempMarblesChoice();
    }

    @Override
    public void renderTakeFromMarketConfirmation() {
    }

    @Override
    public void renderActionToken(ActionToken actionToken) {
        personalController.populateInfoLabel("Lorenzo played: " + actionToken);
    }

    @Override
    public void renderGameOutcome(int finalScore) {
        finalScreenController.renderFinal(null, finalScore);
        goToFinalView();
    }

    @Override
    public void renderGameOutcome(TreeMap<Integer, String> finalScores) {
        finalScreenController.renderFinal(finalScores, -1);
        goToFinalView();
    }

    @Override
    public void renderLeaderCardsChoice(List<DumbLeaderCard> leaderCards) {
        personalController.startLeaderCardsChoice(leaderCards);
    }

    @Override
    public void renderTempMarblesChoice(Map<MarketMarble, Integer> updateMarbles) {
        personalController.startTempMarblesChoice(updateMarbles);
    }

    @Override
    public void renderResourcePregameChoice() {
        personalController.startPreGameResourceChoice();
    }

    @Override
    public void renderErrorMessage(String message) {
        personalController.populateInfoLabel(message);
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

    @Override
    public void renderServerOffline() {

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
        logger.info(item.getClass().getSimpleName());
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
