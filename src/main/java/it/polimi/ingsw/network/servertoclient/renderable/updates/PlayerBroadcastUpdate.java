package it.polimi.ingsw.network.servertoclient.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.servertoclient.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.Player;

/**
 *  This class contains an update regarding another player in the game
 */
public class PlayerBroadcastUpdate extends BroadcastRenderable {
    private final String nickname;
    private final int position;
    private final boolean updatedTurnStatus;
    private final boolean updatedConnectionStatus;

    public PlayerBroadcastUpdate(Player updatedPlayer) {
        this.nickname = updatedPlayer.getNickname();
        this.position = updatedPlayer.getPosition();
        this.updatedTurnStatus = updatedPlayer.isTurn();
        this.updatedConnectionStatus = updatedPlayer.isConnected();
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    @Override
    public OnScreenElement getOnScreenElement(DumbModel dumbModel){
        if(updatedTurnStatus)
            return OnScreenElement.FORCE_DISPLAY;
        return OnScreenElement.valueOf(dumbModel.getPersonalBoard(nickname).getPosition());
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel){
        dumbModel.updatePersonalBoard(nickname, position, updatedTurnStatus, updatedConnectionStatus);
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