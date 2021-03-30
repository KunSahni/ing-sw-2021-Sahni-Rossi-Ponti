package it.polimi.ingsw.server.model;

import java.util.*;
import java.time.*;




public class ConvertLeaderCard extends LeaderCard {

    private Color requirement1;

    private Color requirement2;

    private Resource convertedResource;

    public void setConvertedResource(Resource convertedResource) {
        this.convertedResource = convertedResource;
    }

}