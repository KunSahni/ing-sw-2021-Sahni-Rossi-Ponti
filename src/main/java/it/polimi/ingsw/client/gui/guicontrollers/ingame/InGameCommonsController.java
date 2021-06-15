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

public class InGameCommonsController extends JFXController {
    @FXML
    private ImageView imageView00;

    @FXML
    private ImageView imageView01;

    @FXML
    private ImageView imageView02;

    @FXML
    private ImageView imageView03;

    @FXML
    private ImageView imageView10;

    @FXML
    private ImageView imageView11;

    @FXML
    private ImageView imageView12;

    @FXML
    private ImageView imageView13;

    @FXML
    private ImageView imageView20;

    @FXML
    private ImageView imageView21;

    @FXML
    private ImageView imageView22;

    @FXML
    private ImageView imageView23;

    @FXML
    private Button confirmButton;

    @FXML
    private ImageView imageViewMarble00;

    @FXML
    private ImageView imageViewMarble01;

    @FXML
    private ImageView imageViewMarble02;

    @FXML
    private ImageView imageViewMarble03;

    @FXML
    private ImageView imageViewMarble10;

    @FXML
    private ImageView imageViewMarble11;

    @FXML
    private ImageView imageViewMarble12;

    @FXML
    private ImageView imageViewMarble13;

    @FXML
    private ImageView imageViewMarble20;

    @FXML
    private ImageView imageViewMarble21;

    @FXML
    private ImageView imageViewMarble22;

    @FXML
    private ImageView imageViewMarble23;

    @FXML
    private ImageView imageViewExtraMarble;

    @FXML
    private ToggleButton toggleButton00;

    @FXML
    private ToggleButton toggleButton01;

    @FXML
    private ToggleButton toggleButton02;

    @FXML
    private ToggleButton toggleButton03;

    @FXML
    private ToggleButton toggleButton10;

    @FXML
    private ToggleButton toggleButton11;

    @FXML
    private ToggleButton toggleButton12;

    @FXML
    private ToggleButton toggleButton13;

    @FXML
    private ToggleButton toggleButton20;

    @FXML
    private ToggleButton toggleButton21;

    @FXML
    private ToggleButton toggleButton22;

    @FXML
    private ToggleButton toggleButton23;

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
    private Button pickFromMarketButton;

    @FXML
    private Button buyDevelopmentCardButton;

    @FXML
    private  Button backToPersonalBoardButton;

    @FXML
    private StackPane actionStackPane;

    @FXML
    private VBox actionBox;

    @FXML
    private VBox confirmResetBox;

    @FXML
    private GridPane gridMarket;

    @FXML
    private VBox marketVBox;

    @FXML
    private Pane invisibleDevelopmentPane;

    @FXML
    private GridPane gridDevelopmentCard;

    private ToggleGroup toggleDevelopmentGroup;

    private List<List<ToggleButton>> marketButtons;

    private ToggleGroup toggleMarketGroup;

    private Node[][] gridPaneMarketArray = null;

    @FXML
    private void initialize(){
        toggleMarketGroup = new ToggleGroup();

        marketButtons = new ArrayList<>();

        addMarketButtons();

        initializeGridPaneArray();

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

    @FXML
    private Label alertsLabel;

    private String getDevelopmentcardImage(int row, int column){
        String path;
        path = gui.getDumbModel().getDevelopmentCardsBoard().getBoard()[row][column].toImgPath().toLowerCase(Locale.ROOT)+".png";
        return path;
    }

    @FXML
    private void confirm() {
        pickMarbles((ToggleButton) toggleMarketGroup.getSelectedToggle());
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
        String choose = null;
        int index = 0;

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

        //todo: chiamare metodo della gui con cui passare la carta al InGamePersonal
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
    }

    private void setMarketGraphic(){

        imageViewExtraMarble.setImage(new Image(getMarbleImage(-1, -1)));

        for (int i = 0; i < 3; i++){
            for (int j = 0; j<4; j++){
                ImageView imageView = new ImageView(getMarbleImage(i, j));
                imageView.setFitWidth(51.25);
                imageView.setFitHeight(56.67);
                gridMarket.add(imageView, j, i);
            }
        }
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
    }

    private void initializeGridPaneArray()
    {
        for (int i = 0; i<3; i++){
            for (int j = 0; j<4; j++){
                gridMarket.add(new ImageView(), j, i);
            }
        }

        this.gridPaneMarketArray = new Node[3][4];
        for(Node node : this.gridMarket.getChildren())
        {
            if (node.hasProperties()) {
                this.gridPaneMarketArray[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] = node;
            }
        }
    }

    public void initialize(GUI gui) {
        super.setGui(gui);
        setMarketGraphic();
        addDevelopmentCards();
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
    }

    @FXML
    private void buyDevelopmentCard(){
        gridDevelopmentCard.toFront();
        confirmResetBox.toFront();
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
                toggleButton.setGraphic(new ImageView(getDevelopmentcardImage(row, column)));
                gridDevelopmentCard.add(toggleButton, column, row);
                toggleButton.setToggleGroup(toggleDevelopmentGroup);
            }
        }
    }
}
