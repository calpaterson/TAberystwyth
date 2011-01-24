package taberystwyth.prelim;

/**
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 * 
 * The abstract class for judge allocation algorithms.
 * Algorithms should inherit this class.
 *
 */

public abstract class JudgeAllocationAlgorithm implements AllocationAlgorithm {
	String name;
	String description;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void allocate() {
		// TODO Auto-generated method stub
		
	}

}
