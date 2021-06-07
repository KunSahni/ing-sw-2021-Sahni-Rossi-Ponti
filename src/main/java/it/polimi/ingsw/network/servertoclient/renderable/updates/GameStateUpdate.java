package it.polimi.ingsw.network.servertoclient.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.servertoclient.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.utils.GameState;

/**
 * This class contains an update regarding the state of the game which will be saved in the local DumbModel
 */
public class GameStateUpdate extends BroadcastRenderable {
    private final GameState updatedGameState;

    public GameStateUpdate(GameState gameState) {
        this.updatedGameState = gameState;
    }

    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    @Override
    public OnScreenElement getOnScreenElement(DumbModel dumbModel){
        if(updatedGameState.equals(GameState.ASSIGNED_INKWELL))
            return OnScreenElement.FORCE_DISPLAY;
        return OnScreenElement.DONT_RENDER;
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel){
        dumbModel.updateGameState(updatedGameState);
    }

    /**
     * This method is used by the UI to display an element part of the model on the user's screen
     * @param ui the ui which calls the method
     */
    @Override
    public void render(UI ui) {
        if(updatedGameState.equals(GameState.ASSIGNED_INKWELL))
            ui.renderResourcePregameChoice();
    }
}
