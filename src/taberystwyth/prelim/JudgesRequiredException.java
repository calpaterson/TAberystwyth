package taberystwyth.prelim;

public class JudgesRequiredException extends Exception {
    private static final long serialVersionUID = 1L;
    
    int required;
    
    public JudgesRequiredException(int required){
        this.required = required;
    }

    public synchronized int getRequired() {
        return required;
    }
}
