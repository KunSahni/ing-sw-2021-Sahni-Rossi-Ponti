/*
* This code has been generated by the Rebel: a code generator for modern Java.
*
* Drop us a line or two at feedback@archetypesoftware.com: we would love to hear from you!
*/
package it.polimi.ingsw.server.model.personalboardpackage;

import java.util.*;
import java.time.*;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.Resource;

// ----------- << imports@AAAAAAF4NpH7e3Nyp6s= >>
// ----------- >>

// ----------- << class.annotations@AAAAAAF4NpH7e3Nyp6s= >>
// ----------- >>
public class DevelopmentCardSlot implements DevelopmentSlot {
    // ----------- << attribute.annotations@AAAAAAF4RefsGMmFBsg= >>
    // ----------- >>
    private Stack<DevelopmentCard> cards;

    // ----------- << attribute.annotations@AAAAAAF4QTTsJ2Nzeos= >>
    // ----------- >>
    private DevelopmentCard ;

    public Stack<DevelopmentCard> getCards() {
        return cards;
    }

    public DevelopmentCard get() {
        return ;
    }

    public void setCards(Stack<DevelopmentCard> cards) {
        this.cards = cards;
    }

    public void set(DevelopmentCard ) {
        this. = ;
    }

    public void link(DevelopmentCard _) {
        set(_);
    }

    public void unlink() {
        set(null);
    }

    /**
    * @param resources
    */

    @Override
    // ----------- << method.annotations@AAAAAAF4QTMONjXOrD0= >>
    // ----------- >>
    public Map<Resource, Integer> produce(Map<Resource, Integer> resources) {
    // ----------- << method.body@AAAAAAF4QTMONjXOrD0= >>
    // ----------- >>
    }
    /**
    * @param card
    */

    // ----------- << method.annotations@AAAAAAF4ReeAdsKvBGY= >>
    // ----------- >>
    public boolean canBePlaced(DevelopmentCard card) {
    // ----------- << method.body@AAAAAAF4ReeAdsKvBGY= >>
    // ----------- >>
    }
// ----------- << class.extras@AAAAAAF4NpH7e3Nyp6s= >>
// ----------- >>
}