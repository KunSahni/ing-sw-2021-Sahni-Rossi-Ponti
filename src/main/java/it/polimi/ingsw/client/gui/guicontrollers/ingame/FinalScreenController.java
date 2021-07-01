package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.guicontrollers.JFXController;
import it.polimi.ingsw.network.clienttoserver.messages.ExitRequestMessage;
import it.polimi.ingsw.network.servertoclient.renderable.updates.MultiPlayerGameOutcomeUpdate;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
            vBox.setMinHeight(130.0);
            vBox.setMaxHeight(130.0);
            vBox.setMinWidth(180.0);
            vBox.setMaxWidth(180.0);
            vBox.setAlignment(Pos.CENTER);
            othersVBox.add(vBox);
            otherPlayersHBox.getChildren().add(vBox);
        }
    }

    public void renderFinal(List<MultiPlayerGameOutcomeUpdate.ScoreTuple> finalScores, int finalScore){

        if (finalScores!=null){
            int i = 2;

            winnerLabel.setText(finalScores.get(0).getName());
            winnerPointsLabel.setText(String.valueOf(finalScores.get(0).getScore()));
            finalScores.remove(0);

            for (VBox box: othersVBox) {
                Label nameLabel = new Label(finalScores.get(0).getName());
                Label pointsLabel = new Label(String.valueOf(finalScores.get(0).getScore()));
                Label positionLabel = new Label();

                nameLabel.setMaxHeight(43.33);
                nameLabel.setMinHeight(43.33);
                nameLabel.setMaxWidth(180.0);
                nameLabel.setMinWidth(180.0);
                nameLabel.setTextFill(Color.WHITE);
                nameLabel.setFont(new Font("Bernard MT Condensed", 28));

                pointsLabel.setMaxHeight(43.33);
                pointsLabel.setMinHeight(43.33);
                pointsLabel.setMaxWidth(180.0);
                pointsLabel.setMinWidth(180.0);
                pointsLabel.setFont(new Font("Bernard MT Condensed", 28));
                pointsLabel.setTextFill(Color.WHITE);

                switch (i){
                    case 2:
                        positionLabel.setText("Second");
                        break;
                    case 3:
                        positionLabel.setText("Third");
                        break;
                    case 4:
                        positionLabel.setText("Fourth");
                        break;
                    default:break;
                }
                positionLabel.setMaxHeight(43.33);
                positionLabel.setMinHeight(43.33);
                positionLabel.setMaxWidth(180.0);
                positionLabel.setMinWidth(180.0);
                positionLabel.setFont(new Font("Bernard MT Condensed", 28));
                positionLabel.setTextFill(Color.WHITE);

                box.getChildren().add(0, positionLabel);
                box.getChildren().add(1, nameLabel);
                box.getChildren().add(2, pointsLabel);

                finalScores.remove(0);
                i++;
            }
        }
        else{
            winnerLabel.setText(gui.getDumbModel().getNickname());
            winnerPointsLabel.setText(Integer.toString(finalScore));
        }
    }

    @FXML
    private void quit(){
        Platform.exit();
    }
}
