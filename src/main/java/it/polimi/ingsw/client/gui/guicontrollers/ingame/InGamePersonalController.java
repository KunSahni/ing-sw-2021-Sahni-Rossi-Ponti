package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.*;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.market.MarketMarble;
import it.polimi.ingsw.server.model.utils.ProductionCombo;
import it.polimi.ingsw.server.model.utils.Resource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static javafx.scene.paint.Color.WHITE;

/**
 * JavaFX controller for the personal playing Scene.
 */
public class InGamePersonalController extends PlayerBoardController {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private boolean isTempMarblesChoiceInitialized = false;
    @FXML
    private HBox leaderCardsHBox;
    @FXML
    private Button confirmButton;
    @FXML
    private Button resetButton;
    @FXML
    private VBox confirmResetLayer;
    @FXML
    private VBox actionsLayer;
    @FXML
    private VBox alertsVBox;
    @FXML
    private VBox personalBoardLayer;
    @FXML
    private VBox preGameLeaderCardSelectionLayer;
    @FXML
    private VBox pregameResourceChoiceLayer;
    @FXML
    private TilePane visitLayer;
    @FXML
    private HBox selectMarblesLayer;
    @FXML
    private VBox resourceSelectionLayer;
    @FXML
    private TilePane resourceSelectionSlidersTilePane;
    @FXML
    private Text resourceSelectionText;
    private final ObservableSet<ToggleButton> selectedPreGameLeaderCardToggleButtons =
            FXCollections.observableSet();
    private final ObservableSet<ToggleButton> selectedTempMarblesToggleButtons =
            FXCollections.observableSet();
    @FXML
    private Button coinButton;
    @FXML
    private Button stoneButton;
    @FXML
    private Button shieldButton;
    @FXML
    private Button servantButton;
    private final Map<Resource, Integer> chosenResourcesMap = new HashMap<>();
    @FXML
    private ToggleGroup selectDevCardSlotToggleGroup;
    private final ToggleGroup leaderCardActionToggleGroup = new ToggleGroup();
    private ObservableSet<ToggleButton> selectedProduceLeaderCardsToggleButtons;
    private ObservableSet<ToggleButton> selectedDevelopmentCardsToggleButtons;
    @FXML
    private ToggleButton defaultSlotToggleButton;
    @FXML
    private Text sequentialResourceSelectionText;
    @FXML
    private VBox outputResourcesSelectionLayer;
    @FXML
    private TilePane outputResourcesTilePane;
    @FXML
    private BorderPane defaultSlotOutputTile;
    @FXML
    private ToggleButton defaultSlotCoinButton;
    @FXML
    private ToggleGroup defaultSlotOutputToggleGroup;
    @FXML
    private ToggleButton defaultSlotServantButton;
    @FXML
    private ToggleButton defaultSlotShieldButton;
    @FXML
    private ToggleButton defaultSlotStoneButton;
    @FXML
    private BorderPane leaderCard1OutputTile;
    @FXML
    private ToggleButton leaderCard1CoinButton;
    @FXML
    private ToggleGroup leaderCard1OutputToggleGroup;
    @FXML
    private ToggleButton leaderCard1ServantButton;
    @FXML
    private ToggleButton leaderCard1ShieldButton;
    @FXML
    private ToggleButton leaderCard1StoneButton;
    @FXML
    private BorderPane leaderCard2OutputTile;
    @FXML
    private ToggleButton leaderCard2CoinButton;
    @FXML
    private ToggleGroup leaderCard2OutputToggleGroup;
    @FXML
    private ToggleButton leaderCard2ServantButton;
    @FXML
    private ToggleButton leaderCard2ShieldButton;
    @FXML
    private ToggleButton leaderCard2StoneButton;
    private ImageView blackCross;

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        // Initialize Resource-choosing buttons
        coinButton.setOnAction(e -> addResourceToChoiceMap(Resource.COIN));
        servantButton.setOnAction(e -> addResourceToChoiceMap(Resource.SERVANT));
        shieldButton.setOnAction(e -> addResourceToChoiceMap(Resource.SHIELD));
        stoneButton.setOnAction(e -> addResourceToChoiceMap(Resource.STONE));
        // Initialize Dev Card slot selection buttons
        IntStream.range(0, 3).forEach(i -> ((ToggleButton) selectDevCardSlotToggleGroup.getToggles().get(i)).setVisible(false));
        // Initialize resources in output toggle buttons
        defaultSlotCoinButton.setUserData(Resource.COIN);
        defaultSlotShieldButton.setUserData(Resource.SHIELD);
        defaultSlotStoneButton.setUserData(Resource.STONE);
        defaultSlotServantButton.setUserData(Resource.SERVANT);
        leaderCard1CoinButton.setUserData(Resource.COIN);
        leaderCard1ShieldButton.setUserData(Resource.SHIELD);
        leaderCard1StoneButton.setUserData(Resource.STONE);
        leaderCard1ServantButton.setUserData(Resource.SERVANT);
        leaderCard2CoinButton.setUserData(Resource.COIN);
        leaderCard2ShieldButton.setUserData(Resource.SHIELD);
        leaderCard2StoneButton.setUserData(Resource.STONE);
        leaderCard2ServantButton.setUserData(Resource.SERVANT);
        // Remove Output tiles from parent node (will be added on need)
        outputResourcesTilePane.getChildren().remove(defaultSlotOutputTile);
        outputResourcesTilePane.getChildren().remove(leaderCard1OutputTile);
        outputResourcesTilePane.getChildren().remove(leaderCard2OutputTile);
        // Initialize black cross
        blackCross = new ImageView(getImageFromPath("/img/faithtrack/black_cross.png"));
        blackCross.setFitHeight(55);
        blackCross.setPreserveRatio(true);
    }

    /**
     * Renders the player's nickname and initializes the Visit menu.
     * @param gui GUI Object saved to obtain references to DumbModel,
     *            ClientSocket and InputVerifier.
     */
    public void initialize(GUI gui) {
        super.setGui(gui);
        super.setNicknameLabel(gui.getPersonalNickname());
        initVisitMenu(gui.getDumbModel().getPersonalBoards().stream()
                .map(DumbPersonalBoard::getNickname)
                .filter(nick -> !nick.equals(gui.getPersonalNickname()))
                .collect(Collectors.toList()));
    }

    /**
     * Adds buttons to visit all opponents' boards to the Visit menu.
     * @param oppsNicknames List of opponents' nicknames.
     */
    private void initVisitMenu(List<String> oppsNicknames) {
        Button commonsButton = new Button("Commons");
        commonsButton.setOnAction(e -> {
            gui.goToCommonsView();
            bringToFront(actionsLayer, personalBoardLayer);
        });
        visitLayer.getChildren().add(commonsButton);
        oppsNicknames.forEach(oppNickname -> {
            Button oppButton = new Button(oppNickname);
            oppButton.setOnAction(e -> {
                gui.goToOppView(oppNickname);
                bringToFront(actionsLayer, personalBoardLayer);
            });
            visitLayer.getChildren().add(oppButton);
        });
    }

    /**
     * FXML method called by the Confirm button. Depending on the current state of the button's
     * UserData a certain choice is confirmed.
     */
    @FXML
    private void confirm() {
        switch ((ConfirmResetButtonsStrategy) confirmButton.getUserData()) {
            case PRE_GAME_LEADER_CARDS_CHOICE -> pickLeaderCards();
            case PRE_GAME_RESOURCES_CHOICE -> confirmPreGameResourceChoice();
            case SELECT_MARBLES -> confirmMarbleSelection();
            case SELECT_DEVELOPMENT_SLOT -> confirmDevelopmentSlotSelection();
            case SELECT_RESOURCES_TO_BUY_DEVELOPMENT_CARD -> confirmDevelopmentCardPurchase();
            case DISCARD_LEADER_CARD -> confirmDiscardLeaderCard();
            case ACTIVATE_LEADER_CARD -> confirmActivateLeaderCard();
            case SELECT_PRODUCTION_BUTTONS -> confirmProductionButtonsSelection();
            case SELECT_PRODUCTION_INPUT_RESOURCES -> confirmProductionInputResources();
            case SELECT_PRODUCTION_OUTPUT_RESOURCES -> confirmProduction();
        }
    }

    /**
     * FXML method called by the Reset button. Depending on the current state of the button's
     * UserData a certain choice is reset or cancelled.
     */
    @FXML
    private void reset() {
        switch ((ConfirmResetButtonsStrategy) resetButton.getUserData()) {
            case PRE_GAME_RESOURCES_CHOICE -> {
                chosenResourcesMap.clear();
                populateInfoLabel("Resource choice cleared.");
            }
            case VISIT-> bringToFront(actionsLayer, personalBoardLayer);
            case SELECT_RESOURCES_TO_BUY_DEVELOPMENT_CARD, SELECT_PRODUCTION_INPUT_RESOURCES -> cancelResourceChoice();
            case DISCARD_LEADER_CARD, ACTIVATE_LEADER_CARD -> cancelLeaderCardAction();
            case SELECT_PRODUCTION_OUTPUT_RESOURCES -> endProductionChoices();
            case SELECT_PRODUCTION_BUTTONS -> endProductionButtonsSelection();
            case SELECT_DEVELOPMENT_SLOT -> cancelDevelopmentSlotSelection();
        }
    }

    /**
     * Gives the user the choice to visit the commons board or any other players'
     * board.
     */
    @FXML
    private void openVisitMenu() {
        ConfirmResetButtonsStrategy.VISIT.applyTo(confirmButton, resetButton);
        bringToFront(confirmResetLayer, visitLayer);
    }

    /**
     * Queries the InputVerifier and, if granted, the server with a request to end
     * the user's turn.
     */
    @FXML
    private void endTurn() {
        if (!gui.getInputVerifier().canEndTurn()) {
            populateInfoLabel("You cannot end your turn at this time.");
        } else {
            gui.getClientSocket().sendAction(new EndTurnAction());
        }
    }

    /**
     * Displays a temporary pop up message to communicate information to the user.
     * @param outcome Displayed text.
     */
    public void populateInfoLabel(String outcome) {
        BorderPane alertPane = new BorderPane();
        alertPane.setPrefWidth(190);
        alertPane.setMinWidth(Region.USE_PREF_SIZE);
        alertPane.setMaxWidth(Region.USE_PREF_SIZE);
        alertPane.getStyleClass().add("alert");
        alertPane.setPadding(new Insets(0, 0, 10, 0));
        Text outcomeText = new Text(outcome);
        outcomeText.setWrappingWidth(170);
        Button closeAlertButton = new Button("x");
        closeAlertButton.setAlignment(Pos.TOP_RIGHT);
        alertPane.setTop(closeAlertButton);
        alertPane.setCenter(outcomeText);
        closeAlertButton.setOnAction(e -> {
            alertsVBox.getChildren().remove(alertPane);
            logger.info("Clicked close alert");
        });
        Timeline animation = new Timeline(new KeyFrame(Duration.seconds(0),
                e -> alertsVBox.getChildren().add(alertPane)));
        animation.getKeyFrames().add(new KeyFrame(Duration.seconds(3),
                e -> alertsVBox.getChildren().remove(alertPane)));
        animation.play();
    }

    /**
     * Initializes the two leader cards buttons, which are not displayed up to this
     * method's call. Should only be called once.
     * @param dumbPersonalBoard DumbModel element containing the required information
     *                          that will be rendered to screen.
     */
    private void initLeaderCardsDisplay(DumbPersonalBoard dumbPersonalBoard) {
        // Only called once, after the server has confirmed the player's initial
        // leader cards choice
        List<DumbLeaderCard> cards = dumbPersonalBoard.getLeaderCards();
        cards.forEach(card -> leaderCardsHBox.getChildren().add(createLeaderCardNode(card)));
        preGameLeaderCardSelectionLayer.getChildren().clear();
    }

    /**
     * Creates a ToggleButton as display node for the passed DumbLeaderCard. If the card
     * has ability type of Store, the method will initialize JavaFX elements to display
     * the stored resources.
     * @param card rendered DumbLeaderCard
     * @return Node to be placed on the view.
     */
    private ToggleButton createLeaderCardNode(DumbLeaderCard card) {
        ToggleButton button = new ToggleButton();
        button.setUserData(card);
        button.getStyleClass().add(card.isActive() ? "active-leader-card" : "inactive-leader-card");
        ImageView image = new ImageView(getImageFromPath(card.toImgPath()));
        image.setFitWidth(135);
        image.setPreserveRatio(true);
        if (card.getAbility().equals(LeaderCardAbility.STORE)) {
            DumbStoreLeaderCard storeLeaderCard = (DumbStoreLeaderCard) card;
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(image);
            HBox resourcesHBox = new HBox();
            resourcesHBox.setSpacing(7);
            resourcesHBox.setAlignment(Pos.BOTTOM_CENTER);
            IntStream.range(0, 2).forEach(i -> {
                ImageView resourceImage =
                        new ImageView(getImageFromPath(storeLeaderCard.getStoredType().toImgPath()));
                resourceImage.setFitWidth(35);
                resourceImage.setPreserveRatio(true);
                resourceImage.setVisible(false);
                // resourceImage.setVisible(false);
                resourcesHBox.getChildren().add(resourceImage);
            });
            stackPane.getChildren().add(resourcesHBox);
            button.setGraphic(stackPane);
        } else {
            button.setGraphic(image);
        }
        return button;
    }

    /**
     * Renders to screen the current state of the players' DumbLeaderCards.
     * @param dumbPersonalBoard DumbModel element containing the required information
     *                          that will be rendered to screen.
     */
    @Override
    protected void renderLeaderCards(DumbPersonalBoard dumbPersonalBoard) {
        Platform.runLater(() -> {
            synchronized (leaderCardsHBox) {
                if (!areLeaderCardsInitialized) {
                    areLeaderCardsInitialized = true;
                    initLeaderCardsDisplay(dumbPersonalBoard);
                }
                leaderCardsHBox.getChildren().stream().map(child -> (ToggleButton) child)
                        .filter(ToggleButton::isVisible)
                        .forEach(leaderCardButton -> {
                            DumbLeaderCard cardInButton =
                                    (DumbLeaderCard) leaderCardButton.getUserData();
                            Optional<DumbLeaderCard> cardInBoardOptional = dumbPersonalBoard
                                    .getLeaderCards()
                                    .stream()
                                    .filter(card -> card.toImgPath().equals(cardInButton.toImgPath()))
                                    .findAny();
                            cardInBoardOptional.ifPresentOrElse(cardInBoard -> {
                                        if (cardInBoard.isActive()) {
                                            if (!cardInButton.isActive()) {
                                                leaderCardButton.getStyleClass().add("active-leader-card");
                                                leaderCardButton.getStyleClass().remove("inactive-leader" +
                                                        "-card");
                                                leaderCardButton.setUserData(cardInBoard);
                                            }
                                            if (cardInBoard.getAbility().equals(LeaderCardAbility.STORE)) {
                                                // Update Storage card every time to keep track of
                                                // resource changes
                                                leaderCardButton.setUserData(cardInBoard);
                                                IntStream.range(0, 2).forEach(i -> {
                                                    HBox resourcesHBox =
                                                            (HBox) ((StackPane) leaderCardButton.getGraphic()).getChildren().get(1);
                                                    resourcesHBox.getChildren().get(i)
                                                            .setVisible(((DumbStoreLeaderCard) cardInBoard).getResourceCount() > i);
                                                });
                                            }
                                        }
                                    },
                                    () -> leaderCardButton.setVisible(false));
                        });
            }
        });
    }

    /**
     * Renders to screen the current state of the players' FaithTrack.
     * @param dumbPersonalBoard DumbModel element containing the required information
     *                          that will be rendered to screen.
     */
    @Override
    protected void renderFaithTrack(DumbPersonalBoard dumbPersonalBoard) {
        DumbFaithTrack faithTrack = dumbPersonalBoard.getFaithTrack();
        GridCoordinates faithMarkerCoordinates =
                faithTrackCoordsReference.get(faithTrack.getFaithMarkerPosition());
        Platform.runLater(() -> {
            faithTrackGrid.getChildren().clear();
            if (faithTrack instanceof DumbSinglePlayerFaithTrack) {
                DumbSinglePlayerFaithTrack dumbSinglePlayerFaithTrack = (DumbSinglePlayerFaithTrack) faithTrack;
                GridCoordinates blackCrossCoordinates =
                        faithTrackCoordsReference.get(dumbSinglePlayerFaithTrack.getBlackCrossPosition());
                faithTrackGrid.add(blackCross, blackCrossCoordinates.getColumn(),
                        blackCrossCoordinates.getRow());
            }
            faithTrackGrid.add(faithMarker, faithMarkerCoordinates.getColumn(), faithMarkerCoordinates.getRow());
            renderPopesFavor(firstPopesFavor, faithTrack.getPopesFavors().get(0), 1);
            renderPopesFavor(secondPopesFavor, faithTrack.getPopesFavors().get(1), 2);
            renderPopesFavor(thirdPopesFavor, faithTrack.getPopesFavors().get(2), 3);
        });
    }

    /**
     * Initializes the rendering of the pre-game leader cards choice.
     * @param leaderCards List of four DumbLeaderCards that will be shown to the player
     *                    as alternatives.
     */
    public void startLeaderCardsChoice(List<DumbLeaderCard> leaderCards) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        leaderCards.forEach(card -> hBox.getChildren().add(createPreGameLeaderCardToggleButton(card)));
        Platform.runLater(() -> preGameLeaderCardSelectionLayer.getChildren().add(hBox));
        ConfirmResetButtonsStrategy.PRE_GAME_LEADER_CARDS_CHOICE.applyTo(confirmButton,
                resetButton);
        bringToFront(confirmResetLayer, preGameLeaderCardSelectionLayer);
    }

    /**
     * Creates a ToggleButton that will be shown, next to three others, during the pre-game
     * DumbLeaderCards choice. It displays the image of the passed card.
     * @param card DumbLeaderCard alternative in the pre-game choice.
     * @return JavaFX node that will be displayed to screen.
     */
    private ToggleButton createPreGameLeaderCardToggleButton(DumbLeaderCard card) {
        ToggleButton button = new ToggleButton();
        button.setUserData(card);
        ImageView imageView = new ImageView(getImageFromPath(card.toImgPath()));
        imageView.setFitWidth(170);
        imageView.setPreserveRatio(true);
        button.setGraphic(imageView);
        button.selectedProperty().addListener((observable, oldValue, selectedNow) -> {
            if (selectedNow) {
                selectedPreGameLeaderCardToggleButtons.add(button);
            } else {
                selectedPreGameLeaderCardToggleButtons.remove(button);
            }
        });
        return button;
    }

    /**
     * Attempts to finalize the pre-game leader cards choice.
     */
    private void pickLeaderCards() {
        if (selectedPreGameLeaderCardToggleButtons.size() != 2) {
            populateInfoLabel("Select two cards.");
        } else {
            List<DumbLeaderCard> chosenCards = selectedPreGameLeaderCardToggleButtons.stream()
                    .map(ToggleButton::getUserData)
                    .map(object -> (DumbLeaderCard) object)
                    .collect(Collectors.toList());
            if (gui.getInputVerifier().canPickLeaderCards(chosenCards)) {
                gui.getClientSocket().sendAction(new PregameLeaderCardsChoiceAction(chosenCards));
                populateInfoLabel("Submitted Leader Card selection!");
            } else {
                populateInfoLabel("Invalid Selection");
            }
        }
    }

    /**
     * Terminates the pre-game leader card choice.
     */
    public void endLeaderCardsChoice() {
        bringToFront(actionsLayer, personalBoardLayer);
    }

    /**
     * Initializes the pre-game resource choice if the player has a position
     * greater than 1.
     */
    public void startPreGameResourceChoice() {
        int ownPosition = gui.getDumbModel().getOwnPersonalBoard().getPosition();
        if (ownPosition > 1) {
            ConfirmResetButtonsStrategy.PRE_GAME_RESOURCES_CHOICE.applyTo(confirmButton,
                    resetButton);
            switch (ownPosition) {
                case 2, 3 -> sequentialResourceSelectionText.setText("Pick a resource!");
                case 4 -> sequentialResourceSelectionText.setText("Pick two resources!");
            }
            bringToFront(confirmResetLayer, pregameResourceChoiceLayer);
        }
    }

    /**
     * Attempts to finalize the pre-game resource choice.
     */
    private void confirmPreGameResourceChoice() {
        if (gui.getInputVerifier().canPickResources(chosenResourcesMap)) {
            gui.getClientSocket().sendAction(new PregameResourceChoiceAction(chosenResourcesMap));
            populateInfoLabel("Submitted resource choice");
        } else {
            reset();
            populateInfoLabel("Illegal resource choice, please retry.");
        }
    }

    /**
     * Terminates the pre-game resource choice.
     */
    public void endPreGameResourceChoice() {
        chosenResourcesMap.clear();
        bringToFront(actionsLayer, personalBoardLayer);
    }

    /**
     * Adds the passed resource to the chosenResourceMap map.
     * @param resource Resource instance.
     */
    private void addResourceToChoiceMap(Resource resource) {
        chosenResourcesMap.compute(resource, (k, v) -> (v == null) ? 1 : v + 1);
        StringBuilder resourceMap = new StringBuilder("You have selected: ");
        for (Resource res : chosenResourcesMap.keySet()) {
            resourceMap.append("\n").append(chosenResourcesMap.get(res)).append(" ").append(res);
        }
        populateInfoLabel(resourceMap.toString());
    }

    /**
     * Initializes the choice of discarding a leader card.
     */
    @FXML
    private void startDiscardLeaderCard() {
        leaderCardsHBox.getChildren().stream().map(node ->
                (ToggleButton) node).forEach(button -> button.setToggleGroup(leaderCardActionToggleGroup));
        ConfirmResetButtonsStrategy.DISCARD_LEADER_CARD.applyTo(confirmButton, resetButton);
        bringToFront(confirmResetLayer, preGameLeaderCardSelectionLayer);
    }

    /**
     * Attempts to finalize the choice of discarding a leader card.
     */
    private void confirmDiscardLeaderCard() {
        DumbLeaderCard toDiscardLeaderCard =
                (DumbLeaderCard) leaderCardActionToggleGroup.getSelectedToggle().getUserData();
        if (gui.getInputVerifier().canDiscard(toDiscardLeaderCard)) {
            gui.getClientSocket().sendAction(new DiscardLeaderCardAction(toDiscardLeaderCard));
        } else {
            populateInfoLabel("Invalid Leader Card discard!");
        }
    }

    /**
     * Interrupts any current leader card action (activation or discarding).
     */
    private void cancelLeaderCardAction() {
        leaderCardActionToggleGroup.getToggles().clear();
        bringToFront(actionsLayer, personalBoardLayer);
    }

    /**
     * Ends a successful discarding of a leader card.
     */
    public void endDiscardLeaderCard() {
        populateInfoLabel("You have successfully discarded your leader card!");
        cancelLeaderCardAction();
    }

    /**
     * Initializes the choice of activating a leader card.
     */
    @FXML
    private void startActivateLeaderCard() {
        leaderCardsHBox.getChildren().stream().map(node ->
                (ToggleButton) node).forEach(button -> button.setToggleGroup(leaderCardActionToggleGroup));
        ConfirmResetButtonsStrategy.ACTIVATE_LEADER_CARD.applyTo(confirmButton, resetButton);
        bringToFront(confirmResetLayer, preGameLeaderCardSelectionLayer);
    }

    /**
     * Attempts to finalize the choice of activating a leader card.
     */
    private void confirmActivateLeaderCard() {
        DumbLeaderCard toActivateLeaderCard =
                (DumbLeaderCard) leaderCardActionToggleGroup.getSelectedToggle().getUserData();
        if (gui.getInputVerifier().canActivate(toActivateLeaderCard)) {
            gui.getClientSocket().sendAction(new ActivateLeaderCardAction(toActivateLeaderCard));
        } else {
            populateInfoLabel("Invalid Leader Card activation!");
        }
    }

    /**
     * Ends a successful activation of a leader card.
     */
    public void endActivateLeaderCard() {
        populateInfoLabel("You have successfully activated your leader card!");
        cancelLeaderCardAction();
    }

    /**
     * Initializes the choice of MarketMarbles after a PickFromMarketAction is confirmed.
     * @param tempMarbles map of temporarily stored MarketMarbles, awaiting for selection.
     */
    public synchronized void startTempMarblesChoice(Map<MarketMarble, Integer> tempMarbles) {
        if (!isTempMarblesChoiceInitialized) {
            isTempMarblesChoiceInitialized = true;
            tempMarbles.forEach((marble, quantity) ->
                    IntStream.range(0, quantity).forEach(i ->
                            createMarbleChoiceNode(marble).ifPresent(node ->
                                    Platform.runLater(() -> selectMarblesLayer.getChildren().add(node)))));
            ConfirmResetButtonsStrategy.SELECT_MARBLES.applyTo(confirmButton, resetButton);
            bringToFront(confirmResetLayer, selectMarblesLayer);
        }
    }

    /**
     * Creates a JavaFX Node Optional which, if present, contains at least one ToggleButton
     * representing a marble.
     * @param marble MarketMarble that will get displayed. Depending on the current DumbModel
     *               state, for example if a convert leader card is active, the output node
     *               will vary.
     * @return empty optional if the passed marble is White and there are no convert leader
     * cards active. Otherwise returns a Node containing buttons that will forward the choice.
     */
    private Optional<Node> createMarbleChoiceNode(MarketMarble marble) {
        if (marble.equals(MarketMarble.WHITE)) {
            List<DumbConvertLeaderCard> convertLeaderCards = gui.getDumbModel()
                    .getOwnPersonalBoard().getLeaderCards().stream()
                    .filter(card -> card.isActive() && card.getAbility().equals(LeaderCardAbility.CONVERT))
                    .map(card -> (DumbConvertLeaderCard) card)
                    .collect(Collectors.toList());
            switch (convertLeaderCards.size()) {
                case 0:
                    return Optional.empty();
                case 1:
                    return Optional.of(createMarbleToggleButton(
                            convertLeaderCards.get(0).getConvertedResource().toMarble()));
                case 2:
                    ToggleGroup toggleGroup = new ToggleGroup();
                    VBox multiChoiceVBox = new VBox();
                    multiChoiceVBox.setSpacing(3);
                    convertLeaderCards.forEach(convertCard -> {
                        ToggleButton button =
                                createMarbleToggleButton(convertCard.getConvertedResource().toMarble());
                        button.setToggleGroup(toggleGroup);
                        multiChoiceVBox.getChildren().add(button);
                    });
                    return Optional.of(multiChoiceVBox);
                default:
                    Throwable throwable = new Throwable("Somehow this player has more than 2 " +
                            "Leader Cards");
                    throwable.printStackTrace();
                    return Optional.empty();
            }
        } else {
            return Optional.of(createMarbleToggleButton(marble));
        }
    }

    /**
     * Creates a single ToggleButton containing the passed marble image.
     * @param marble MarketMarble that will be displayed on the button.
     * @return ToggleButton used in the marble choice.
     */
    private ToggleButton createMarbleToggleButton(MarketMarble marble) {
        if (marble.equals(MarketMarble.WHITE))
            logger.info("Something went wrong: creating white marble button");
        ToggleButton button = new ToggleButton();
        ImageView marbleImage = new ImageView(getImageFromPath(marble.toImagePath()));
        marbleImage.setFitHeight(50);
        marbleImage.setPreserveRatio(true);
        button.setPadding(new Insets(10));
        button.setUserData(marble);
        button.setGraphic(marbleImage);
        button.selectedProperty().addListener((observable, oldValue, selectedNow) -> {
            if (selectedNow) {
                selectedTempMarblesToggleButtons.add(button);
            } else {
                selectedTempMarblesToggleButtons.remove(button);
            }
        });
        return button;
    }

    /**
     * Attempts to finalize the Market Marbles selection.
     */
    private void confirmMarbleSelection() {
        Map<MarketMarble, Integer> confirmedMarblesChoice = new HashMap<>();
        selectedTempMarblesToggleButtons.forEach(button ->
                confirmedMarblesChoice.compute(
                        ((MarketMarble) button.getUserData()),
                        (k, v) -> (v == null) ? 1 : v + 1));
        gui.getClientSocket().sendAction(new SelectMarblesAction(confirmedMarblesChoice));
    }

    /**
     * Ends a successful selection of marbles.
     */
    public void endTempMarblesChoice() {
        selectedTempMarblesToggleButtons.clear();
        Platform.runLater(() -> selectMarblesLayer.getChildren().clear());
        isTempMarblesChoiceInitialized = false;
        bringToFront(actionsLayer, personalBoardLayer);
        populateInfoLabel("The selected marbles have been converted to " +
                "resources and stored!");
    }

    /**
     * Initializes the development slot selection during the purchase of a DumbDevelopmentCard
     * from the board.
     * @param dumbDevelopmentCard card which is getting purchased.
     */
    public void startDevelopmentSlotSelection(DumbDevelopmentCard dumbDevelopmentCard) {
        selectDevCardSlotToggleGroup.getToggles().forEach(toggle -> {
            ((ToggleButton) toggle).setVisible(true);
            ((ToggleButton) toggle).setUserData(dumbDevelopmentCard);
        });
        ConfirmResetButtonsStrategy.SELECT_DEVELOPMENT_SLOT.applyTo(confirmButton, resetButton);
        bringToFront(confirmResetLayer, personalBoardLayer);
        gui.goToPersonalView();
    }

    /**
     * Attempts to finalize the development slot selection.
     */
    private void confirmDevelopmentSlotSelection() {
        Map<Resource, Integer> fullCost =
                ((DumbDevelopmentCard) selectDevCardSlotToggleGroup.getSelectedToggle().getUserData()).getCost();
        gui.getDumbModel().getOwnPersonalBoard().getLeaderCards().stream()
                .filter(card -> card.isActive() && card.getAbility().equals(LeaderCardAbility.DISCOUNT))
                .forEach(card -> {
                    DumbDiscountLeaderCard activeDiscountLeaderCard = (DumbDiscountLeaderCard) card;
                    fullCost.compute(activeDiscountLeaderCard.getDiscountedResource(),
                            (k, v) -> v == null ? null : (v == 1 ? null : v - 1));
                });
        startResourceChoice(fullCost, null);
        ConfirmResetButtonsStrategy.SELECT_RESOURCES_TO_BUY_DEVELOPMENT_CARD.applyTo(confirmButton, resetButton);
        bringToFront(confirmResetLayer, resourceSelectionLayer);
        selectDevCardSlotToggleGroup.getToggles().forEach(toggle -> ((ToggleButton) toggle).setVisible(false));
    }

    /**
     * Cancels the player action of selecting a development slot, interrupting the whole
     * development card purchase process.
     */
    private void cancelDevelopmentSlotSelection() {
        bringToFront(actionsLayer, personalBoardLayer);
        selectDevCardSlotToggleGroup.getToggles().forEach(toggle -> ((ToggleButton) toggle).setVisible(false));
        Optional.ofNullable(selectDevCardSlotToggleGroup.getSelectedToggle()).ifPresent(toggle -> toggle.setSelected(false));
    }

    /**
     * Attempts to finalize the purchase of a development card.
     */
    private void confirmDevelopmentCardPurchase() {
        ToggleButton selectedDevelopmentSlotToggleButton =
                (ToggleButton) selectDevCardSlotToggleGroup.getSelectedToggle();
        DumbDevelopmentCard selectedCard =
                (DumbDevelopmentCard) selectedDevelopmentSlotToggleButton.getUserData();
        Map<Resource, Integer> depotsMap = createDepotsMapFromSliders();
        Map<Resource, Integer> strongboxMap = createStrongboxMapFromSliders();
        int developmentCardSlotIndex =
                selectDevCardSlotToggleGroup.getToggles().indexOf(selectedDevelopmentSlotToggleButton) + 1;
        if (gui.getInputVerifier().canBuy(selectedCard.getLevel(), selectedCard.getColor(),
                developmentCardSlotIndex, depotsMap, strongboxMap)) {
            gui.getClientSocket().sendAction(new BuyDevelopmentCardAction(selectedCard.getLevel(),
                    selectedCard.getColor(), developmentCardSlotIndex, depotsMap, strongboxMap));
        } else {
            populateInfoLabel("Invalid Development Card purchase!");
        }
    }

    /**
     * Ends a successful purchase of a development card.
     */
    public void endDevelopmentCardPurchase() {
        populateInfoLabel("The purchased Development Card has been added to your Development " +
                "Slot!");
        cancelDevelopmentSlotSelection();
        cancelResourceChoice();
    }

    /**
     * Initializes a resource discard choice on the screen.
     * @param requiredResources Map of required resources for the associated choice.
     * @param extraMessage Additional information communicated on the screen during the resource
     *                     choice.
     */
    private void startResourceChoice(Map<Resource, Integer> requiredResources,
                                     String extraMessage) {
        StringBuilder requiredResourcesMessage = new StringBuilder("The selected action requires " +
                "the following resources:\n");
        requiredResources.forEach((resource, quantity) -> requiredResourcesMessage.append(resource).append(": x").append(quantity).append("\n"));
        if (extraMessage != null) requiredResourcesMessage.append(extraMessage);
        resourceSelectionText.setText(requiredResourcesMessage.toString());
        Map<String, Map<Resource, Integer>> resourceContainersMap = new HashMap<>();
        DumbPersonalBoard personalBoard = gui.getDumbModel().getOwnPersonalBoard();
        if (personalBoard.getDepots().getResourceCount() > 0)
            resourceContainersMap.put("Warehouse Depots",
                    personalBoard.getDepots().getStoredResources());
        if (personalBoard.getStrongbox().getResourceCount() > 0)
            resourceContainersMap.put("Strongbox",
                    personalBoard.getStrongbox().getStoredResources());
        personalBoard.getLeaderCards().stream()
                .filter(card -> card.isActive() && card.getAbility().equals(LeaderCardAbility.STORE))
                .map(card -> (DumbStoreLeaderCard) card)
                .filter(card -> card.getResourceCount() > 0)
                .forEach(card -> resourceContainersMap.put(card.getStoredType() + " Store Leader " +
                        "Card", card.getStoredResources()));
        resourceContainersMap.forEach((containerName, resourcesMap) ->
                resourceSelectionSlidersTilePane.getChildren().add(createResourceSlidersBorderPane(containerName, resourcesMap)));
    }

    /**
     * Interrupts a resource choice and therefore the associated action, either a development
     * card purchase or a production activation.
     */
    private void cancelResourceChoice() {
        bringToFront(actionsLayer, personalBoardLayer);
        Platform.runLater(() -> resourceSelectionSlidersTilePane.getChildren().clear());
    }

    /**
     * Creates a BorderPane containing JavaFX Sliders for each resource of the passed map and a
     * header containing the passed text.
     * @param labelText String that will be displayed at the top of the created pane.
     * @param resourcesMap Resources map that will dictate the number of sliders, their associated
     *                     ImageViews and their length.
     * @return A JavaFX BorderPane displayed in a resource choice.
     */
    private BorderPane createResourceSlidersBorderPane(String labelText,
                                                       Map<Resource, Integer> resourcesMap) {
        BorderPane pane = new BorderPane();
        pane.setId(labelText);
        pane.getStyleClass().add("left-pane");
        Label sliderTileLabel = new Label(labelText);
        sliderTileLabel.setTextFill(WHITE);
        pane.setTop(sliderTileLabel);
        sliderTileLabel.setAlignment(Pos.CENTER);
        VBox slidersVBox = new VBox();
        resourcesMap.forEach((resource, quantity) -> {
            HBox imageSliderHBox = new HBox();
            imageSliderHBox.setAlignment(Pos.CENTER);
            imageSliderHBox.setSpacing(12);
            imageSliderHBox.setPadding(new Insets(15));
            ImageView resourceImage = new ImageView(getImageFromPath(resource.toImgPath()));
            resourceImage.setFitWidth(35);
            resourceImage.setPreserveRatio(true);
            imageSliderHBox.getChildren().add(resourceImage);
            Slider quantitySelectionSlider = new Slider(0, quantity, 0);
            quantitySelectionSlider.setShowTickLabels(true);
            quantitySelectionSlider.setShowTickMarks(true);
            quantitySelectionSlider.setSnapToTicks(true);
            quantitySelectionSlider.setMajorTickUnit(1);
            quantitySelectionSlider.setMinorTickCount(0);
            quantitySelectionSlider.setUserData(resource);
            imageSliderHBox.getChildren().add(quantitySelectionSlider);
            slidersVBox.getChildren().add(imageSliderHBox);
        });
        pane.setCenter(slidersVBox);
        return pane;
    }

    /**
     * Creates a data structure of resources to be discarded from depots.
     * @return Resource, Integer Map parsed from the resource choice sliders.
     */
    private Map<Resource, Integer> createDepotsMapFromSliders() {
        List<Map<Resource, Integer>> depotsAndLeaderCardsStorages = new ArrayList<>();
        depotsAndLeaderCardsStorages.add(getResourceMapFromSliders("Warehouse Depots"));
        gui.getDumbModel().getOwnPersonalBoard().getLeaderCards().stream()
                .filter(card -> card.isActive() && card.getAbility().equals(LeaderCardAbility.STORE))
                .map(card -> (DumbStoreLeaderCard) card)
                .filter(storeLeaderCard -> storeLeaderCard.getResourceCount() > 0)
                .forEach(dumbStoreLeaderCard ->
                        depotsAndLeaderCardsStorages.add(getResourceMapFromSliders(dumbStoreLeaderCard.getStoredType() + " Store Leader Card")));
        Map<Resource, Integer> depotsMap = new HashMap<>();
        Map<Resource, Integer> finalDepotsMap = depotsMap;
        depotsAndLeaderCardsStorages.forEach(storage -> storage.forEach((resource, quantity) ->
                finalDepotsMap.compute(resource, (k, v) -> (v == null) ? quantity : v + quantity)));
        if (finalDepotsMap.values().stream().reduce(0, Integer::sum) == 0) depotsMap = null;
        else depotsMap = finalDepotsMap;
        return depotsMap;
    }

    /**
     * Creates a data structure of resources to be discarded from the strongbox.
     * @return Resource, Integer Map parsed from the resource choice sliders.
     */
    private Map<Resource, Integer> createStrongboxMapFromSliders() {
        Map<Resource, Integer> strongboxMap = getResourceMapFromSliders("Strongbox");
        if (strongboxMap.values().stream().reduce(0, Integer::sum) == 0) strongboxMap = null;
        return strongboxMap;
    }

    /**
     * Returns a Map, even with no entries, parsed from the sliders. Each value is > 0.
     * @param id identifier to distinguish depots/strongbox/leader cards.
     * @return resource map of the DumbResourceManager associated to the given id.
     */
    public Map<Resource, Integer> getResourceMapFromSliders(String id) {
        Map<Resource, Integer> resourcesMap = new HashMap<>();
        Optional<BorderPane> containerBorderPane =
                resourceSelectionSlidersTilePane.getChildren().stream()
                        .filter(node -> node.getId().equals(id))
                        .map(node -> (BorderPane) node)
                        .findAny();
        containerBorderPane.ifPresent(borderPane -> {
            VBox slidersParent = (VBox) borderPane.getCenter();
            slidersParent.getChildren().forEach(node -> {
                Slider resourceSlider = (Slider) ((HBox) node).getChildren().get(1);
                int quantity = (int) resourceSlider.getValue();
                if (quantity > 0)
                    resourcesMap.put((Resource) resourceSlider.getUserData(), quantity);
            });
        });
        return resourcesMap;
    }

    /**
     * Initializes all ToggleButtons that could be used in a production. When selected they
     * will be counted towards the to-be-activated production.
     */
    @FXML
    private void startProductionToggleButtonsSelection() {
        selectedProduceLeaderCardsToggleButtons = FXCollections.observableSet();
        selectedDevelopmentCardsToggleButtons = FXCollections.observableSet();
        leaderCardsHBox.getChildren().stream()
                .filter(node -> ((DumbLeaderCard) node.getUserData()).isActive()
                        && ((DumbLeaderCard) node.getUserData()).getAbility().equals(LeaderCardAbility.PRODUCE))
                .map(node -> (ToggleButton) node)
                .forEach(button -> addButtonToProductionGroup(button,
                        selectedProduceLeaderCardsToggleButtons));
        IntStream.range(0, 3).forEach(i -> {
            StackPane slotPane = devCardSlotsStackPanes.get(i);
            Optional<DumbDevelopmentCard> paneCard =
                    Optional.ofNullable((DumbDevelopmentCard) slotPane.getUserData());
            if (paneCard.isPresent()) {
                addButtonToProductionGroup(
                        (ToggleButton) slotPane.getChildren().get(slotPane.getChildren().size() - 1),
                        selectedDevelopmentCardsToggleButtons);
            }
        });
        ConfirmResetButtonsStrategy.SELECT_PRODUCTION_BUTTONS.applyTo(confirmButton, resetButton);
        bringToFront(confirmResetLayer, personalBoardLayer);
    }

    /**
     * Ends the selection of selectable production elements.
     */
    private void endProductionButtonsSelection() {
        Platform.runLater(() -> {
            selectedProduceLeaderCardsToggleButtons.forEach(toggleButton -> toggleButton.setSelected(false));
            selectedDevelopmentCardsToggleButtons.forEach(toggleButton -> toggleButton.setSelected(false));
            defaultSlotToggleButton.setSelected(false);
            resourceSelectionSlidersTilePane.getChildren().clear();
        });
        bringToFront(actionsLayer, personalBoardLayer);
    }

    /**
     * Utility method created to add the passed ToggleButton to an ObservableSet on click
     * and remove it on deselection.
     * @param button target ToggleButton.
     * @param productionGroup set that will host all buttons for a certain production type.
     *                        For example produce leader card buttons or development card buttons.
     */
    private void addButtonToProductionGroup(ToggleButton button,
                                            ObservableSet<ToggleButton> productionGroup) {
        button.selectedProperty().addListener((observable, oldValue, selectedNow) -> {
            if (selectedNow) {
                productionGroup.add(button);
            } else {
                productionGroup.remove(button);
            }
        });
    }

    /**
     * Finalizes the selection of productions buttons and calculates the price of
     * the production, which will be passed onto the StartResourceChoice method.
     */
    private void confirmProductionButtonsSelection() {
        Map<Resource, Integer> productionCostMap = new HashMap<>();
        selectedDevelopmentCardsToggleButtons.forEach(selectedDevCardToggleButton ->
                ((DumbDevelopmentCard) selectedDevCardToggleButton.getUserData())
                        .getInputResources()
                        .forEach((inputResource, quantity) ->
                                productionCostMap.compute(inputResource,
                                        (k, v) -> (v == null) ? quantity : v + quantity)));
        selectedProduceLeaderCardsToggleButtons.forEach(selectedProdLeaderCardButton ->
                productionCostMap.compute(
                        ((DumbProduceLeaderCard) selectedProdLeaderCardButton.getUserData()).getInputResource(),
                        (k, v) -> v == null ? 1 : v + 1
                )
        );
        startResourceChoice(productionCostMap,
                defaultSlotToggleButton.isSelected()
                        ? "2x random (Default Production)"
                        : null);
        ConfirmResetButtonsStrategy.SELECT_PRODUCTION_INPUT_RESOURCES.applyTo(confirmButton,
                resetButton);
        bringToFront(confirmResetLayer, resourceSelectionLayer);
    }

    /**
     * Finalizes the selection of resources that will be discarded from the production. If
     * any of the selected buttons have a wildcard output, a new choice will be initialized.
     * Otherwise the confirmProduction method will be called.
     */
    private void confirmProductionInputResources() {
        boolean defaultSlotSelected = defaultSlotToggleButton.isSelected();
        ToggleButton leaderCard1ToggleButton = (ToggleButton) leaderCardsHBox.getChildren().get(0);
        ToggleButton leaderCard2ToggleButton = (ToggleButton) leaderCardsHBox.getChildren().get(1);
        boolean leaderCard1Selected = (leaderCard1ToggleButton.isSelected()
                && ((DumbLeaderCard) leaderCard1ToggleButton.getUserData()).isActive()
                && ((DumbLeaderCard) leaderCard1ToggleButton.getUserData()).getAbility().equals(LeaderCardAbility.PRODUCE));
        boolean leaderCard2Selected = (leaderCard2ToggleButton.isSelected()
                && ((DumbLeaderCard) leaderCard2ToggleButton.getUserData()).isActive()
                && ((DumbLeaderCard) leaderCard2ToggleButton.getUserData()).getAbility().equals(LeaderCardAbility.PRODUCE));
        if (defaultSlotSelected || leaderCard1Selected || leaderCard2Selected) {
            startOutputResourcesChoice(defaultSlotSelected, leaderCard1Selected,
                    leaderCard2Selected);
        } else {
            confirmProduction();
        }
    }

    /**
     * Initializes the choice for wildcard outputs.
     * @param defaultSlot flag which indicates if the default slot was selected in the production.
     * @param leaderCard1 flag which indicates if the first leader card was selected in the production.
     * @param leaderCard2 flag which indicates if the second leader card was selected in the production.
     */
    private void startOutputResourcesChoice(boolean defaultSlot, boolean leaderCard1,
                                            boolean leaderCard2) {
        if (defaultSlot) outputResourcesTilePane.getChildren().add(defaultSlotOutputTile);
        if (leaderCard1) outputResourcesTilePane.getChildren().add(leaderCard1OutputTile);
        if (leaderCard2) outputResourcesTilePane.getChildren().add(leaderCard2OutputTile);
        ConfirmResetButtonsStrategy.SELECT_PRODUCTION_OUTPUT_RESOURCES.applyTo(confirmButton,
                resetButton);
        bringToFront(confirmResetLayer, outputResourcesSelectionLayer);
    }

    /**
     * Attempts to finalize the production.
     */
    private void confirmProduction() {
        ProductionCombo productionCombo = new ProductionCombo();
        if (selectedDevelopmentCardsToggleButtons.size() > 0)
            productionCombo.setDevelopmentCards(
                    selectedDevelopmentCardsToggleButtons.stream()
                            .map(button -> (DumbDevelopmentCard) button.getUserData())
                            .collect(Collectors.toList()));
        if (outputResourcesTilePane.getChildren().contains(defaultSlotOutputTile)) {
            Map<Resource, Integer> resourceIntegerMap = new HashMap<>();
            resourceIntegerMap.put((Resource) defaultSlotOutputToggleGroup.getSelectedToggle().getUserData(), 1);
            productionCombo.setDefaultSlotOutput(resourceIntegerMap);
        }
        if (selectedProduceLeaderCardsToggleButtons.size() > 0) {
            Map<DumbProduceLeaderCard, Resource> dumbProduceLeaderCardResourceMap = new HashMap<>();
            if (outputResourcesTilePane.getChildren().contains(leaderCard1OutputTile)) {
                dumbProduceLeaderCardResourceMap.put((DumbProduceLeaderCard) leaderCardsHBox.getChildren().get(0).getUserData(),
                        (Resource) leaderCard1OutputToggleGroup.getSelectedToggle().getUserData());
            }

            if (outputResourcesTilePane.getChildren().contains(leaderCard2OutputTile)) {
                dumbProduceLeaderCardResourceMap.put((DumbProduceLeaderCard) leaderCardsHBox.getChildren().get(1).getUserData(),
                        (Resource) leaderCard2OutputToggleGroup.getSelectedToggle().getUserData());
            }
            productionCombo.setLeaderCardProduction(dumbProduceLeaderCardResourceMap);
        }
        productionCombo.setDiscardedResourcesFromDepots(createDepotsMapFromSliders());
        productionCombo.setDiscardedResourcesFromStrongbox(createStrongboxMapFromSliders());
        if (gui.getInputVerifier().canProduce(productionCombo)) {
            gui.getClientSocket().sendAction(new ActivateProductionAction(productionCombo));
        } else {
            populateInfoLabel("Invalid production!");
        }
    }

    /**
     * Terminates all production related choices and resets buttons and layers related to a
     * production activation visual process.
     */
    public void endProductionChoices() {
        bringToFront(actionsLayer, personalBoardLayer);
        Platform.runLater(() -> {
            resourceSelectionSlidersTilePane.getChildren().clear();
            outputResourcesTilePane.getChildren().clear();
            defaultSlotToggleButton.setSelected(false);
            selectedDevelopmentCardsToggleButtons.forEach(button -> button.setSelected(false));
            selectedProduceLeaderCardsToggleButtons.forEach(button -> button.setSelected(false));
            Optional.ofNullable(defaultSlotOutputToggleGroup.getSelectedToggle()).ifPresent(button -> button.setSelected(false));
            Optional.ofNullable(leaderCard1OutputToggleGroup.getSelectedToggle()).ifPresent(button -> button.setSelected(false));
            Optional.ofNullable(leaderCard2OutputToggleGroup.getSelectedToggle()).ifPresent(button -> button.setSelected(false));
        });
    }

    /**
     * Shows to screen the two passed Nodes.
     * @param left layer which will be brought to front on the bottom left StackPane
     * @param right layer which will be brought to front on the main StackPane
     */
    private void bringToFront(Node left, Node right) {
        Platform.runLater(() -> {
            left.toFront();
            right.toFront();
        });
    }
}
