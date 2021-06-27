package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.client.gui.guicontrollers.JFXController;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCardSlot;
import it.polimi.ingsw.client.utils.dumbobjects.DumbFaithTrack;
import it.polimi.ingsw.client.utils.dumbobjects.DumbPersonalBoard;
import it.polimi.ingsw.server.model.personalboard.FavorStatus;
import it.polimi.ingsw.server.model.utils.Resource;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public abstract class PlayerBoardController extends JFXController {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());
    protected boolean areLeaderCardsInitialized = false;
    @FXML
    protected Label nicknameLabel;
    @FXML
    protected Circle positionLed1;
    @FXML
    protected Circle positionLed2;
    @FXML
    protected Circle positionLed3;
    @FXML
    protected Circle positionLed4;
    private final List<Circle> positionLEDs = new ArrayList<>();
    @FXML
    protected Circle playingLed;
    @FXML
    protected Circle connectedLed;
    @FXML
    private VBox depotsVBox;
    private final List<List<ImageView>> warehouseDepotsResourceImages = new ArrayList<>();
    private Map<Resource, Integer> currentWarehouseDepotsDisplay = new HashMap<>();
    @FXML
    protected GridPane faithTrackGrid;
    protected ImageView faithMarker;
    protected List<GridCoordinates> faithTrackCoordsReference;
    @FXML
    protected ImageView firstPopesFavor;
    @FXML
    protected ImageView secondPopesFavor;
    @FXML
    protected ImageView thirdPopesFavor;
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
    protected final List<StackPane> devCardSlotsStackPanes = new ArrayList<>();

    @FXML
    public void initialize() {
        // Init LEDs
        playingLed.setUserData(false);
        connectedLed.setUserData(false);
        positionLEDs.add(positionLed1);
        positionLEDs.add(positionLed2);
        positionLEDs.add(positionLed3);
        positionLEDs.add(positionLed4);
        positionLEDs.forEach(led -> led.setUserData(false));
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
        resourcesRow.setMinHeight(63);
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

    public void setNicknameLabel(String nickname) {
        nicknameLabel.setText(nickname);
    }

    public void renderPersonalBoard(DumbPersonalBoard dumbPersonalBoard) {
        renderPlayerInformation(dumbPersonalBoard);
        renderLeaderCards(dumbPersonalBoard);
        renderWarehouseDepots(dumbPersonalBoard);
        renderFaithTrack(dumbPersonalBoard);
        renderStrongbox(dumbPersonalBoard);
        renderDevelopmentCardSlots(dumbPersonalBoard);
    }

    private void renderPlayerInformation(DumbPersonalBoard dumbPersonalBoard) {
        int position = dumbPersonalBoard.getPosition();
        if (positionLEDs.stream().noneMatch(led -> (boolean) led.getUserData()) && position != 0) {
            switchOnLed(positionLEDs.get(position - 1));
        }
        boolean isConnected = dumbPersonalBoard.getConnectionStatus();
        if ((boolean) connectedLed.getUserData() != isConnected) {
            if (isConnected) switchOnLed(connectedLed);
            else switchOffLed(connectedLed);
        }
        boolean isPlaying = dumbPersonalBoard.getTurnStatus();
        if ((boolean) playingLed.getUserData() != isPlaying) {
            if (isPlaying) switchOnLed(playingLed);
            else switchOffLed(playingLed);
        }
    }

    private void switchOnLed(Circle led) {
        led.getStyleClass().add("active-led");
        led.getStyleClass().remove("inactive-led");
        led.setUserData(true);
    }

    private void switchOffLed(Circle led) {
        led.getStyleClass().add("inactive-led");
        led.getStyleClass().remove("active-led");
        led.setUserData(false);
    }

    protected abstract void renderLeaderCards(DumbPersonalBoard dumbPersonalBoard);

    private void renderWarehouseDepots(DumbPersonalBoard dumbPersonalBoard) {
        LinkedHashMap<Resource, Integer> depotsResources =
                new LinkedHashMap<>(dumbPersonalBoard.getDepots().getStoredResources());
        if (!depotsResources.equals(currentWarehouseDepotsDisplay)) {
            currentWarehouseDepotsDisplay = depotsResources;
            // Clear all rendered images of the depots
            IntStream.range(0, 3).forEach(i -> warehouseDepotsResourceImages.get(i)
                    .forEach(imageView -> imageView.setImage(null)));
            // Magic
            IntStream.range(3 - depotsResources.size(), 3).forEach(i -> {
                int depotsIndex = i + depotsResources.size() - 3;
                Resource resourceAtIndex =
                        new ArrayList<>(depotsResources.keySet()).get(depotsIndex);
                List<ImageView> resourceSlots = warehouseDepotsResourceImages.get(i);
                IntStream.range(0, resourceSlots.size()).forEach(j -> resourceSlots.get(j).setImage(
                        j < depotsResources.get(resourceAtIndex)
                                ? getImageFromPath(resourceAtIndex.toImgPath())
                                : null
                ));
            });
        }
    }

    protected void renderFaithTrack(DumbPersonalBoard dumbPersonalBoard) {
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

    protected void renderPopesFavor(ImageView popesFavorImageView, FavorStatus favorStatus,
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
        Map<Resource, Integer> strongboxResourcesMap =
                dumbPersonalBoard.getStrongbox().getStoredResources();
        Platform.runLater(() -> {
            strongboxCoinQuantity.setText(resourceAmountInStrongbox(strongboxResourcesMap,
                    Resource.COIN));
            strongboxServantQuantity.setText(resourceAmountInStrongbox(strongboxResourcesMap,
                    Resource.SERVANT));
            strongboxShieldQuantity.setText(resourceAmountInStrongbox(strongboxResourcesMap,
                    Resource.SHIELD));
            strongboxStoneQuantity.setText(resourceAmountInStrongbox(strongboxResourcesMap,
                    Resource.STONE));
        });
    }

    private String resourceAmountInStrongbox(Map<Resource, Integer> strongboxResourcesMap,
                                             Resource resource) {
        return "x" + (strongboxResourcesMap.get(resource) == null ? "0" :
                strongboxResourcesMap.get(resource));
    }


    private void renderDevelopmentCardSlots(DumbPersonalBoard dumbPersonalBoard) {
        Platform.runLater(() -> {
            synchronized (devCardSlotsStackPanes) {
                IntStream.range(0, 3).forEach(i -> {
                    StackPane slotPane = devCardSlotsStackPanes.get(i);
                    DumbDevelopmentCardSlot dumbDevelopmentCardSlot = dumbPersonalBoard.getDevelopmentCardSlots().get(i);
                    if (slotPane.getChildren().size() != dumbDevelopmentCardSlot.getDevelopmentCards().size() + 1) {
                        addDevelopmentCardsToStackPane(slotPane, dumbDevelopmentCardSlot.getDevelopmentCards());
                    }
                });
            }
        });
    }

    private void addDevelopmentCardsToStackPane(StackPane stackPane,
                                                List<DumbDevelopmentCard> dumbDevelopmentCards) {
        stackPane.setUserData(dumbDevelopmentCards.get(dumbDevelopmentCards.size() - 1));
        IntStream.range(0, dumbDevelopmentCards.size()).forEach(i -> {
            DumbDevelopmentCard dumbDevelopmentCard = dumbDevelopmentCards.get(i);
            ToggleButton toggleButton = new ToggleButton();
            toggleButton.setUserData(dumbDevelopmentCard);
            ImageView imageView = new ImageView(getImageFromPath(dumbDevelopmentCard.toImgPath()));
            imageView.setFitHeight(210);
            imageView.setPreserveRatio(true);
            toggleButton.setGraphic(imageView);
            toggleButton.setTranslateY(i * (-50));
            stackPane.getChildren().add(toggleButton);
        });
    }
}
