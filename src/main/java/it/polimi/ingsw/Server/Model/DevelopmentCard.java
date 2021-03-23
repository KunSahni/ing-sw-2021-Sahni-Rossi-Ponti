/*
* This code has been generated by the Rebel: a code generator for modern Java.
*
* Drop us a line or two at feedback@archetypesoftware.com: we would love to hear from you!
*/
package it.polimi.ingsw.server.model;

import java.util.*;
import java.time.*;



// ----------- << imports@AAAAAAF4NodPBW5hOvc= >>
// ----------- >>

// ----------- << class.annotations@AAAAAAF4NodPBW5hOvc= >>
// ----------- >>
public class DevelopmentCard implements VictoryPointsElement {
    // ----------- << attribute.annotations@AAAAAAF4N/sPuOueGLw= >>
    // ----------- >>
    private Color type;

    // ----------- << attribute.annotations@AAAAAAF4N/xQ8vDGVaQ= >>
    // ----------- >>
    private Level level;

    // ----------- << attribute.annotations@AAAAAAF4N/zkePaZoZw= >>
    // ----------- >>
    private int victoryPoints;

    // ----------- << attribute.annotations@AAAAAAF4N/4UgPsW/WU= >>
    // ----------- >>
    private Map<Resource, Integer> inputResources;

    // ----------- << attribute.annotations@AAAAAAF4N/8pkQQJb/Y= >>
    // ----------- >>
    private Map<Resource, Integer> outputResources;

    // ----------- << attribute.annotations@AAAAAAF4OAA8egukDXE= >>
    // ----------- >>
    private Map<Resource, Integer> cost;

    // ----------- << attribute.annotations@AAAAAAF4Rfrve691V7k= >>
    // ----------- >>
    private int faithIncrement;

    // ----------- << attribute.annotations@AAAAAAF4NppTOnot5BE= >>
    // ----------- >>
    private DevelopmentCardsBoard ;

    // ----------- << attribute.annotations@AAAAAAF4N/iLk7cbCro= >>
    // ----------- >>
    private DevelopmentCardsDeck ;

    // ----------- << attribute.annotations@AAAAAAF4RiZrN/OdOx4= >>
    // ----------- >>
    private ResourceBank ;

    // ----------- << attribute.annotations@AAAAAAF4NpjcRXhYkW8= >>
    // ----------- >>
    private Color ;

    // ----------- << attribute.annotations@AAAAAAF4Npi8+Xf6n6o= >>
    // ----------- >>
    private Level ;

    public Color getType() {
        return type;
    }

    public Level getLevel() {
        return level;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public Map<Resource, Integer> getInputResources() {
        return inputResources;
    }

    public Map<Resource, Integer> getOutputResources() {
        return outputResources;
    }

    public Map<Resource, Integer> getCost() {
        return cost;
    }

    public int getFaithIncrement() {
        return faithIncrement;
    }

    public DevelopmentCardsBoard get() {
        return ;
    }

    public DevelopmentCardsDeck get() {
        return ;
    }

    public ResourceBank get() {
        return ;
    }

    public Color get() {
        return ;
    }

    public Level get() {
        return ;
    }

    public void set(DevelopmentCardsBoard ) {
        this. = ;
    }

    public void set(DevelopmentCardsDeck ) {
        this. = ;
    }

    public void set(ResourceBank ) {
        this. = ;
    }

    public void set(Color ) {
        this. = ;
    }

    public void set(Level ) {
        this. = ;
    }

    public void link(DevelopmentCardsBoard _) {
        if (_ != null) {
            _.unlink();
            _.set(this);
        }

        unlink();
        set(_);
    }

    public void link(DevelopmentCardsDeck _) {
        if (_ != null) {
            _.getDeck().add(this);
        }

        unlink();
        set(_);
    }

    public void link(ResourceBank _) {
        set(_);
    }

    public void link(Color _) {
        if (_ != null) {
            _.unlink();
            _.set(this);
        }

        unlink();
        set(_);
    }

    public void link(Level _) {
        if (_ != null) {
            _.unlink();
            _.set(this);
        }

        unlink();
        set(_);
    }

    public void unlink() {
        if (get() != null) {
            get().set(null);
            set(null);
        }
    }

    public void unlink() {
        if (get() != null) {
            get().getDeck().remove(this);
            set(null);
        }
    }

    public void unlink() {
        set(null);
    }

    public void unlink() {
        if (get() != null) {
            get().set(null);
            set(null);
        }
    }

    public void unlink() {
        if (get() != null) {
            get().set(null);
            set(null);
        }
    }

    @Override
    // ----------- << method.annotations@AAAAAAF4RjrUDX8u+pU= >>
    // ----------- >>
    public int getVictoryPoints() {
    // ----------- << method.body@AAAAAAF4RjrUDX8u+pU= >>
    // ----------- >>
    }
    // ----------- << method.annotations@AAAAAAF4OAk1Ak5PrM4= >>
    // ----------- >>
    public Map<Resource, Integer> produce() {
    // ----------- << method.body@AAAAAAF4OAk1Ak5PrM4= >>
    // ----------- >>
    }
// ----------- << class.extras@AAAAAAF4NodPBW5hOvc= >>
// ----------- >>
}