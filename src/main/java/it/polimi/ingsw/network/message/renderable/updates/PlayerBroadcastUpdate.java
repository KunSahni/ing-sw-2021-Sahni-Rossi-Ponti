package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
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
        this.updatedTurnStatus = updatedPlayer.isPlayersTurn();
        this.updatedConnectionStatus = updatedPlayer.isConnected();
    }

    @Override
    public void render(UI ui) {
        ui.updatePersonalBoard(nickname, position, updatedTurnStatus, updatedConnectionStatus);
    }
}