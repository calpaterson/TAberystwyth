/**
 * 
 */
package taberystwyth.prelim;

/**
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 * 
 * This interface ensures that all algorithms for allocating teams, judges
 * or locations supply a name and a description, and have a method to 
 * perform the allocation
 *
 */
public interface AllocationAlgorithm {
	
	/**
	 * @return The name of the algorithm
	 */
	String getName();
	
	/**
	 * @return A description of the algorithm
	 */
	String getDescription();
	
	/**
	 * Run the algorithm
	 */
	void allocate(); 

}
