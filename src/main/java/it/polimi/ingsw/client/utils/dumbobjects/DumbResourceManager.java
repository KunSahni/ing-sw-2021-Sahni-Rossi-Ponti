package it.polimi.ingsw.client.utils.dumbobjects;

import it.polimi.ingsw.server.model.utils.Resource;
import javafx.scene.image.ImageView;

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
        return storedResources
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
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

        Collections.reverse(entryList);

        List<List<String>> printableResourceList = new ArrayList<>();

        IntStream.range(0, 3).forEach(
            $ -> printableResourceList.add(new ArrayList<>())
        );

        IntStream.range(0, 3).forEach(
                i -> {
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
        return    "\033[" + x + ";" + y + "H" + (storedResources.containsKey(Resource.COIN) ? storedResources.get(Resource.COIN) : "0") + " x " + Resource.COIN.formatPrintableString()
                + "\033[" + (x+1) + ";" + y + "H" + (storedResources.containsKey(Resource.SHIELD) ? storedResources.get(Resource.SHIELD) : "0") + " x " + Resource.SHIELD.formatPrintableString()
                + "\033[" + (x+2) + ";" + y + "H" + (storedResources.containsKey(Resource.SERVANT) ? storedResources.get(Resource.SERVANT) : "0") + " x " + Resource.SERVANT.formatPrintableString()
                + "\033[" + (x+3) + ";" + y + "H" + (storedResources.containsKey(Resource.STONE) ? storedResources.get(Resource.STONE) : "0") + " x " + Resource.STONE.formatPrintableString();
    }

}
