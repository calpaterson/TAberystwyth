package taberystwyth.allocation.options;

public class BestToBest implements TabAlgorithm{

    @Override
    public String getName() {
        return "Best to Best";
    }
    @Override
    public String getDescription() {
        return "The best matches are held in the highest rated rooms";
    }
    
    @Override
    public String toString(){
        return getName();
    }
}


