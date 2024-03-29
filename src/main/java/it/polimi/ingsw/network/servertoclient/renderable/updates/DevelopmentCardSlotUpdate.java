package it.polimi.ingsw.network.servertoclient.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbDevelopmentCard;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.servertoclient.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.personalboard.DevelopmentCardSlot;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains an updated version of a player's DevelopmentCardSlot which will be saved in the local DumbModel
 */
public class DevelopmentCardSlotUpdate extends BroadcastRenderable {
    private final String nickname;
    private final int position;
    private final List<DumbDevelopmentCard> updatedDevelopmentCardSlot;

    public DevelopmentCardSlotUpdate(String nickname, DevelopmentCardSlot updatedDevelopmentCardSlot) {
        this.nickname = nickname;
        this.position = updatedDevelopmentCardSlot.getSlotIndex();
        this.updatedDevelopmentCardSlot = updatedDevelopmentCardSlot.
                getDevelopmentCards()
                .stream()
                .map(
                    DumbDevelopmentCard::new
                )
                .collect(Collectors.toList());
    }

    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    @Override
    public OnScreenElement getOnScreenElement(DumbModel dumbModel){
        if(dumbModel.getOwnPersonalBoard().getNickname().equals(nickname))
            return OnScreenElement.FORCE_DISPLAY;
        return OnScreenElement.valueOf(dumbModel.getPersonalBoard(nickname).getPosition());
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel){
        dumbModel.updateDevelopmentCardSlot(nickname, position, updatedDevelopmentCardSlot);
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
