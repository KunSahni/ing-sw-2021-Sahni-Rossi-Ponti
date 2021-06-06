package it.polimi.ingsw.network.servertoclient.renderable.updates;

import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.utils.dumbobjects.DumbModel;
import it.polimi.ingsw.client.utils.dumbobjects.OnScreenElement;
import it.polimi.ingsw.network.servertoclient.renderable.BroadcastRenderable;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.model.market.MarketMarble;

/**
 *  This class contains an updated version of the Market which will be saved in the local DumbModel
 */
public class MarketUpdate extends BroadcastRenderable {
    private final MarketMarble[][] updatedMarket;
    private final MarketMarble updatedExtraMarble;

    public MarketUpdate(Market updatedMarket) {
        this.updatedMarket = updatedMarket.getMarblesLayout();
        this.updatedExtraMarble = updatedMarket.getExtraMarble();
    }

    /**
     * This method returns an Enum value which represents what type of graphical element is contained in the Renderable
     * @return OnScreenElement enum value representing the payload in the Renderable
     */
    @Override
    public OnScreenElement getOnScreenElement(DumbModel dumbModel){
        return OnScreenElement.COMMONS;
    }

    /**
     * This method has the goal of updating the dumb model if the renderable is carrying any update regarding the server model
     * @param dumbModel the dumb model in which the updates will be stored
     */
    @Override
    public void update(DumbModel dumbModel){
        dumbModel.updateMarket(updatedMarket, updatedExtraMarble);
    }

    /**
     * This method is used by the UI to display an element part of the model on the user's screen
     * @param ui the ui which calls the method
     */
    @Override
    public void render(UI ui) {
        ui.renderCommons();
    }
}
