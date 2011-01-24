/**
 * 
 */
package taberystwyth.prelim;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 * 
 */
public class BestToBestAllocationAlgorithm extends LocationAllocationAlgorithm {

	/**
	 * Constructor
	 */
	protected BestToBestAllocationAlgorithm() {
		name = "Best to Best";
		description = "The best teams are placed in the best rated locations";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see taberystwyth.prelim.AllocationAlgorithm#allocate()
	 */
	@Override
	ArrayList<String> allocate() throws SQLException {
		TreeMap<Integer, ArrayList<String>> locationMap = getLocationMap();
		ArrayList<String> locations = new ArrayList<String>();
		while (!locationMap.isEmpty()) {
			locations.addAll(locationMap.remove(locationMap.lastKey()));
		}
		return locations;
	}

}
