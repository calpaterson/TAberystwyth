/**
 * 
 */
package taberystwyth.prelim;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 * 
 */
public class BestToBestLocationAllocationAlgorithm 
        extends LocationAllocationAlgorithm {

    ArrayList<String> locations = new ArrayList<String>();
    int index = 0;
    
	/**
	 * Constructor
	 */
	protected BestToBestLocationAllocationAlgorithm() {
		name = "Best to Best";
		description = "The best teams are placed in the best rated locations";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see taberystwyth.prelim.AllocationAlgorithm#allocate()
	 */
	@Override
	public void allocate() throws SQLException {
		TreeMap<Integer, ArrayList<String>> locationMap = getLocationMap();

		while (!locationMap.isEmpty()) {
			locations.addAll(locationMap.remove(locationMap.lastKey()));
		}
	}

    public Iterator<String> iterator() {
        return this;
    }

    public boolean hasNext() {
        return (index < locations.size() - 1);
    }

    public String next() {
        index++;
        return locations.get(index);
    }

    public void remove() {
        // void
    }

}
