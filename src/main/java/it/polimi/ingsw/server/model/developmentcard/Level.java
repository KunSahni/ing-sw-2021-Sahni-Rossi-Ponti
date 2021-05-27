package it.polimi.ingsw.server.model.developmentcard;

/**
 * this enumeration represents leader cards level
 */
public enum Level {
    LEVEL1(1),
    LEVEL2(2),
    LEVEL3(3);

    int level;

    private Level(int level){
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}