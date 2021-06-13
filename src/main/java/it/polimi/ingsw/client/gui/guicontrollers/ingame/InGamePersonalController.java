package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.guicontrollers.JFXController;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PregameLeaderCardsChoiceAction;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PregameResourceChoiceAction;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.personalboard.FavorStatus;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InGamePersonalController extends JFXController {
    private final Logger logger = Logger.getLogger(getClass().getName());
    @FXML
    private HBox leaderCardsHBox;
    private boolean areLeaderCardsInitialized = false;
    @FXML
    private Label nicknameLabel;
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
    private VBox resourceChoiceLayer;
    private final ObservableSet<ToggleButton> selectedPreGameLeaderCardToggleButtons =
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
    private VBox depotsVBox;
    private final List<List<ImageView>> warehouseDepotsResourceImages = new ArrayList<>();
    @FXML
    private GridPane faithTrackGrid;
    private ImageView faithMarker;
    private List<GridCoordinates> faithTrackCoordsReference;
    @FXML
    private ImageView firstPopesFavor;
    @FXML
    private ImageView secondPopesFavor;
    @FXML
    private ImageView thirdPopesFavor;
    @FXML
    private Label strongboxCoinQuantity;
    @FXML
    private Label strongboxServantQuantity;
    @FXML
    private Label strongboxShieldQuantity;
    @FXML
    private Label strongboxStoneQuantity;
    @FXML
    private StackPane devCardSlot1;
    @FXML
    private StackPane devCardSlot2;
    @FXML
    private StackPane devCardSlot3;
    private final List<StackPane> devCardSlotsStackPanes = new ArrayList<>();
    @Override
    public void setGui(GUI gui) {
        super.setGui(gui);
        nicknameLabel.setText(gui.getPersonalNickname());
    }

    @FXML
    private void initialize() {
        // Initialize Resource-choosing buttons
        coinButton.setOnAction(e -> addResourceToChoiceMap(Resource.COIN));
        servantButton.setOnAction(e -> addResourceToChoiceMap(Resource.SERVANT));
        shieldButton.setOnAction(e -> addResourceToChoiceMap(Resource.SHIELD));
        stoneButton.setOnAction(e -> addResourceToChoiceMap(Resource.STONE));
        // Initialize WarehouseDepots Images
        IntStream.range(1, 4).forEach(this::initWarehouseDepotsResourceImages);
        // Initialize a list that will be used to position the marker in the correct
        // position on the faith track.
        initFaithTrackCoordsReferenceList();
        faithMarker = new ImageView(getImageFromPath("/img/faithtrack/faith_marker.png"));
        faithMarker.setFitHeight(60);
        faithMarker.setPreserveRatio(true);
        // Initialize Development Card Slots array
        devCardSlotsStackPanes.add(devCardSlot1);
        devCardSlotsStackPanes.add(devCardSlot2);
        devCardSlotsStackPanes.add(devCardSlot3);
    }

    private void initWarehouseDepotsResourceImages(int numberOfImageViews) {
        List<ImageView> resourceRowImages = new ArrayList<>();
        HBox resourcesRow = new HBox();
        resourcesRow.setAlignment(Pos.CENTER);
        IntStream.range(0, numberOfImageViews).forEach(i -> {
            ImageView resource = new ImageView();
            resource.setFitWidth(45);
            resource.setPreserveRatio(true);
            resourcesRow.getChildren().add(resource);
            resourceRowImages.add(resource);
        });
        depotsVBox.getChildren().add(resourcesRow);
        warehouseDepotsResourceImages.add(resourceRowImages);
    }

    private void initFaithTrackCoordsReferenceList() {
        try {
            JsonReader reader = new JsonReader(new FileReader(
                    "src/main/resources/json/client/faithTrackGridCoordinates.json"));
            GridCoordinates[] array =
                    new GsonBuilder().setPrettyPrinting().serializeNulls().create()
                            .fromJson(reader, GridCoordinates[].class);
            faithTrackCoordsReference = Arrays.asList(array);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void confirm() {
        switch ((ConfirmResetButtonsStrategy) confirmButton.getUserData()) {
            case PRE_GAME_LEADER_CARDS_CHOICE -> pickLeaderCards();
            case PRE_GAME_RESOURCES_CHOICE -> confirmPreGameResourceChoice();
        }
    }

    @FXML
    private void reset() {
        switch ((ConfirmResetButtonsStrategy) resetButton.getUserData()) {
            case PRE_GAME_RESOURCES_CHOICE -> {
                chosenResourcesMap.clear();
                populateInfoLabel("Resource choice cleared.");
            }
        }
    }

    private void populateInfoLabel(String outcome) {
        BorderPane alertPane = new BorderPane();
        alertPane.setPrefWidth(200);
        alertPane.getStyleClass().add("alert");
        Label outcomeLabel = new Label(outcome);
        Button closeAlertButton = new Button("x");
        alertPane.setTop(closeAlertButton);
        alertPane.setCenter(outcomeLabel);
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

    public void initLeaderCardsDisplay(DumbPersonalBoard dumbPersonalBoard) {
        endLeaderCardsSelection();
        // Only called once, after the server has confirmed the player's initial
        // leader cards choice
        List<DumbLeaderCard> cards = dumbPersonalBoard.getLeaderCards();
        cards.forEach(card -> Platform.runLater(() -> leaderCardsHBox.getChildren().add(createLeaderCardNode(card))));
    }

    private ToggleButton createLeaderCardNode(DumbLeaderCard card) {
        ToggleButton button = new ToggleButton();
        button.setUserData(card);
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

    public void renderPersonalBoard() {
        DumbPersonalBoard dumbPersonalBoard = gui.getDumbModel().getOwnPersonalBoard();
        renderPlayerInformation(dumbPersonalBoard);
        renderLeaderCards(dumbPersonalBoard);
        renderWarehouseDepots(dumbPersonalBoard);
        renderFaithTrack(dumbPersonalBoard);
        renderStrongbox(dumbPersonalBoard);
        renderDevelopmentCardSlots(dumbPersonalBoard);
    }

    private void renderPlayerInformation(DumbPersonalBoard dumbPersonalBoard) {

    }

    private synchronized void renderLeaderCards(DumbPersonalBoard dumbPersonalBoard) {
        synchronized (leaderCardsHBox) {
            if (!areLeaderCardsInitialized) {
                areLeaderCardsInitialized = true;
                initLeaderCardsDisplay(dumbPersonalBoard);
            }
        }
        leaderCardsHBox.getChildren().stream().map(child -> (ToggleButton) child)
                .filter(ToggleButton::isVisible)
                .forEach(leaderCardButton -> {
                    DumbLeaderCard cardInButton = (DumbLeaderCard) leaderCardButton.getUserData();
                    Optional<DumbLeaderCard> cardInBoardOptional = dumbPersonalBoard
                            .getLeaderCards()
                            .stream()
                            .filter(card -> card.toImgPath().equals(cardInButton.toImgPath()))
                            .findAny();
                    cardInBoardOptional.ifPresentOrElse(cardInBoard -> {
                                if (cardInBoard.getAbility().equals(LeaderCardAbility.STORE)) {
                                    IntStream.range(0, 2).forEach(i -> {
                                        HBox resourcesHBox =
                                                (HBox) ((StackPane) leaderCardButton.getGraphic()).getChildren().get(1);
                                        resourcesHBox.getChildren().get(i)
                                                .setVisible(((DumbStoreLeaderCard) cardInBoard).getResourceCount() > i);
                                    });
                                }
                            },
                            () -> leaderCardButton.setVisible(false));
                });

    }

    private void renderWarehouseDepots(DumbPersonalBoard dumbPersonalBoard) {
        LinkedHashMap<Resource, Integer> depotsResources =
                new LinkedHashMap<>(dumbPersonalBoard.getDepots().getStoredResources());
        // Clear all rendered images of the depots
        IntStream.range(0, 3).forEach(i -> warehouseDepotsResourceImages.get(i)
                .forEach(imageView -> imageView.setImage(null)));
        // Magic
        IntStream.range(3 - depotsResources.size(), 3).forEach(i -> {
            int depotsIndex = i + depotsResources.size() - 3;
            Resource resourceAtIndex = new ArrayList<>(depotsResources.keySet()).get(depotsIndex);
            List<ImageView> resourceSlots = warehouseDepotsResourceImages.get(i);
            IntStream.range(0, resourceSlots.size()).forEach(j -> resourceSlots.get(j).setImage(
                    j < depotsResources.get(resourceAtIndex)
                            ? getImageFromPath(resourceAtIndex.toImgPath())
                            : null
            ));
        });
    }

    public void renderFaithTrack(DumbPersonalBoard dumbPersonalBoard) {
        DumbFaithTrack faithTrack = dumbPersonalBoard.getFaithTrack();
        GridCoordinates coordinates =
                faithTrackCoordsReference.get(faithTrack.getFaithMarkerPosition());
        Platform.runLater(() -> {
            faithTrackGrid.getChildren().clear();
            faithTrackGrid.add(faithMarker, coordinates.getColumn(), coordinates.getRow());
            renderPopesFavor(firstPopesFavor, faithTrack.getPopesFavors().get(0), 1);
            renderPopesFavor(secondPopesFavor, faithTrack.getPopesFavors().get(1), 2);
            renderPopesFavor(thirdPopesFavor, faithTrack.getPopesFavors().get(2), 3);
        });
    }

    private void renderPopesFavor(ImageView popesFavorImageView, FavorStatus favorStatus,
                                  int index) {
        popesFavorImageView.setImage(
                switch (favorStatus) {
                    case ACTIVE -> getImageFromPath("/img/faithtrack/pope_favor" + index +
                            "_front.png");
                    case INACTIVE -> getImageFromPath("/img/faithtrack/pope_favor" + index +
                            "_back.png");
                    case DISCARDED -> null;
                }
        );
    }

    private void renderStrongbox(DumbPersonalBoard dumbPersonalBoard) {
        Map<Resource, Integer> strongboxResourcesMap = dumbPersonalBoard.getStrongbox().getStoredResources();
        Platform.runLater(() -> {
            strongboxCoinQuantity.setText(resourceAmountInStrongbox(strongboxResourcesMap, Resource.COIN));
            strongboxServantQuantity.setText(resourceAmountInStrongbox(strongboxResourcesMap, Resource.SERVANT));
            strongboxShieldQuantity.setText(resourceAmountInStrongbox(strongboxResourcesMap, Resource.SHIELD));
            strongboxStoneQuantity.setText(resourceAmountInStrongbox(strongboxResourcesMap, Resource.STONE));
        });
    }

    private String resourceAmountInStrongbox(Map<Resource, Integer> strongboxResourcesMap, Resource resource) {
        return "x" + (strongboxResourcesMap.get(resource) == null ? "0" :
                strongboxResourcesMap.get(resource));
    }

    private void renderDevelopmentCardSlots(DumbPersonalBoard dumbPersonalBoard) {
        IntStream.range(0, 3).forEach(i -> {
            StackPane slotPane = devCardSlotsStackPanes.get(0);
            DumbDevelopmentCard peekedCard = dumbPersonalBoard.getDevelopmentCardSlots().get(i).peek();
            synchronized (slotPane) {
                Optional<DumbDevelopmentCard> paneCard =
                        Optional.ofNullable((DumbDevelopmentCard) slotPane.getUserData());
                paneCard.ifPresentOrElse(cardInUserData -> {
                    if (!cardInUserData.toImgPath().equals(peekedCard.toImgPath())) {
                        addDevelopmentCardToStackPane(peekedCard, slotPane);
                    }
                }, () -> {
                    if (peekedCard != null) {
                        addDevelopmentCardToStackPane(peekedCard, slotPane);
                    }
                });
            }
        });
    }

    private void addDevelopmentCardToStackPane(DumbDevelopmentCard dumbDevelopmentCard,
                                               StackPane stackPane) {
        stackPane.setUserData(dumbDevelopmentCard);
        ToggleButton toggleButton = new ToggleButton();
        ImageView imageView = new ImageView(getImageFromPath(dumbDevelopmentCard.toImgPath()));
        imageView.setFitHeight(210);
        imageView.setPreserveRatio(true);
        toggleButton.setGraphic(imageView);
        toggleButton.setTranslateY(stackPane.getChildren().size() * (-50));
        stackPane.getChildren().add(toggleButton);
    }

    public void initLeaderCardsSelection(List<DumbLeaderCard> leaderCards) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        leaderCards.forEach(card -> hBox.getChildren().add(createPreGameLeaderCardToggleButton(card)));
        preGameLeaderCardSelectionLayer.getChildren().add(hBox);
        bringToFront(confirmResetLayer, preGameLeaderCardSelectionLayer);
        ConfirmResetButtonsStrategy.PRE_GAME_LEADER_CARDS_CHOICE.applyTo(confirmButton,
                resetButton);
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
                populateInfoLabel("Confirmed.");
            } else {
                populateInfoLabel("Invalid Selection");
            }
        }
    }

    private void endLeaderCardsSelection() {
        ConfirmResetButtonsStrategy.NONE.applyTo(confirmButton, resetButton);
        bringToFront(actionsLayer, personalBoardLayer);
    }

    public void initResourceChoice() {
        int ownPosition = gui.getDumbModel().getOwnPersonalBoard().getPosition();
        logger.info(String.valueOf(ownPosition));
        if (ownPosition > 1) {
            ConfirmResetButtonsStrategy.PRE_GAME_RESOURCES_CHOICE.applyTo(confirmButton,
                    resetButton);
            switch (ownPosition) {
                case 2, 3 -> populateInfoLabel("Pick a resource!");
                case 4 -> populateInfoLabel("Pick two resources!");
            }
            bringToFront(confirmResetLayer, resourceChoiceLayer);
        }
    }

    private void addResourceToChoiceMap(Resource resource) {
        chosenResourcesMap.compute(resource, (k, v) -> (v == null) ? 1 : v + 1);
        StringBuilder resourceMap = new StringBuilder("You have selected: ");
        for (Resource res : chosenResourcesMap.keySet()) {
            resourceMap.append("\n").append(chosenResourcesMap.get(res)).append(" ").append(res);
        }
        populateInfoLabel(resourceMap.toString());
    }

    private void confirmPreGameResourceChoice() {
        if (gui.getInputVerifier().canPickResources(chosenResourcesMap)) {
            gui.getClientSocket().sendAction(new PregameResourceChoiceAction(chosenResourcesMap));
            populateInfoLabel("Submitted resource choice");
            endPreGameResourceChoice();
        } else {
            reset();
            populateInfoLabel("Illegal resource choice, please retry.");
        }
    }

    private void endPreGameResourceChoice() {
        ConfirmResetButtonsStrategy.NONE.applyTo(confirmButton, resetButton);
        personalBoardLayer.toFront();
    }

    private Image getImageFromPath(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }

    private void bringToFront(Node left, Node right) {
        Platform.runLater(() -> {
            left.toFront();
            right.toFront();
        });
    }
}
