package taberystwyth.allocation.exceptions;

public class JudgesRequiredException extends Exception {
    private static final long serialVersionUID = 1L;
    
    private final int required;
    
    public JudgesRequiredException(final int required){
        super();
        this.required = required;
    }

    public int getRequired() {
        return required;
    }
}
