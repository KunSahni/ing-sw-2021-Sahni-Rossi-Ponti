package it.polimi.ingsw.network.servertoclient.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.servertoclient.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.personalboard.FaithTrack;
import it.polimi.ingsw.server.model.personalboard.FavorStatus;
import it.polimi.ingsw.server.model.personalboard.SinglePlayerFaithTrack;

import java.util.List;

/**
 * This class contains an update regarding a player's faith track which will be saved in the local DumbModel
 */
public class FaithTrackUpdate extends BroadcastRenderable {
    private final String nickname;
    private final int updatedFaithTrackPosition;
    private final List<FavorStatus> updatedPopesFavors;
    private final int updatedBlackCrossPosition;

    public FaithTrackUpdate(String nickname, FaithTrack updatedFaithTrack) {
        this.nickname = nickname;
        this.updatedFaithTrackPosition = updatedFaithTrack.getFaithMarkerPosition();
        this.updatedPopesFavors = updatedFaithTrack.getPopesFavors();
        this.updatedBlackCrossPosition = -1;
    }

    public FaithTrackUpdate(String nickname, SinglePlayerFaithTrack updatedFaithTrack) {
        this.nickname = nickname;
        this.updatedFaithTrackPosition = updatedFaithTrack.getFaithMarkerPosition();
        this.updatedPopesFavors = updatedFaithTrack.getPopesFavors();
        this.updatedBlackCrossPosition = updatedFaithTrack.getBlackCrossPosition();
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
        //if multiplayer game then this is a faith track
        if(updatedBlackCrossPosition == -1)
            dumbModel.updateFaithTrack(nickname, updatedFaithTrackPosition, updatedPopesFavors);
        //else this is a single player faith track
        else
            dumbModel.updateSinglePlayerFaithTrack(nickname, updatedFaithTrackPosition, updatedPopesFavors, updatedBlackCrossPosition);

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
