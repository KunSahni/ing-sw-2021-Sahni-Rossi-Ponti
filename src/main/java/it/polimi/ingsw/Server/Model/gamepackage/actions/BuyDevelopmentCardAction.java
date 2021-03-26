package it.polimi.ingsw.server.model.gamepackage.actions;

import it.polimi.ingsw.server.model.*;

public class BuyDevelopmentCardAction implements Action {
    private final Level level;
    private final Color color;
    private final int position;

    public BuyDevelopmentCardAction(Level level, Color color, int position) {
        this.level = level;
        this.color = color;
        this.position = position;
    }

    public Level getLevel() {
        return level;
    }

    public Color getColor() {
        return color;
    }

    public int getPosition() {
        return position;
    }
}