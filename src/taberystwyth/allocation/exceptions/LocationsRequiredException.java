package taberystwyth.allocation.exceptions;

/**
 * An exception to represent when not enough locations are in the database
 */
public class LocationsRequiredException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The number of locations that need to be added. */
    int required;
    
    /**
     * Instantiates a new LocationsRequiredException.
     *
     * @param required the required
     */
    public LocationsRequiredException(int required){
        this.required = required;
    }

    /**
     * Gets the number of locations that need to be added.
     *
     * @return the required
     */
    public synchronized int getRequired() {
        return required;
    }
}
