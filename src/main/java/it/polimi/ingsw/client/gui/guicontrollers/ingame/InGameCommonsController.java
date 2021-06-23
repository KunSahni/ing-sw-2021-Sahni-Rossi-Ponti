package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.guicontrollers.JFXController;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.TakeFromMarketAction;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class InGameCommonsController extends JFXController {

    @FXML
    private Button confirmButton;

    @FXML
    private ImageView imageViewExtraMarble;

    @FXML
    private VBox VBoxMarket;

    @FXML
    private HBox HBoxMarket;

    @FXML
    private Pane invisibleHorizontalMarketPane;

    @FXML
    private Pane invisibleVerticalMarketPane;

    @FXML
    private Button resetButton;

    @FXML
    private VBox actionBox;

    @FXML
    private VBox confirmResetBox;

    @FXML
    private GridPane gridMarket;

    @FXML
    private Pane invisibleDevelopmentPane;

    @FXML
    private GridPane gridDevelopmentCard;

    private ToggleGroup toggleDevelopmentGroup;

    private List<List<ToggleButton>> marketButtons;

    private ToggleGroup toggleMarketGroup;

    private Node[][] gridPaneDevelopmentCardArray = null;

    @FXML
    private void initialize(){
        toggleMarketGroup = new ToggleGroup();

        marketButtons = new ArrayList<>();

        invisibleDevelopmentPane.toFront();

        addMarketButtons();

        toggleDevelopmentGroup = new ToggleGroup();
    }

    private void addMarketButtons(){
        marketButtons.add(0, new ArrayList<>());
        marketButtons.add(1, new ArrayList<>());

        for (int i=0; i<3; i++){
            ToggleButton toggleButton = new ToggleButton();
            toggleButton.setMaxWidth(35.0);
            toggleButton.setMinWidth(35.0);
            toggleButton.setMaxHeight(56.5);
            toggleButton.setMinHeight(56.5);
            toggleButton.setUserData(new Couple(i, "row"));
            toggleButton.setToggleGroup(toggleMarketGroup);
            VBoxMarket.getChildren().add(i, toggleButton);
            marketButtons.get(0).add(i, toggleButton);
        }

        for (int i=0; i<4; i++){
            ToggleButton toggleButton = new ToggleButton();
            toggleButton.setMaxWidth(51.25);
            toggleButton.setMinWidth(51.25);
            toggleButton.setMaxHeight(40.0);
            toggleButton.setMinHeight(40.0);
            toggleButton.setUserData(new Couple(i, "column"));
            toggleButton.setToggleGroup(toggleMarketGroup);
            HBoxMarket.getChildren().add(i, toggleButton);
            marketButtons.get(1).add(i, toggleButton);
        }
    }

    public void initialize(GUI gui) {
        super.setGui(gui);

        setMarketGraphic();

        addDevelopmentCards();

        initializeGridPaneDevelopmentCardArray();

        setDevelopmentCardsGraphic();
    }

    private void initializeGridPaneDevelopmentCardArray()
    {
        this.gridPaneDevelopmentCardArray = new Node[3][4];
        for(Node node : this.gridDevelopmentCard.getChildren())
        {
            if (node.hasProperties()) {
                this.gridPaneDevelopmentCardArray[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] = node;
            }
        }
    }

    private void setMarketGraphic(){
        Platform.runLater(() -> {
            imageViewExtraMarble.setImage(new Image(getMarbleImage(-1, -1)));

            for (int i = 0; i < 3; i++){
                for (int j = 0; j<4; j++){
                    ImageView imageView = new ImageView(getMarbleImage(i, j));
                    imageView.setFitWidth(51.25);
                    imageView.setFitHeight(56.67);
                    gridMarket.add(imageView, j, i);
                }
            }
        });
    }

    @FXML
    private Label alertsLabel;

    private String getDevelopmentCardImage(int row, int column){
        String path;
        path = gui.getDumbModel().getDevelopmentCardsBoard().getBoard()[row][column].toImgPath().toLowerCase(Locale.ROOT);
        return path;
    }

    @FXML
    private void confirm() {
        switch ((ConfirmResetButtonsStrategy) confirmButton.getUserData()) {
            case PICK_MARBLES -> pickMarbles((ToggleButton) toggleMarketGroup.getSelectedToggle());
            case BUY_DEVELOPMENT_CARD -> pickDevelopmentCards((ToggleButton) toggleDevelopmentGroup.getSelectedToggle());
        }

    }

    private String getMarbleImage(int row, int column){
        String name;
        if (row==-1){
            name = gui.getDumbModel().getMarket().getExtraMarble().name();
        }
        else {
            name = gui.getDumbModel().getMarket().getMarket()[row][column].name();
        }
        return switch (name) {
            case "WHITE" -> "img/marbles/white.png";
            case "RED" -> "img/marbles/red.png";
            case "GREY" -> "img/marbles/grey.png";
            case "BLUE" -> "img/marbles/blue.png";
            case "YELLOW" -> "img/marbles/yellow.png";
            case "PURPLE" -> "img/marbles/purple.png";
            default -> null;
        };
    }

    private void populateInfoLabel(String outcome) {
        Platform.runLater(() -> alertsLabel.setText(outcome));
    }

    private void pickMarbles(ToggleButton toggleButton){
        String choose;
        int index;

        Couple couple = (Couple) toggleButton.getUserData();

        index = couple.getIndex();
        choose = couple.getChoose();

        if (gui.getInputVerifier().canTake(choose, index)){
             gui.getClientSocket().sendAction(new TakeFromMarketAction(index, isRow(choose)));
             populateInfoLabel("Confirmed.");
             resetCommon();
             gui.goToPersonalView();
        }
        else {
            populateInfoLabel("Select one action");
        }

    }

    private void pickDevelopmentCards(ToggleButton toggleButton) {
        DumbDevelopmentCard chosenCard = (DumbDevelopmentCard) toggleButton.getUserData();
        resetCommon();
        gui.getPersonalController().startDevelopmentSlotSelection(chosenCard);
    }

    private static class Couple {
        Integer index;
        String choose;

        public Couple(Integer index, String choose) {
            this.index = index;
            this.choose = choose;
        }

        public Integer getIndex() {
            return index;
        }

        public String getChoose() {
            return choose;
        }
    }

    @FXML
    private void pickFromMarket(){
        VBoxMarket.toFront();
        HBoxMarket.toFront();
        confirmResetBox.toFront();
        ConfirmResetButtonsStrategy.PICK_MARBLES.applyTo(confirmButton, resetButton);
    }

    private boolean isRow(String choose){
        return choose.equals("row");
    }

    private void resetCommon(){
        actionBox.toFront();
        invisibleVerticalMarketPane.toFront();
        invisibleHorizontalMarketPane.toFront();
        invisibleDevelopmentPane.toFront();
        populateInfoLabel("");
        Optional.ofNullable(toggleMarketGroup.getSelectedToggle()).ifPresent(toggle ->toggle.selectedProperty().setValue(false));
        Optional.ofNullable(toggleDevelopmentGroup.getSelectedToggle()).ifPresent(toggle ->toggle.selectedProperty().setValue(false));
    }

    @FXML
    private void backToPersonal(){
        gui.goToPersonalView();
    }

    @FXML
    private void reset(){
        actionBox.toFront();
        invisibleVerticalMarketPane.toFront();
        invisibleHorizontalMarketPane.toFront();
        invisibleDevelopmentPane.toFront();
        Optional.ofNullable(toggleMarketGroup.getSelectedToggle()).ifPresent(toggle ->toggle.selectedProperty().setValue(false));
        Optional.ofNullable(toggleDevelopmentGroup.getSelectedToggle()).ifPresent(toggle ->toggle.selectedProperty().setValue(false));
    }

    @FXML
    private void buyDevelopmentCard(){
        gridDevelopmentCard.toFront();
        confirmResetBox.toFront();
        ConfirmResetButtonsStrategy.BUY_DEVELOPMENT_CARD.applyTo(confirmButton, resetButton);
    }

    private void addDevelopmentCards(){
        for (int row = 0; row < 3; row++){
            for (int column = 0; column <4; column++){
                ToggleButton toggleButton = new ToggleButton();
                toggleButton.setMinHeight(232.67);
                toggleButton.setMaxHeight(232.67);
                toggleButton.setMinWidth(154);
                toggleButton.setMaxWidth(154);
                toggleButton.setUserData(gui.getDumbModel().getDevelopmentCardsBoard().getBoard()[row][column]);
                gridDevelopmentCard.add(toggleButton, column, row);
                toggleButton.setToggleGroup(toggleDevelopmentGroup);
            }
        }
    }

    private void updateDevelopmentCards(){
        for (int row = 0; row < 3; row++){
            for (int column = 0; column <4; column++){
                gridPaneDevelopmentCardArray[row][column].setUserData(gui.getDumbModel().getDevelopmentCardsBoard().getBoard()[row][column]);
            }
        }
    }

    private void setDevelopmentCardsGraphic(){
        Platform.runLater(() -> {
            for (int row = 0; row < 3; row++){
                for (int column = 0; column <4; column++){
                    ImageView imageView = new ImageView(getDevelopmentCardImage(row, column));
                    imageView.setFitWidth(154);
                    imageView.setPreserveRatio(true);
                    ToggleButton toggleButton = (ToggleButton) gridPaneDevelopmentCardArray[row][column];
                    toggleButton.setGraphic(imageView);
                }
            }
        });

    }

    public void renderCommonsBoard(){
        setDevelopmentCardsGraphic();
        setMarketGraphic();
        updateDevelopmentCards();
        Optional.ofNullable(toggleMarketGroup.getSelectedToggle()).ifPresent(toggle ->toggle.selectedProperty().setValue(false));
        Optional.ofNullable(toggleDevelopmentGroup.getSelectedToggle()).ifPresent(toggle ->toggle.selectedProperty().setValue(false));
    }
}
