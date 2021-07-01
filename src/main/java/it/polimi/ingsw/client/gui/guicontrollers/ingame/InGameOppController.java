package it.polimi.ingsw.client.gui.guicontrollers.ingame;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbLeaderCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbPersonalBoard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbStoreLeaderCard;
import it.polimi.ingsw.server.model.leadercard.LeaderCardAbility;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * JFX Controller used to handle interaction on an opponent's board.
 */
public class InGameOppController extends PlayerBoardController {
    @FXML
    private Button backToPersonalBoardButton;
    @FXML
    private ImageView leaderCard1;
    @FXML
    private ImageView leaderCard1LeftResource;
    @FXML
    private ImageView leaderCard1RightResource;
    @FXML
    private ImageView leaderCard2;
    @FXML
    private ImageView leaderCard2LeftResource;
    @FXML
    private ImageView leaderCard2RightResource;
    private final List<ImageView> leaderCardImages = new ArrayList<>();
    private final List<List<ImageView>> leaderCardResources = new ArrayList<>();
    private List<DumbLeaderCard> renderedLeaderCards = new ArrayList<>();

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        backToPersonalBoardButton.setOnAction(e -> gui.goToPersonalView());
        // Init leader cards
        leaderCardImages.add(leaderCard1);
        leaderCardImages.add(leaderCard2);
        List<ImageView> leaderCard1Resources = new ArrayList<>();
        leaderCard1Resources.add(leaderCard1LeftResource);
        leaderCard1Resources.add(leaderCard1RightResource);
        List<ImageView> leaderCard2Resources = new ArrayList<>();
        leaderCard2Resources.add(leaderCard2LeftResource);
        leaderCard2Resources.add(leaderCard2RightResource);
        leaderCardResources.add(leaderCard1Resources);
        leaderCardResources.add(leaderCard2Resources);
        leaderCardResources.forEach(imageViewList -> imageViewList.forEach(imageView -> imageView.setVisible(false)));
    }

    public void initialize(GUI gui, String nickname) {
        super.setGui(gui);
        super.setNicknameLabel(nickname);
    }

    @Override
    protected void renderLeaderCards(DumbPersonalBoard dumbPersonalBoard) {
        List<DumbLeaderCard> inBoardLeaderCards = dumbPersonalBoard.getLeaderCards();
        if (!renderedLeaderCards.equals(inBoardLeaderCards)) {
            IntStream.range(0, inBoardLeaderCards.size()).forEach(i -> {
                DumbLeaderCard dumbLeaderCard = inBoardLeaderCards.get(i);
                if (dumbLeaderCard == null)
                    leaderCardImages.get(i).setImage(getImageFromPath("/img/cards/leader_card_back.png"));
                else {
                    leaderCardImages.get(i).setImage(getImageFromPath(dumbLeaderCard.toImgPath()));
                    if (dumbLeaderCard.getAbility().equals(LeaderCardAbility.STORE)) {
                        DumbStoreLeaderCard dumbStoreLeaderCard =
                                (DumbStoreLeaderCard) dumbLeaderCard;
                        List<ImageView> leaderCardResourceImages = leaderCardResources.get(i);
                        IntStream.range(0, 2).forEach(j -> {
                            leaderCardResourceImages.get(j)
                                    .setImage(getImageFromPath(dumbStoreLeaderCard.getStoredType().toImgPath()));
                            leaderCardResourceImages.get(j).setVisible(dumbStoreLeaderCard.getResourceCount() > j);
                        });
                    }
                }
            });
            IntStream.range(inBoardLeaderCards.size(), 2).forEach(i -> {
                leaderCardImages.get(i).setVisible(false);
                leaderCardResources.get(i).forEach(imageView -> imageView.setVisible(false));
            });
            renderedLeaderCards = inBoardLeaderCards;
        }
    }
}
