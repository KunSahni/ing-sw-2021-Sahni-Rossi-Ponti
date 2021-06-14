package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import it.polimi.ingsw.client.gui.guicontrollers.JFXController;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.network.clienttoserver.action.playeraction.TakeFromMarketAction;
import it.polimi.ingsw.server.model.market.MarketMarble;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Pane invisibleDevelopmentPane;

    private ToggleGroup toggleGroup;

    private List<List<ToggleButton>> marketButtons;

    private ToggleGroup toggleMarketGroup;

    @FXML
    private void initialize(){
        toggleMarketGroup = new ToggleGroup();

        marketButtons = new ArrayList<>();

        addMarketButtons();

        setMarketGraphic();
        toggleGroup = new ToggleGroup();
        toggleButton00.setToggleGroup(toggleGroup);
        toggleButton01.setToggleGroup(toggleGroup);
        toggleButton02.setToggleGroup(toggleGroup);
        toggleButton03.setToggleGroup(toggleGroup);
        toggleButton10.setToggleGroup(toggleGroup);
        toggleButton11.setToggleGroup(toggleGroup);
        toggleButton12.setToggleGroup(toggleGroup);
        toggleButton13.setToggleGroup(toggleGroup);
        toggleButton20.setToggleGroup(toggleGroup);
        toggleButton21.setToggleGroup(toggleGroup);
        toggleButton22.setToggleGroup(toggleGroup);
        toggleButton23.setToggleGroup(toggleGroup);

    }

    private void addMarketButtons(){
        marketButtons.add(0, new ArrayList<>());
        marketButtons.add(1, new ArrayList<>());

        for (int i=0; i<3; i++){
            ToggleButton toggleButton = new ToggleButton();
            toggleButton.setMaxSize(50.0, 38.0);
            toggleButton.setMinSize(50.0, 38.0);
            toggleButton.setUserData(new Couple(i, "row"));
            toggleButton.setToggleGroup(toggleMarketGroup);
            VBoxMarket.getChildren().add(i, toggleButton);
            marketButtons.get(0).add(i, toggleButton);
        }

        for (int i=0; i<4; i++){
            ToggleButton toggleButton = new ToggleButton();
            toggleButton.setMaxSize(50.0, 38.0);
            toggleButton.setMinSize(50.0, 38.0);
            toggleButton.setUserData(new Couple(i, "column"));
            toggleButton.setToggleGroup(toggleMarketGroup);
            HBoxMarket.getChildren().add(i, toggleButton);
            marketButtons.get(1).add(i, toggleButton);
        }
    }

    @FXML
    private Label alertsLabel;

    private String getDevelopmentcardImage(int row, int column){
        return gui.getDumbModel().getDevelopmentCardsBoard().getBoard()[row][column].toImgPath();
    }

    private DumbDevelopmentCard getDevelopmentcard(int row, int column){
        return gui.getDumbModel().getDevelopmentCardsBoard().getBoard()[row][column];
    }

    public void renderCommon(){
        this.imageView00 = new ImageView(getDevelopmentcardImage(0, 0));
        this.imageView01 = new ImageView(getDevelopmentcardImage(0, 1));
        this.imageView02 = new ImageView(getDevelopmentcardImage(0, 2));
        this.imageView03 = new ImageView(getDevelopmentcardImage(0, 3));
        this.imageView10 = new ImageView(getDevelopmentcardImage(1, 0));
        this.imageView11 = new ImageView(getDevelopmentcardImage(1, 1));
        this.imageView12 = new ImageView(getDevelopmentcardImage(1, 2));
        this.imageView13 = new ImageView(getDevelopmentcardImage(1, 3));
        this.imageView20 = new ImageView(getDevelopmentcardImage(2, 0));
        this.imageView21 = new ImageView(getDevelopmentcardImage(2, 1));
        this.imageView22 = new ImageView(getDevelopmentcardImage(2, 2));
        this.imageView23 = new ImageView(getDevelopmentcardImage(2, 3));
        toggleButton00.setUserData(getDevelopmentcard(0, 0));
        toggleButton00.setUserData(getDevelopmentcard(0, 1));
        toggleButton00.setUserData(getDevelopmentcard(0, 2));
        toggleButton00.setUserData(getDevelopmentcard(0, 3));
        toggleButton00.setUserData(getDevelopmentcard(1, 0));
        toggleButton00.setUserData(getDevelopmentcard(1, 1));
        toggleButton00.setUserData(getDevelopmentcard(1, 2));
        toggleButton00.setUserData(getDevelopmentcard(1, 3));
        toggleButton00.setUserData(getDevelopmentcard(2, 0));
        toggleButton00.setUserData(getDevelopmentcard(2, 1));
        toggleButton00.setUserData(getDevelopmentcard(2, 2));
        toggleButton00.setUserData(getDevelopmentcard(2, 3));
    }

    @FXML
    private void confirm() {
        pickMarbles((ToggleButton) toggleMarketGroup.getSelectedToggle());
    }

    private String getMarbleImage(int row, int column){
        return switch (gui.getDumbModel().getMarket().getMarket()[row][column].getMarbleColor()) {
            case "WHITE" -> "src/main/resources/img/marbles/whiteMarble.png";
            case "RED" -> "src/main/resources/img/marbles/redMarble.png";
            case "GREY" -> "src/main/resources/img/marbles/greyMarble.png";
            case "BLUE" -> "src/main/resources/img/marbles/blueMarble.png";
            case "YELLOW" -> "src/main/resources/img/marbles/yellowMarble.png";
            case "PURPLE" -> "src/main/resources/img/marbles/purpleMarble.png";
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

    /**
     * @param choose is "column" or "row" depending on the user choose
     * @param index is the index of row or column chosen by the user
     * @return marbles in the column or row chosen
     */
    private Map<MarketMarble, Integer> getMarbles(String choose, Integer index){
        switch (choose){
            case "row": {
                Map<MarketMarble, Integer> marbles = new HashMap<>();
                for (MarketMarble marble: gui.getDumbModel().getMarket().getMarket()[index]) {
                    if (marbles.containsKey(marble)){
                        int quantity = marbles.remove(marble);
                        marbles.put(marble, quantity+1);
                    }
                    else {
                        marbles.put(marble, 1);
                    }
                }

                return marbles;
            }

            case "column": {
                Map<MarketMarble, Integer> marbles = new HashMap<>();
                for (int i = 0; i < 3; i++){
                    MarketMarble marble = gui.getDumbModel().getMarket().getMarket()[i][index];
                    if (marbles.containsKey(marble)){
                        int quantity = marbles.remove(marble);
                        marbles.put(marble, quantity+1);
                    }
                    else {
                        marbles.put(marble, 1);
                    }
                }

                return marbles;
            }

            default: return null;
        }
    }

    private boolean marketChoose(ToggleButton toggleButton) {
        return false;
    }

    private class Couple {
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

//        for (int i = 0; i < 3; i++){
//            for (int j = 0; j<4; j++){
//                ImageView imageView = (ImageView) getNodeByRowColumnIndex(i, j);
//                imageView.setImage(new Image(getMarbleImage(0, 0)));
//            }
//        }
    }

    public Node getNodeByRowColumnIndex (final int row, final int column) {
        Node result = null;
        ObservableList<Node> childrens = gridMarket.getChildren();

        for (Node node : childrens) {
            if(gridMarket.getRowIndex(node) == row && gridMarket.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    private boolean isRow(String choose){
        if (choose.equals("row")){
            return true;
        }
        else {
            return false;
        }
    }

    private void resetCommon(){
        actionBox.toFront();
        invisibleVerticalMarketPane.toFront();
        invisibleHorizontalMarketPane.toFront();
        invisibleDevelopmentPane.toFront();
    }
}
