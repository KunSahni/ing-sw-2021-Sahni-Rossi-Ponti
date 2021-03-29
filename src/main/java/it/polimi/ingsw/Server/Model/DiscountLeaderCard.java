package it.polimi.ingsw.server.model;

import java.util.*;
import java.time.*;



public class DiscountLeaderCard extends LeaderCard {

    private Color requirement1;

    private Color requirement2;

    private Resource discountedResource;

    public Resource getDiscountedResource() {
        return discountedResource;
    }

}