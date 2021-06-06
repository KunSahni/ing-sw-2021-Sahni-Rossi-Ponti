package it.polimi.ingsw.network.servertoclient.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.*;
import it.polimi.ingsw.network.servertoclient.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.leadercard.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains an update regarding another player's leader cards which will be saved in the local DumbModel
 */
public class LeaderCardsBroadcastUpdate extends BroadcastRenderable {
    private final String nickname;
    private final List<DumbLeaderCard> updatedLeaderCards;

    public LeaderCardsBroadcastUpdate(String nickname, List<LeaderCard> updatedLeaderCards) {
        this.nickname = nickname;
        this.updatedLeaderCards = updatedLeaderCards.stream()
                .map(card -> (card.isActive()) ? card.convertToDumb() : null)
                .collect(Collectors.toList());
    }

    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    @Override
    public OnScreenElement getOnScreenElement(DumbModel dumbModel){
        return OnScreenElement.valueOf(dumbModel.getPersonalBoard(nickname).getPosition());
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel){
        dumbModel.updateLeaderCards(nickname, updatedLeaderCards);
    }

    /**
     * This method is used by the UI to display an element part of the model on the user's screen
     * @param ui the ui which calls the method
     */
    @Override
    public void render(UI ui) {
        ui.renderPersonalBoard(nickname);
    }
}
