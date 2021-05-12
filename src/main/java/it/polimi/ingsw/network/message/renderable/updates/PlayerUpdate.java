package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;

/**
 *  This class contains an update regarding a player who's just joined a game
 */
public class PlayerUpdate extends BroadcastRenderable {
    private final String nickname;
    private final int position;

    public PlayerUpdate(String nickname, int position) {
        this.nickname = nickname;
        this.position = position;
    }

    @Override
    public void render(UI ui) {
        ui.addPersonalBoard(nickname, position);
    }
}
