/*
* This code has been generated by the Rebel: a code generator for modern Java.
*
* Drop us a line or two at feedback@archetypesoftware.com: we would love to hear from you!
*/
package Model;

import java.util.*;
import java.time.*;



// ----------- << imports@AAAAAAF4Wkqftzp1sZ8= >>
// ----------- >>

// ----------- << class.annotations@AAAAAAF4Wkqftzp1sZ8= >>
// ----------- >>
public interface StorageElement {
    /**
    * @param addResources
    */

    // ----------- << method.annotations@AAAAAAF4Wk109UDSeb8= >>
    // ----------- >>
    void storeResources(Map<Resource, Integer> addResources);
    /**
    * @param delResources
    */

    // ----------- << method.annotations@AAAAAAF4Wk3BUEnAsZ4= >>
    // ----------- >>
    void discardResources(Map<Resource, Integer> delResources);
    // ----------- << method.annotations@AAAAAAF4Wk4Ui2QVgjg= >>
    // ----------- >>
    Map<Resource, Integer> peekResources();
// ----------- << interface.extras@AAAAAAF4Wkqftzp1sZ8= >>
// ----------- >>
}