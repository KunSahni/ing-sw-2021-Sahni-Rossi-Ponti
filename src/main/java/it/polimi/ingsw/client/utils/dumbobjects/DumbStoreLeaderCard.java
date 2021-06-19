package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.client.utils.constants.Constants;
import it.polimi.ingsw.server.model.leadercard.StoreLeaderCard;
import it.polimi.ingsw.server.model.utils.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * This is a dumber version of a regular StoreLeaderCard,
 * this class only contains the data stored in a StoreLeaderCard but has none of its logic.
 */
public class DumbStoreLeaderCard extends DumbLeaderCard{
    private final DumbResourceManager storage;
    private final Resource storedResource;

    public DumbStoreLeaderCard(StoreLeaderCard leaderCard) {
        super(leaderCard);
        storage = new DumbResourceManager();
        HashMap<Resource, Integer> storedResources = new HashMap<>();
        storedResources.put(leaderCard.getStoredType(), leaderCard.getResourceCount());
        storage.updateStoredResources(storedResources);
        storedResource = leaderCard.getStoredType();
    }

    /**
     * @param updatedStoredResources an updated map of the stored resources
     */
    public void updateStoredResources(Map<Resource, Integer> updatedStoredResources) {
        this.storage.updateStoredResources(updatedStoredResources);
    }

    public Resource getStoredType() {
        return storedResource;
    }

    /**
     * Returns the number of resources contained in the StoreLeaderCard
     */
    public int getResourceCount() {
        return storage.getResourceCount();
    }

    public Map<Resource, Integer> getStoredResources() {
        return storage.getStoredResources();
    }

    @Override
    public StoreLeaderCard convert() {
        return new StoreLeaderCard(getVictoryPoints(), getLeaderCardRequirements(), getStoredType(), getStoredResources());
    }

    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    @Override
    public String formatPrintableStringAt(int x, int y) {
        //contains the only color requirement
        Resource resource = new TreeMap<>(this.getLeaderCardRequirements().getRequiredResources()).firstKey();
        String containedResource1 = getResourceCount()>=1 ? storedResource.formatPrintableString() : " " ;
        String containedResource2 = getResourceCount()==2 ? storedResource.formatPrintableString() : " " ;

        return    "\033["+ x +";"+ y +"H╔══════════════╗"
                + "\033["+ (x+1) +";"+ y +"H║ 5x " + resource.formatPrintableString() + "        ║"
                + "\033["+ (x+3) +";"+ y +"H║              ║"
                + "\033["+ (x+3) +";"+ y +"H║              ║"
                + "\033["+ (x+4) +";"+ y +"H║ ╔═══╗  ╔═══╗ ║"
                + "\033["+ (x+5) +";"+ y +"H║ ║ " + containedResource1 + " ║  ║ " + containedResource2 + " ║ ║"
                + "\033["+ (x+6) +";"+ y +"H║ ╚═══╝  ╚═══╝ ║"
                + "\033["+ (x+7) +";"+ y +"H║              ║"
                + "\033["+ (x+8) +";"+ y +"H║    " + Constants.ANSI_YELLOW + getVictoryPoints() + Constants.ANSI_RESET+ "     ║"
                + "\033["+ (x+9) +";"+ y +"H╚══════════════╝";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DumbStoreLeaderCard)) return false;
        if (!super.equals(o)) return false;
        DumbStoreLeaderCard that = (DumbStoreLeaderCard) o;
        return storage.equals(that.storage) && storedResource == that.storedResource;
    }

    @Override
    public String toImgPath() {
        return super.toImgPath() + "leader_card_store_" + storedResource + ".png";
    }
}
