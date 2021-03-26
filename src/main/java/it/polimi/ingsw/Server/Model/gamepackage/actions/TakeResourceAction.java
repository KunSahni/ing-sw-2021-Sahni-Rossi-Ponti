package it.polimi.ingsw.server.model.gamepackage.actions;

public class TakeResourceAction implements Action {
    private final int value;
    private final boolean row;

    public TakeResourceAction(int value, boolean row) {
        this.value = value;
        this.row = row;
    }

    public int getValue() {
        return value;
    }

    public boolean isRow() {
        return row;
    }
}