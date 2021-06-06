package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.utils.Resource;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is a dumber version of a regular ResourceManager,
 * this class only contains data contained in a ResourceManager but has none of its logic.
 */
public class DumbResourceManager implements Serializable {
    private Map<Resource, Integer> storedResources;


    public DumbResourceManager() {
        super();
    }

    /**
     * @param updatedStoredResources an updated map of the stored resources
     */
    public void updateStoredResources(Map<Resource, Integer> updatedStoredResources) {
        this.storedResources = new HashMap<>(updatedStoredResources);
    }

    public int getResourceCount() {
        return storedResources.values()
                .stream()
                .reduce(0, Integer::sum);
    }

    public Map<Resource, Integer> getStoredResources() {
        return storedResources;
    }


    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    public String formatPrintableStringAtAsDepots(int x, int y) {
        List<Map.Entry<Resource, Integer>> entryList = storedResources
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        List<List<String>> printableResourceList = new ArrayList<>();

        IntStream.range(0, 3).forEach(
                i -> {
                    printableResourceList.add(new ArrayList<String>());
                    if(entryList.size() > i)
                        IntStream.range(0, entryList.get(i).getValue()).forEach(
                                $ -> printableResourceList.get(i).add(entryList.get(i).getKey().formatPrintableString())
                        );
                    fixLength(printableResourceList.get(i), 3-i);
                }
        );


        return    "\033[" + x + ";" + (y+6) + "H╔═══╗"
                + "\033[" + (x+1) + ";" + (y+6) + "H║ "+ printableResourceList.get(2).get(0) +" ║"
                + "\033[" + (x+2) + ";" + (y+6) + "H╚═══╝"
                + "\033[" + (x+3) + ";" + (y+3) + "H╔═══╗ ╔═══╗"
                + "\033[" + (x+4) + ";" + (y+3) + "H║ "+ printableResourceList.get(1).get(0) +" ║ ║ "+ printableResourceList.get(1).get(1) +" ║"
                + "\033[" + (x+5) + ";" + (y+3) + "H╚═══╝ ╚═══╝"
                + "\033[" + (x+6) + ";" + y + "H╔═══╗ ╔═══╗ ╔═══╗"
                + "\033[" + (x+7) + ";" + y + "H║ "+ printableResourceList.get(0).get(0) +" ║ ║ "+ printableResourceList.get(0).get(1) +" ║ ║ "+ printableResourceList.get(0).get(2) +" ║"
                + "\033[" + (x+8) + ";" + y + "H╚═══╝ ╚═══╝ ╚═══╝";
    }

    /**
     * this method increases the size of a list up to a wanted size
     * @param resourceList the list whose size needs to be increased
     * @param wantedSize the wanted size for the list
     */
    private void fixLength(List<String> resourceList, int wantedSize) {
        IntStream.range(0, wantedSize-resourceList.size()).forEach(
                $ -> resourceList.add(" ")
        );
    }


    /**
     * @param x the x position of the cursor in the console
     * @param y the y position of the cursor in the console
     * @return a string color of a leader Card with the top left corner in position x,y
     */
    public String formatPrintableStringAtAsStrongbox(int x, int y) {
        return    "\033[" + x + ";" + y + "H" + (storedResources.get(Resource.COIN)!= null? storedResources.get(Resource.COIN) : "0") + " x " + Resource.COIN
                + "\033[" + (x+1) + ";" + y + "H" + (storedResources.get(Resource.SHIELD)!= null? storedResources.get(Resource.SHIELD) : "0") + " x " + Resource.SHIELD
                + "\033[" + (x+1) + ";" + y + "H" + (storedResources.get(Resource.SERVANT)!= null? storedResources.get(Resource.SERVANT) : "0") + " x " + Resource.SERVANT
                + "\033[" + (x+1) + ";" + y + "H" + (storedResources.get(Resource.STONE)!= null? storedResources.get(Resource.STONE) : "0") + " x " + Resource.STONE;
    }

}
