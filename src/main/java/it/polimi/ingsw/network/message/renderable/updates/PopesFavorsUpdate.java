package it.polimi.ingsw.network.message.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.network.message.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.personalboardpackage.FavorStatus;

import java.util.List;

/**
 *  This class contains an update regarding a player's popesFavors
 */
public class PopesFavorsUpdate extends BroadcastRenderable {
    private final String nickname;
    private final List<FavorStatus> updatedPopesFavors;

    public PopesFavorsUpdate(String nickname, List<FavorStatus> updatedPopesFavors) {
        this.nickname = nickname;
        this.updatedPopesFavors = updatedPopesFavors;
    }

    @Override
    public void render(UI ui) {
        ui.updatePopesFavors(nickname, updatedPopesFavors);
    }
}