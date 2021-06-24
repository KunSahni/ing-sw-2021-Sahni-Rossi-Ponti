package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.guicontrollers.JFXController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class FinalScreenController extends JFXController {

    @FXML
    private Label winnerLabel;

    @FXML
    private Label winnerPointsLabel;

    @FXML
    private HBox otherPlayersHBox;

    private List<VBox> othersVBox;

    private List<javafx.scene.control.Label> nameLabels;

    private List<javafx.scene.control.Label> victoryPointsLabels;

    @FXML
    private void initialize(){

    }

    public void initialize(GUI gui){
        super.setGui(gui);

        createVBox();
    }

    private void createVBox(){
        othersVBox = new ArrayList<>();
        for (int i=1; i<gui.getDumbModel().getSize(); i++){
            VBox vBox = new VBox();
            vBox.getChildren().add(i-1, new javafx.scene.control.Label());
            vBox.getChildren().add(new javafx.scene.control.Label());
            othersVBox.add(vBox);
            otherPlayersHBox.getChildren().add(vBox);
        }
    }

    public void renderFinal(TreeMap<Integer, String> finalScores, int finalScore){
        if (finalScores!=null){
            for (VBox box: othersVBox) {
                Object[] objects = box.getChildren().stream().sorted().toArray();
                javafx.scene.control.Label nameLabel = (javafx.scene.control.Label) objects[0];
                javafx.scene.control.Label victoryPointsLabel = (javafx.scene.control.Label) objects[1];
                nameLabel.setText(finalScores.firstEntry().getValue());
                victoryPointsLabel.setText(finalScores.firstKey().toString());
                finalScores.remove(finalScores.firstKey());
            }
            winnerLabel.setText(finalScores.lastEntry().getValue());
            winnerPointsLabel.setText(finalScores.lastKey().toString());
        }
        else{
            winnerLabel.setText(gui.getDumbModel().getNickname());
            winnerPointsLabel.setText(Integer.toString(finalScore));
        }
    }
}
