package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.network.message.renderable.PrivateRenderable;
import it.polimi.ingsw.server.model.leadercard.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains an update regarding this player's leader cards which will be saved in the local DumbModel
 */
public class LeaderCardsPrivateUpdate extends PrivateRenderable {
    private final List<DumbLeaderCard> updatedLeaderCards;

    public LeaderCardsPrivateUpdate(String nickname, List<LeaderCard> updatedLeaderCards) {
        super(nickname);
        this.updatedLeaderCards = updatedLeaderCards.stream()
                .map(LeaderCard::convertToDumb)
                .collect(Collectors.toList());
    }

    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    @Override
    public OnScreenElement getOnScreenElement(DumbModel dumbModel){
        return OnScreenElement.valueOf(dumbModel.getPersonalBoard(getNickname()).getPosition());
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel){
        dumbModel.updateLeaderCards(updatedLeaderCards);
    }

    /**
     * This method is used by the UI to display an element part of the model on the user's screen
     * @param ui the ui which calls the method
     */
    @Override
    public void render(UI ui) {
        ui.renderPersonalBoard(getNickname());
    }
}