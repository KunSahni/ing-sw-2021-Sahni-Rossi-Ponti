package it.polimi.ingsw.server.model.developmentcard;

import java.io.Serializable;

/**
 * this enumeration represents leader cards level
 */
public enum Level implements Serializable {
    LEVEL1(1),
    LEVEL2(2),
    LEVEL3(3);

    private final int level;

    private Level(int level){
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static Level getLevel(int level) {
        for(Level l : values())
            if(l.getLevel() == level) return l;
        throw new IllegalArgumentException();
    }
}