package taberystwyth.allocation.exceptions;

/**
 * An exception to represent when not enough judges are in the database
 */
public class JudgesRequiredException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The number of judges that need to be added. */
    private final int required;
    
    /**
     * Instantiates a new JudgesRequiredException.
     * 
     * @param required
     *            the required
     */
    public JudgesRequiredException(final int required) {
        super();
        this.required = required;
    }
    
    /**
     * Gets the number of judges that need to be added.
     * 
     * @return the required
     */
    public int getRequired() {
        return required;
    }
}
