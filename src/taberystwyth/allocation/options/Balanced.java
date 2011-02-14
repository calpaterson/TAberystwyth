package taberystwyth.allocation.options;

public class Balanced implements TabAlgorithm{
    @Override
    public String getName() {
        return "Balanced";
    }
    @Override
    public String getDescription() {
        return "The judges are assigned to rooms in a round-robin, balanced way";
    }
    
    @Override
    public String toString(){
        return getName();
    }
}