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
	protected BestToBestAllocationAlgorithm(){
		name = "Best to Best";
		description = "The best teams are placed in the best rated locations";
	}

	/* (non-Javadoc)
	 * @see taberystwyth.prelim.AllocationAlgorithm#allocate()
	 */
	@Override
	ArrayList<String> allocate() throws SQLException {
		TreeMap<Integer, ArrayList<String>> locationMap = getLocationMap();
		ArrayList<String> locations = new ArrayList<String>();
		/*
		 * For key in the location map...
		 */
		for(int i = locationMap.lastKey(); i>0; --i){
			/*
			 * For each location in the array at that key...
			 */
			for(String name : locationMap.get(i)){
				/*
				 * Add it to the locations array
				 */
				locations.add(name);
			}	
		}
		return locations;
	}

}
