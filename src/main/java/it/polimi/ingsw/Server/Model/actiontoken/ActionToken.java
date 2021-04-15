package it.polimi.ingsw.server.model.actiontoken;

/**
 * this enumeration represents all possible types of action token
 */
public enum ActionToken {
    MOVEBYTWO("MoveByTwo"),
    MOVEANDSHUFFLE("MoveAndShuffle"),
    REMOVEGREEN("RemoveGreen"),
    REMOVEPURPLE("RemovePurple"),
    REMOVEYELLOW("RemoveYellow"),
    REMOVEBLUE("RemoveBlue");

    public final String label;

    ActionToken(String label) {
        this.label = label;
    }
}