package taberystwyth.allocation.exceptions;

public class SwingTeamsRequiredException extends Exception {

    private static final long serialVersionUID = 1L;
    
    int required;
    
    public SwingTeamsRequiredException(int required){
        this.required = required;
    }

    public synchronized int getRequired() {
        return required;
    }
    
}
