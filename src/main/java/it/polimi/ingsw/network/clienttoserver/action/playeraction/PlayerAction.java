package it.polimi.ingsw.network.clienttoserver.action.playeraction;

import it.polimi.ingsw.network.clienttoserver.action.Action;
import it.polimi.ingsw.network.servertoclient.renderable.ConfirmationMessageType;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.personalboard.FaithTrack;

import java.io.Serializable;

/**
 * Class representing player-initiated actions. Created to
 * implement the setPlayer method used by the RemoteView to
 * set the reference to the Player that initiated the action
 * client-side.
 */
public abstract class PlayerAction implements Action, Serializable {
    protected transient String nickname;
    protected transient Game game;
    protected transient Player player;

    /**
     * Add the nickname of the player who initiated the Action.
     *
     * @param nickname server-side reference used to identify the
     *                 Player object.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * Set the game Object that will be targeted by the Action for
     * checks and changes.
     *
     * @param game reference to the game Object.
     */
    public void setGame(Game game) {
        this.game = game;
        this.player = game.getPlayer(nickname);
    }

    /**
     * Check if the the Action is allowed by the current
     * state of the model.
     */
    public void runChecks() throws InvalidActionException {
        if (!player.isTurn())
            throw new InvalidActionException("Not your turn");
    }

    /**
     * Move forward the Faith Marker of the player
     * @param steps number of tiles that the Faith Marker
     *              will move on.
     */
    protected void moveFaithMarker(int steps) {
        FaithTrack playerFaithTrack = player.getPersonalBoard().getFaithTrack();
        for (int i = 0; i < steps; i++) {
            playerFaithTrack.moveMarker();
            if (playerFaithTrack.checkVaticanReport(playerFaithTrack.getFaithMarkerPosition())) {
                game.getPlayerList().forEach(npc -> npc.getPersonalBoard()
                                .getFaithTrack()
                                .flipPopesFavor(playerFaithTrack.getFaithMarkerPosition() / 8)
                );
            }
        }
    }

    public abstract ConfirmationMessageType getConfirmationMessage();
}
