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

    /**
     * Initialize the Common scene by creating toggle groups and adding to them buttons
     */
    @FXML
    private void initialize(){
        toggleMarketGroup = new ToggleGroup();

        marketButtons = new ArrayList<>();

        invisibleDevelopmentPane.toFront();

        addMarketButtons();

        toggleDevelopmentGroup = new ToggleGroup();
    }

    /**
     * Creates toggle buttons for market and add them to toggleMarketGroup
     */
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

    /**
     * initialize Common scene by setting gui, all graphics and adding development cards
     * @param gui is the gui of the client
     */
    public void initialize(GUI gui) {
        super.setGui(gui);

        setMarketGraphic();

        addDevelopmentCards();

        initializeGridPaneDevelopmentCardArray();

        setDevelopmentCardsGraphic();
    }

    /**
     * initialize an array that contains all children of gridDevelopmentCard
     */
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

    /**
     * set market graphic by adding all images of marbles
     */
    private void setMarketGraphic(){
        Platform.runLater(() -> {
            imageViewExtraMarble.setImage(getMarbleImage(-1, -1));

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

    /**
     *
     * @param row is the row of the grid pane of development cards that is consider to place the image
     * @param column is the column of the grid pane of development cards that is consider to place the image
     * @return path where to get a development card image
     */
    private ImageView getDevelopmentCardImage(int row, int column){
        ImageView imageView;
        if (gui.getDumbModel().getDevelopmentCardsBoard().getBoard()[row][column] != null){
            imageView = new ImageView(getImageFromPath(gui.getDumbModel().getDevelopmentCardsBoard().getBoard()[row][column].toImgPath()));
            imageView.setFitWidth(154);
            imageView.setPreserveRatio(true);
        }
        else {
            imageView = new ImageView(getImageFromPath("/img/deck_end.png"));
            imageView.setFitWidth(154);
            imageView.setFitHeight(232.67);
        }
        return imageView;
    }

    /**
     * method called by the Common Scene when the confirm button is clicked
     * depending on current ConfirmButtonStrategy calls another method to pick marbles or pick a development card
     */
    @FXML
    private void confirm() {
        switch ((ConfirmResetButtonsStrategy) confirmButton.getUserData()) {
            case PICK_MARBLES -> pickMarbles((ToggleButton) toggleMarketGroup.getSelectedToggle());
            case BUY_DEVELOPMENT_CARD -> pickDevelopmentCards((ToggleButton) toggleDevelopmentGroup.getSelectedToggle());
        }

    }

    /**
     *
     * @param row is the row of the market that is consider to place the image
     * @param column is the column of the market that is consider to place the image
     * @return path where to get a marble image
     */
    private Image getMarbleImage(int row, int column){
        String name;
        if (row==-1){
            name = gui.getDumbModel().getMarket().getExtraMarble().name();
        }
        else {
            name = gui.getDumbModel().getMarket().getMarket()[row][column].name();
        }
        String marblePath = switch (name) {
            case "WHITE" -> "/img/marbles/white.png";
            case "RED" -> "/img/marbles/red.png";
            case "GREY" -> "/img/marbles/grey.png";
            case "BLUE" -> "/img/marbles/blue.png";
            case "YELLOW" -> "/img/marbles/yellow.png";
            case "PURPLE" -> "/img/marbles/purple.png";
            default -> null;
        };

        return getImageFromPath(marblePath);
    }

    /**
     * populate a label with something to communicate to the user
     * @param outcome is the string that will be printed
     */
    private void populateInfoLabel(String outcome) {
        Platform.runLater(() -> alertsLabel.setText(outcome));
    }

    /**
     * call the method to verify if the action il legal, and if it is send the action to the server to be performed
     * @param toggleButton represents the row or the column choose by the user
     */
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

    /**
     * via the chosen toggle button get the chosen development card and sand it to the gui
     * @param toggleButton is the button that represents the chosen leader card
     */
    private void pickDevelopmentCards(ToggleButton toggleButton) {
        DumbDevelopmentCard chosenCard = (DumbDevelopmentCard) toggleButton.getUserData();
        resetCommon();
        gui.getPersonalController().startDevelopmentSlotSelection(chosenCard);
    }

    /**
     * this class represents a couple with a string and an integer that are associate to each market buttons
     * the string can be "row" or "column", depending if the button select a row or a column
     * integer represents number of column or row
     */
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

    /**
     * Set to front market toggles, confirm and reset buttons, and set ConfirmReset button strategy to pick marbles
     */
    @FXML
    private void pickFromMarket(){
        VBoxMarket.toFront();
        HBoxMarket.toFront();
        confirmResetBox.toFront();
        ConfirmResetButtonsStrategy.PICK_MARBLES.applyTo(confirmButton, resetButton);
    }

    /**
     * @param choose can be "row" or "columns"
     * @return true only if choose is row
     */
    private boolean isRow(String choose){
        return choose.equals("row");
    }

    /**
     * reset common scene by setting all market and development card toggles not pushable
     */
    private void resetCommon(){
        actionBox.toFront();
        invisibleVerticalMarketPane.toFront();
        invisibleHorizontalMarketPane.toFront();
        invisibleDevelopmentPane.toFront();
        populateInfoLabel("");
        Optional.ofNullable(toggleMarketGroup.getSelectedToggle()).ifPresent(toggle ->toggle.selectedProperty().setValue(false));
        Optional.ofNullable(toggleDevelopmentGroup.getSelectedToggle()).ifPresent(toggle ->toggle.selectedProperty().setValue(false));
    }

    /**
     * change the scene to personal
     */
    @FXML
    private void backToPersonal(){
        gui.goToPersonalView();
    }

    /**
     * reset common scene by setting all market and development card toggles not pushable
     */
    @FXML
    private void reset(){
        actionBox.toFront();
        invisibleVerticalMarketPane.toFront();
        invisibleHorizontalMarketPane.toFront();
        invisibleDevelopmentPane.toFront();
        Optional.ofNullable(toggleMarketGroup.getSelectedToggle()).ifPresent(toggle ->toggle.selectedProperty().setValue(false));
        Optional.ofNullable(toggleDevelopmentGroup.getSelectedToggle()).ifPresent(toggle ->toggle.selectedProperty().setValue(false));
    }

    /**
     * set to front development cards buttons to allow to player to choose which he wants.
     * It also set confirmReset button strategy to BUY_DEVELOPMENT_CARD
     */
    @FXML
    private void buyDevelopmentCard(){
        gridDevelopmentCard.toFront();
        confirmResetBox.toFront();
        ConfirmResetButtonsStrategy.BUY_DEVELOPMENT_CARD.applyTo(confirmButton, resetButton);
    }

    /**
     * Add all toggle buttons representing development cards in the scene, set their user data and add all to a toggle group
     */
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

    /**
     * update user data to all development cards
     */
    private void updateDevelopmentCards(){
        for (int row = 0; row < 3; row++){
            for (int column = 0; column <4; column++){
                gridPaneDevelopmentCardArray[row][column].setUserData(gui.getDumbModel().getDevelopmentCardsBoard().getBoard()[row][column]);
            }
        }
    }

    /**
     * set all development toggle buttons images
     */
    private void setDevelopmentCardsGraphic(){
        Platform.runLater(() -> {
            for (int row = 0; row < 3; row++){
                for (int column = 0; column <4; column++){
                    ImageView imageView = getDevelopmentCardImage(row, column);
                    ToggleButton toggleButton = (ToggleButton) gridPaneDevelopmentCardArray[row][column];
                    toggleButton.setGraphic(imageView);
                    if (gui.getDumbModel().getDevelopmentCardsBoard().getBoard()[row][column] == null){
                        toggleButton.setDisable(true);
                    }
                }
            }
        });

    }

    /**
     * set all common scene graphic
     */
    public void renderCommonsBoard(){
        setDevelopmentCardsGraphic();
        setMarketGraphic();
        updateDevelopmentCards();
        Optional.ofNullable(toggleMarketGroup.getSelectedToggle()).ifPresent(toggle ->toggle.selectedProperty().setValue(false));
        Optional.ofNullable(toggleDevelopmentGroup.getSelectedToggle()).ifPresent(toggle ->toggle.selectedProperty().setValue(false));
    }
}
