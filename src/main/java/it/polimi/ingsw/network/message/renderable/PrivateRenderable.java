package it.polimi.ingsw.network.message.renderable;

import java.io.Serializable;

/**
 * Renderable items that are get shown only to one player.
 */
public abstract class PrivateRenderable implements Serializable, Renderable {
    private final String nickname;

    public PrivateRenderable(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
