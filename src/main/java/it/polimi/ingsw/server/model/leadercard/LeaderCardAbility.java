package it.polimi.ingsw.server.model.leadercard;

import java.io.Serializable;

/**
 * this enumeration represents all the abilities that a leader card can have
 */
public enum LeaderCardAbility implements Serializable {
    DISCOUNT,
    STORE,
    CONVERT,
    PRODUCE;
}