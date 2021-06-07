package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.guicontrollers.JFXController;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbPersonalBoard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbStoreLeaderCard;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PregameLeaderCardsChoiceAction;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PregameResourceChoiceAction;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.utils.Resource;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InGamePersonalController extends JFXController {
    private final Logger logger = Logger.getLogger(getClass().getName());
    @FXML
    private ImageView leaderCard1;
    @FXML
    private HBox leaderCard1ResourcesHBox;
    @FXML
    private ImageView leaderCard2;
    @FXML
    private HBox leaderCard2ResourcesHBox;
    private final List<List<ImageView>> leaderCardResourcesImages = new ArrayList<>();
    private final List<ImageView> leaderCardImages = new ArrayList<>();
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
    private void initialize() {
        // Initialize Leader Card 1 Resource Images
        initLeaderCardStorageImages(leaderCard1ResourcesHBox);
        // Initialize Leader Card 2 Resource Images
        initLeaderCardStorageImages(leaderCard2ResourcesHBox);
        leaderCardImages.add(leaderCard1);
        leaderCardImages.add(leaderCard2);
        // Initialize Resource-choosing buttons
        resourceChoiceCoin.setOnAction(e -> addResourceToChoiceMap(Resource.COIN));
        resourceChoiceServant.setOnAction(e -> addResourceToChoiceMap(Resource.SERVANT));
        resourceChoiceShield.setOnAction(e -> addResourceToChoiceMap(Resource.SHIELD));
        resourceChoiceStone.setOnAction(e -> addResourceToChoiceMap(Resource.STONE));
    }

    @Override
    public void setGui(GUI gui) {
        super.setGui(gui);
        nicknameLabel.setText(gui.getPersonalNickname());
    }

    private void initLeaderCardStorageImages(HBox leaderCardResourceHBox) {
        ImageView resource1 = new ImageView();
        resource1.setFitWidth(33);
        resource1.setPreserveRatio(true);
        ImageView resource2 = new ImageView();
        resource2.setFitWidth(33);
        resource2.setPreserveRatio(true);
        resource2.setLayoutX(10);
        resource2.setLayoutY(10);
        leaderCardResourceHBox.getChildren()
                .addAll(resource1, resource2);
        List<ImageView> hBoxElements = new ArrayList<>();
        hBoxElements.add(resource1);
        hBoxElements.add(resource2);
        leaderCardResourcesImages.add(hBoxElements);
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

    public void renderPersonalBoard() {
        DumbPersonalBoard dumbPersonalBoard = gui.getDumbModel().getOwnPersonalBoard();
        renderPlayerInformation(dumbPersonalBoard);
        renderLeaderCards(dumbPersonalBoard);
        /*renderFaithTrack(dumbPersonalBoard);
        renderWarehouseDepots(dumbPersonalBoard);
        renderStrongbox(dumbPersonalBoard);
        renderFaithTrack(dumbPersonalBoard);
        renderDevelopmentCardSlots(dumbPersonalBoard);*/
    }

    private void renderPlayerInformation(DumbPersonalBoard dumbPersonalBoard) {
        positionIcon.setImage(getImageFromPath("/img/playericons/position_" + dumbPersonalBoard.getPosition() + ".PNG"));
        turnIcon.setImage(getImageFromPath("/img/playericons/turn_" + dumbPersonalBoard.getTurnStatus() + ".PNG"));
        connectionIcon.setImage(getImageFromPath("/img/playericons/" + (dumbPersonalBoard.getConnectionStatus() ? "connected" : "disconnected") + ".PNG"));
    }

    private void renderLeaderCards(DumbPersonalBoard dumbPersonalBoard) {
        List<DumbLeaderCard> leaderCards = dumbPersonalBoard.getLeaderCards();
        IntStream.range(0, 2).forEach(i -> {
            if (leaderCards.size() > i) {
                DumbLeaderCard card = leaderCards.get(i);
                leaderCardImages.get(i).setImage(getImageFromPath(card.toImgPath()));
                if (card.isActive() && card.getAbility().equals(LeaderCardAbility.STORE)) {
                    DumbStoreLeaderCard storeLeaderCard = (DumbStoreLeaderCard) card;
                    IntStream.range(0, 2).forEach(j ->
                            leaderCardResourcesImages.get(i).get(j)
                                    .setImage(storeLeaderCard.getResourceCount() > j
                                            ? getImageFromPath(storeLeaderCard.getStoredType().toImgPath())
                                            : null));
                }
            } else leaderCardImages.get(i).setImage(null);
        });
    }

    public void initLeaderCardsSelection(List<DumbLeaderCard> leaderCards) {
        initLeaderCardCheckBox(leaderCardCheckBox1, leaderCards.get(0));
        initLeaderCardCheckBox(leaderCardCheckBox2, leaderCards.get(1));
        initLeaderCardCheckBox(leaderCardCheckBox3, leaderCards.get(2));
        initLeaderCardCheckBox(leaderCardCheckBox4, leaderCards.get(3));
        preGameLeaderCardSelectionLayer.toFront();
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
