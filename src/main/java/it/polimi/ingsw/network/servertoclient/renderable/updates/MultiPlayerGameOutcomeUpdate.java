package it.polimi.ingsw.network.servertoclient.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.servertoclient.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.util.Comparator;
import java.util.TreeMap;

/**
 *  This class contains an regarding the outcome of a multiplayer game
 */
public class MultiPlayerGameOutcomeUpdate extends BroadcastRenderable {
    private final TreeMap<Integer, String> finalScores;

    public MultiPlayerGameOutcomeUpdate(Game game) {
        Comparator<Player> c = Comparator.comparingInt(Player::getPosition);
        finalScores = new TreeMap<>();
        for(Player player: game.getPlayerList())
            finalScores.put(player.getPersonalBoard().getVictoryPoints(), player.getNickname());
    }

    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    @Override
    public OnScreenElement getOnScreenElement(DumbModel dumbModel){
        return OnScreenElement.FORCE_DISPLAY;
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel){
        //doesn't need to update anything
    }

    /**
     * This method is used by the UI to display an element part of the model on the user's screen
     * @param ui the ui which calls the method
     */
    @Override
    public void render(UI ui) {
        ui.renderGameOutcome(finalScores);
    }
}
