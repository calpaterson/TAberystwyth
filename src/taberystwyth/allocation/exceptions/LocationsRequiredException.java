package taberystwyth.allocation.exceptions;

public class LocationsRequiredException extends Exception {
    private static final long serialVersionUID = 1L;
    
    int required;
    
    public LocationsRequiredException(int required){
        this.required = required;
    }

    public synchronized int getRequired() {
        return required;
    }
}
