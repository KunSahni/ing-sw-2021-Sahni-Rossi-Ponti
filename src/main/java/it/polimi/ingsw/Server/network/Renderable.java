package it.polimi.ingsw.server.network;

import it.polimi.ingsw.client.UI;

/**
 * Wrapper class for all model items that are meant to
 * be rendered via GUI / CLI .
 */
public interface Renderable {
    Renderable render(UI ui);
}
