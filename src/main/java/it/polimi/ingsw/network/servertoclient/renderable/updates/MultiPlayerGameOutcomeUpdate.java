package it.polimi.ingsw.network.servertoclient.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.servertoclient.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 *  This class contains an regarding the outcome of a multiplayer game
 */
public class MultiPlayerGameOutcomeUpdate extends BroadcastRenderable {
    private final List<ScoreTuple> finalScores;

    public MultiPlayerGameOutcomeUpdate(Game game) {
        finalScores = new ArrayList<>();
        //Add all scores to list
        for(Player player: game.getPlayerList())
            finalScores.add(new ScoreTuple(player.getNickname(), player.getPersonalBoard().getVictoryPoints()));
        //Sort the scores and reverse
        finalScores.sort(Comparator.comparingInt(ScoreTuple::getScore));
        Collections.reverse(finalScores);
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

    /**
     * Inner class used to store final scores as tuples
     */
    public class ScoreTuple{
        private final String name;
        private final int score;

        public ScoreTuple(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }
}
