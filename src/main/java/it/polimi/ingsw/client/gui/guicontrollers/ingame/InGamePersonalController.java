package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.*;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.market.MarketMarble;
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
    private ObservableSet<ToggleButton> selectedProductionToggleButtons;

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
    }

    public void initialize(GUI gui) {
        super.setGui(gui);
        super.setNicknameLabel(gui.getPersonalNickname());
        initVisitMenu(gui.getDumbModel().getPersonalBoards().stream()
                .map(DumbPersonalBoard::getNickname)
                .filter(nick -> !nick.equals(gui.getPersonalNickname()))
                .collect(Collectors.toList()));
    }

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
        }
    }

    @FXML
    private void reset() {
        switch ((ConfirmResetButtonsStrategy) resetButton.getUserData()) {
            case PRE_GAME_RESOURCES_CHOICE -> {
                chosenResourcesMap.clear();
                populateInfoLabel("Resource choice cleared.");
            }
            case VISIT, SELECT_DEVELOPMENT_SLOT -> bringToFront(actionsLayer, personalBoardLayer);
            case SELECT_RESOURCES_TO_BUY_DEVELOPMENT_CARD -> cancelResourceChoice();
            case DISCARD_LEADER_CARD, ACTIVATE_LEADER_CARD -> cancelLeaderCardAction();
        }
    }

    @FXML
    private void openVisitMenu() {
        ConfirmResetButtonsStrategy.VISIT.applyTo(confirmButton, resetButton);
        bringToFront(confirmResetLayer, visitLayer);
    }

    @FXML
    private void endTurn() {
        if (!gui.getInputVerifier().canEndTurn()) {
            populateInfoLabel("You cannot end your turn at this time.");
        } else {
            gui.getClientSocket().sendAction(new EndTurnAction());
        }
    }

    public void populateInfoLabel(String outcome) {
        BorderPane alertPane = new BorderPane();
        alertPane.setPrefWidth(200);
        alertPane.getStyleClass().add("alert");
        Text outcomeText = new Text(outcome);
        Button closeAlertButton = new Button("x");
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

    private void initLeaderCardsDisplay(DumbPersonalBoard dumbPersonalBoard) {
        // Only called once, after the server has confirmed the player's initial
        // leader cards choice
        List<DumbLeaderCard> cards = dumbPersonalBoard.getLeaderCards();
        cards.forEach(card -> Platform.runLater(() -> leaderCardsHBox.getChildren().add(createLeaderCardNode(card))));
        Platform.runLater(() -> preGameLeaderCardSelectionLayer.getChildren().clear());
    }

    private ToggleButton createLeaderCardNode(DumbLeaderCard card) {
        ToggleButton button = new ToggleButton();
        button.setUserData(card);
        button.getStyleClass().add("inactive-leader-card");
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

    @Override
    protected synchronized void renderLeaderCards(DumbPersonalBoard dumbPersonalBoard) {
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
                                        leaderCardButton.getStyleClass().remove("inactive-leader-card");
                                        leaderCardButton.setUserData(cardInBoard);
                                    }
                                    if (cardInBoard.getAbility().equals(LeaderCardAbility.STORE)) {
                                        // Update Storage card every time to keep track of resource changes
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

    public void startLeaderCardsChoice(List<DumbLeaderCard> leaderCards) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        leaderCards.forEach(card -> hBox.getChildren().add(createPreGameLeaderCardToggleButton(card)));
        Platform.runLater(() -> preGameLeaderCardSelectionLayer.getChildren().add(hBox));
        ConfirmResetButtonsStrategy.PRE_GAME_LEADER_CARDS_CHOICE.applyTo(confirmButton,
                resetButton);
        bringToFront(confirmResetLayer, preGameLeaderCardSelectionLayer);
    }

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

    public void endLeaderCardsChoice() {
        bringToFront(actionsLayer, personalBoardLayer);
    }

    public void startPreGameResourceChoice() {
        int ownPosition = gui.getDumbModel().getOwnPersonalBoard().getPosition();
        logger.info(String.valueOf(ownPosition));
        if (ownPosition > 1) {
            ConfirmResetButtonsStrategy.PRE_GAME_RESOURCES_CHOICE.applyTo(confirmButton,
                    resetButton);
            switch (ownPosition) {
                case 2, 3 -> populateInfoLabel("Pick a resource!");
                case 4 -> populateInfoLabel("Pick two resources!");
            }
            bringToFront(confirmResetLayer, pregameResourceChoiceLayer);
        }
    }

    private void confirmPreGameResourceChoice() {
        if (gui.getInputVerifier().canPickResources(chosenResourcesMap)) {
            gui.getClientSocket().sendAction(new PregameResourceChoiceAction(chosenResourcesMap));
            populateInfoLabel("Submitted resource choice");
        } else {
            reset();
            populateInfoLabel("Illegal resource choice, please retry.");
        }
    }

    public void endPreGameResourceChoice() {
        bringToFront(actionsLayer, personalBoardLayer);
    }

    private void addResourceToChoiceMap(Resource resource) {
        chosenResourcesMap.compute(resource, (k, v) -> (v == null) ? 1 : v + 1);
        StringBuilder resourceMap = new StringBuilder("You have selected: ");
        for (Resource res : chosenResourcesMap.keySet()) {
            resourceMap.append("\n").append(chosenResourcesMap.get(res)).append(" ").append(res);
        }
        populateInfoLabel(resourceMap.toString());
    }

    @FXML
    private void startDiscardLeaderCard() {
        leaderCardsHBox.getChildren().stream().map(node ->
                (ToggleButton) node).forEach(button -> button.setToggleGroup(leaderCardActionToggleGroup));
        ConfirmResetButtonsStrategy.DISCARD_LEADER_CARD.applyTo(confirmButton, resetButton);
        bringToFront(confirmResetLayer, preGameLeaderCardSelectionLayer);
    }

    private void confirmDiscardLeaderCard() {
        DumbLeaderCard toDiscardLeaderCard =
                (DumbLeaderCard) leaderCardActionToggleGroup.getSelectedToggle().getUserData();
        if (gui.getInputVerifier().canDiscard(toDiscardLeaderCard)) {
            gui.getClientSocket().sendAction(new DiscardLeaderCardAction(toDiscardLeaderCard));
        } else {
            populateInfoLabel("Invalid Leader Card discard!");
        }
    }

    private void cancelLeaderCardAction() {
        leaderCardActionToggleGroup.getToggles().clear();
        bringToFront(actionsLayer, personalBoardLayer);
    }

    public void endDiscardLeaderCard() {
        populateInfoLabel("You have successfully discarded your leader card!");
        cancelLeaderCardAction();
    }

    @FXML
    private void startActivateLeaderCard() {
        leaderCardsHBox.getChildren().stream().map(node ->
                (ToggleButton) node).forEach(button -> button.setToggleGroup(leaderCardActionToggleGroup));
        ConfirmResetButtonsStrategy.ACTIVATE_LEADER_CARD.applyTo(confirmButton, resetButton);
        bringToFront(confirmResetLayer, preGameLeaderCardSelectionLayer);
    }

    private void confirmActivateLeaderCard() {
        DumbLeaderCard toActivateLeaderCard =
                (DumbLeaderCard) leaderCardActionToggleGroup.getSelectedToggle().getUserData();
        if (gui.getInputVerifier().canActivate(toActivateLeaderCard)) {
            gui.getClientSocket().sendAction(new ActivateLeaderCardAction(toActivateLeaderCard));
        } else {
            populateInfoLabel("Invalid Leader Card activation!");
        }
    }

    public void endActivateLeaderCard() {
        populateInfoLabel("You have successfully activated your leader card!");
        cancelLeaderCardAction();
    }

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

    private void confirmMarbleSelection() {
        Map<MarketMarble, Integer> confirmedMarblesChoice = new HashMap<>();
        selectedTempMarblesToggleButtons.forEach(button ->
                confirmedMarblesChoice.compute(
                        ((MarketMarble) button.getUserData()),
                        (k, v) -> (v == null) ? 1 : v + 1));
        gui.getClientSocket().sendAction(new SelectMarblesAction(confirmedMarblesChoice));
    }

    public void endTempMarblesChoice() {
        selectedTempMarblesToggleButtons.clear();
        Platform.runLater(() -> selectMarblesLayer.getChildren().clear());
        isTempMarblesChoiceInitialized = false;
        bringToFront(actionsLayer, personalBoardLayer);
    }

    public void startDevelopmentSlotSelection(DumbDevelopmentCard dumbDevelopmentCard) {
        selectDevCardSlotToggleGroup.getToggles().forEach(toggle -> {
            ((ToggleButton) toggle).setVisible(true);
            ((ToggleButton) toggle).setUserData(dumbDevelopmentCard);
        });
        ConfirmResetButtonsStrategy.SELECT_DEVELOPMENT_SLOT.applyTo(confirmButton, resetButton);
        bringToFront(confirmResetLayer, personalBoardLayer);
        gui.goToPersonalView();
    }

    private void confirmDevelopmentSlotSelection() {
        selectDevCardSlotToggleGroup.getToggles().forEach(toggle -> ((ToggleButton) toggle).setVisible(false));
        startResourceChoice(((DumbDevelopmentCard) selectDevCardSlotToggleGroup.getSelectedToggle().getUserData()).getCost());
    }

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

    public void endDevelopmentCardPurchase() {
        populateInfoLabel("The purchased Development Card has been added to your Development Slot!");
        cancelResourceChoice();
    }

    private void startResourceChoice(Map<Resource, Integer> requiredResources) {
        StringBuilder requiredResourcesMessage = new StringBuilder("The selected action requires the following resources:");
        requiredResources.forEach((resource, quantity) -> requiredResourcesMessage.append("\n").append(resource).append(": x").append(quantity));
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
        ConfirmResetButtonsStrategy.SELECT_RESOURCES_TO_BUY_DEVELOPMENT_CARD.applyTo(confirmButton, resetButton);
        bringToFront(confirmResetLayer, resourceSelectionLayer);
    }

    private void cancelResourceChoice() {
        bringToFront(actionsLayer, personalBoardLayer);
        Platform.runLater(() -> resourceSelectionSlidersTilePane.getChildren().clear());
    }

    private BorderPane createResourceSlidersBorderPane(String labelText,
                                                       Map<Resource, Integer> chosenResourcesMap) {
        BorderPane pane = new BorderPane();
        pane.setId(labelText);
        pane.getStyleClass().add("left-pane");
        Label sliderTileLabel = new Label(labelText);
        sliderTileLabel.setTextFill(WHITE);
        pane.setTop(sliderTileLabel);
        sliderTileLabel.setAlignment(Pos.CENTER);
        VBox slidersVBox = new VBox();
        chosenResourcesMap.forEach((resource, quantity) -> {
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

    private Map<Resource, Integer> createStrongboxMapFromSliders() {
        Map<Resource, Integer> strongboxMap = getResourceMapFromSliders("Strongbox");
        if (strongboxMap.values().stream().reduce(0, Integer::sum) == 0) strongboxMap = null;
        return strongboxMap;
    }

    /**
     * Returns a Map, even with no entries, parsed from the sliders. Each value is > 0
     *
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

    @FXML
    private void startProduction() {
        selectedProductionToggleButtons = FXCollections.observableSet();
    }
    private void bringToFront(Node left, Node right) {
        Platform.runLater(() -> {
            left.toFront();
            right.toFront();
        });
    }
}
