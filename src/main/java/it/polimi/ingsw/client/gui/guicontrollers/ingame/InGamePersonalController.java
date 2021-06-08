package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.guicontrollers.JFXController;
import it.polimi.ingsw.client.utils.dumbobjects.DumbFaithTrack;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbPersonalBoard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbStoreLeaderCard;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PregameLeaderCardsChoiceAction;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PregameResourceChoiceAction;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.personalboard.FavorStatus;
import it.polimi.ingsw.server.model.utils.Resource;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InGamePersonalController extends JFXController {
    private final Logger logger = Logger.getLogger(getClass().getName());
    @FXML
    private ToggleButton leaderCard1;
    @FXML
    private HBox leaderCard1ResourcesHBox;
    @FXML
    private ToggleButton leaderCard2;
    @FXML
    private HBox leaderCard2ResourcesHBox;
    @FXML
    private Label nicknameLabel;
    @FXML
    private ImageView positionIcon;
    @FXML
    private ImageView turnIcon;
    @FXML
    private ImageView connectionIcon;
    @FXML
    private Button confirmButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label alertsLabel;
    @FXML
    private VBox personalBoardLayer;
    @FXML
    private VBox preGameLeaderCardSelectionLayer;
    @FXML
    private VBox resourceChoiceLayer;
    @FXML
    private CheckBox leaderCardCheckBox1;
    @FXML
    private CheckBox leaderCardCheckBox2;
    @FXML
    private CheckBox leaderCardCheckBox3;
    @FXML
    private CheckBox leaderCardCheckBox4;
    private final ObservableSet<CheckBox> selectedPreGameLeaderCardCheckBoxes =
            FXCollections.observableSet();
    @FXML
    private Button resourceChoiceCoin;
    @FXML
    private Button resourceChoiceServant;
    @FXML
    private Button resourceChoiceShield;
    @FXML
    private Button resourceChoiceStone;
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

    @Override
    public void setGui(GUI gui) {
        super.setGui(gui);
        nicknameLabel.setText(gui.getPersonalNickname());
    }

    @FXML
    private void initialize() {
        // Initialize Leader Card buttons
        leaderCard1.setVisible(false);
        leaderCard2.setVisible(false);
        // Initialize Resource-choosing buttons
        resourceChoiceCoin.setOnAction(e -> addResourceToChoiceMap(Resource.COIN));
        resourceChoiceServant.setOnAction(e -> addResourceToChoiceMap(Resource.SERVANT));
        resourceChoiceShield.setOnAction(e -> addResourceToChoiceMap(Resource.SHIELD));
        resourceChoiceStone.setOnAction(e -> addResourceToChoiceMap(Resource.STONE));
        // Initialize WarehouseDepots Images
        IntStream.range(1, 4).forEach(this::initWarehouseDepotsResourceImages);
        // Initialize a list that will be used to position the marker in the correct
        // position on the faith track.
        initFaithTrackCoordsReferenceList();
        faithMarker = new ImageView(getImageFromPath("/img/faithtrack/faith_marker.png"));
        faithMarker.setFitHeight(60);
        faithMarker.setPreserveRatio(true);
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
        Platform.runLater(() -> alertsLabel.setText(outcome));
    }

    public void initLeaderCardsDisplay() {
        // Only called once, after the server has confirmed the player's initial
        // leader cards choice
        List<DumbLeaderCard> cards = gui.getDumbModel().getOwnPersonalBoard().getLeaderCards();
        DumbLeaderCard firstCard = cards.get(0);
        initLeaderCardToggleButton(firstCard, leaderCard1);
        if (firstCard.getAbility().equals(LeaderCardAbility.STORE))
            initStoreLeaderCardImages(leaderCard1ResourcesHBox,
                    ((DumbStoreLeaderCard) firstCard).getStoredType());
        DumbLeaderCard secondCard = cards.get(1);
        initLeaderCardToggleButton(secondCard, leaderCard2);
        if (secondCard.getAbility().equals(LeaderCardAbility.STORE))
            initStoreLeaderCardImages(leaderCard2ResourcesHBox,
                    ((DumbStoreLeaderCard) secondCard).getStoredType());
    }

    private void initLeaderCardToggleButton(DumbLeaderCard card, ToggleButton button) {
        button.setUserData(card);
        String image = getClass().getResource(card.toImgPath()).toExternalForm();
        button.setStyle("-fx-background-image: url('"+ image +"')");
        button.setVisible(true);
    }

    private void initStoreLeaderCardImages(HBox leaderCardResourceHBox, Resource storedResource) {
        leaderCardResourceHBox.getChildren()
                .addAll(create45x45ResourceInvisibleImageView(storedResource),
                        create45x45ResourceInvisibleImageView(storedResource));
    }

    private ImageView create45x45ResourceInvisibleImageView(Resource resource) {
        ImageView image = new ImageView(getImageFromPath(resource.toImgPath()));
        image.setFitWidth(45);
        image.setPreserveRatio(true);
        image.setVisible(false);
        return image;
    }

    public void renderPersonalBoard() {
        DumbPersonalBoard dumbPersonalBoard = gui.getDumbModel().getOwnPersonalBoard();
        renderPlayerInformation(dumbPersonalBoard);
        renderLeaderCards(dumbPersonalBoard);
        renderWarehouseDepots(dumbPersonalBoard);
        renderFaithTrack(dumbPersonalBoard);
        /*renderStrongbox(dumbPersonalBoard);
        renderDevelopmentCardSlots(dumbPersonalBoard);*/
    }

    private void renderPlayerInformation(DumbPersonalBoard dumbPersonalBoard) {
        positionIcon.setImage(getImageFromPath("/img/playericons/position_" + dumbPersonalBoard.getPosition() + ".PNG"));
        turnIcon.setImage(getImageFromPath("/img/playericons/turn_" + dumbPersonalBoard.getTurnStatus() + ".PNG"));
        connectionIcon.setImage(getImageFromPath("/img/playericons/" + (dumbPersonalBoard.getConnectionStatus() ? "connected" : "disconnected") + ".PNG"));
    }

    private void renderLeaderCards(DumbPersonalBoard dumbPersonalBoard) {
        /*List<DumbLeaderCard> leaderCards = dumbPersonalBoard.getLeaderCards();
        IntStream.range(0, 2).forEach(i -> {
            if (leaderCards.size() > i) {
                DumbLeaderCard card = leaderCards.get(i);
                ToggleButton cardButton = leaderCardToggleButtons.get(i);
                if (cardButton.isVisible()) {
                    if (card.isActive()) {
                        if (cardButton.getStyleClass().contains("inactive-leader-card")) {
                            cardButton.getStyleClass().clear();
                            cardButton.getStyleClass().add("active-leader-card");
                        }
                        if (card.getAbility().equals(LeaderCardAbility.STORE) {
                            DumbStoreLeaderCard storeLeaderCard = (DumbStoreLeaderCard) card;
                            IntStream.range(0, 2).forEach(j ->
                                    leaderCardResourcesImages.get(i).get(j)
                                            .setImage(storeLeaderCard.getResourceCount() > j
                                                    ?
                                                    getImageFromPath(storeLeaderCard.getStoredType()
                                                    .toImgPath())
                                                    : null));
                        }
                    }
                } else {
                    cardButton.getStyleClass().add("inactive-leader-card");
                    cardButton.setStyle("-fx-background-image: " + getImageFromPath(card.toImgPath()));
                    cardButton.setVisible(true);
                }
            } else leaderCardToggleButtons.get(i).setVisible(false);
        });*/
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

    public void initLeaderCardsSelection(List<DumbLeaderCard> leaderCards) {
        initLeaderCardCheckBox(leaderCardCheckBox1, leaderCards.get(0));
        initLeaderCardCheckBox(leaderCardCheckBox2, leaderCards.get(1));
        initLeaderCardCheckBox(leaderCardCheckBox3, leaderCards.get(2));
        initLeaderCardCheckBox(leaderCardCheckBox4, leaderCards.get(3));
        // preGameLeaderCardSelectionLayer.toFront();
        ConfirmResetButtonsStrategy.PRE_GAME_LEADER_CARDS_CHOICE.applyTo(confirmButton,
                resetButton);
    }

    private void initLeaderCardCheckBox(CheckBox checkBox, DumbLeaderCard card) {
        checkBox.setUserData(card);
        checkBox.setText(card.toImgPath());
        checkBox.selectedProperty().addListener((observable, oldValue, selectedNow) -> {
            if (selectedNow) {
                selectedPreGameLeaderCardCheckBoxes.add(checkBox);
            } else {
                selectedPreGameLeaderCardCheckBoxes.remove(checkBox);
            }
        });
    }

    private void pickLeaderCards() {
        if (selectedPreGameLeaderCardCheckBoxes.size() != 2) {
            populateInfoLabel("Select two cards.");
        } else {
            List<DumbLeaderCard> chosenCards = selectedPreGameLeaderCardCheckBoxes.stream()
                    .map(CheckBox::getUserData)
                    .map(object -> (DumbLeaderCard) object)
                    .collect(Collectors.toList());
            if (gui.getInputVerifier().canPickLeaderCards(chosenCards)) {
                gui.getClientSocket().sendAction(new PregameLeaderCardsChoiceAction(chosenCards));
                populateInfoLabel("Confirmed.");
                endLeaderCardsSelection();
            } else {
                populateInfoLabel("Invalid Selection");
            }
        }
    }

    private void endLeaderCardsSelection() {
        ConfirmResetButtonsStrategy.NONE.applyTo(confirmButton, resetButton);
        personalBoardLayer.toFront();
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
            Platform.runLater(() -> resourceChoiceLayer.toFront());
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
}
