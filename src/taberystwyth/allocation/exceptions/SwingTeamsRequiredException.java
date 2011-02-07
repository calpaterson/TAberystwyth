/*
 * 
 */
package taberystwyth.allocation.exceptions;

// TODO: Auto-generated Javadoc
/**
 * An exception to represent when not enough teams are in the database
 */
public class SwingTeamsRequiredException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The number of teams still required. */
    int required;
    
    /**
     * Instantiates a new SwingTeamsRequiredException.
     *
     * @param required the required
     */
    public SwingTeamsRequiredException(int required){
        this.required = required;
    }

    /**
     * Gets the number of swing teams that will be needed.
     *
     * @return the required
     */
    public synchronized int getRequired() {
        return required;
    }
    
}
