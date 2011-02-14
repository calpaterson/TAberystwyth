package taberystwyth.allocation.options;


public class Random implements TabAlgorithm{

    @Override
    public String getName() {
        return "Random";
    }

    @Override
    public String getDescription() {
        return "Rooms are allocated randomly. This is useful " +
                "if you don't want teams to know they are " +
                "in a higher room";
    }
    
    @Override
    public String toString(){
        return getName();
    }
    
}