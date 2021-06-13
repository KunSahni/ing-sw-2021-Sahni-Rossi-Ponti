package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PregameLeaderCardsChoiceAction;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.PregameResourceChoiceAction;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import it.polimi.ingsw.server.model.utils.Resource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InGamePersonalController extends PlayerBoardController {
    private final Logger logger = Logger.getLogger(getClass().getName());
    @FXML
    private HBox leaderCardsHBox;
    private boolean areLeaderCardsInitialized = false;
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
    @FXML
    private TilePane visitLayer;
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
    @Override
    public void initialize() {
        super.initialize();
        // Initialize Resource-choosing buttons
        coinButton.setOnAction(e -> addResourceToChoiceMap(Resource.COIN));
        servantButton.setOnAction(e -> addResourceToChoiceMap(Resource.SERVANT));
        shieldButton.setOnAction(e -> addResourceToChoiceMap(Resource.SHIELD));
        stoneButton.setOnAction(e -> addResourceToChoiceMap(Resource.STONE));
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
        }
    }

    @FXML
    private void reset() {
        switch ((ConfirmResetButtonsStrategy) resetButton.getUserData()) {
            case PRE_GAME_RESOURCES_CHOICE -> {
                chosenResourcesMap.clear();
                populateInfoLabel("Resource choice cleared.");
            }
            case VISIT -> bringToFront(actionsLayer, personalBoardLayer);
        }
    }

    @FXML
    private void openVisitMenu() {
        ConfirmResetButtonsStrategy.VISIT.applyTo(confirmButton, resetButton);
        bringToFront(confirmResetLayer, visitLayer);
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

    private void initLeaderCardsDisplay(DumbPersonalBoard dumbPersonalBoard) {
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

    @Override
    protected void renderLeaderCards(DumbPersonalBoard dumbPersonalBoard) {
        synchronized (leaderCardsHBox) {
            if (!areLeaderCardsInitialized) {
                areLeaderCardsInitialized = true;
                initLeaderCardsDisplay(dumbPersonalBoard);
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
    }

    public void initLeaderCardsSelection(List<DumbLeaderCard> leaderCards) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        leaderCards.forEach(card -> hBox.getChildren().add(createPreGameLeaderCardToggleButton(card)));
        Platform.runLater(() -> preGameLeaderCardSelectionLayer.getChildren().add(hBox));
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
        bringToFront(actionsLayer, personalBoardLayer);
    }

    private void bringToFront(Node left, Node right) {
        Platform.runLater(() -> {
            left.toFront();
            right.toFront();
        });
    }
}
