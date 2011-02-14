package taberystwyth.allocation.options;

final class BestToBest implements TabAlgorithm{

    @Override
    public String getName() {
        return "Best to Best";
    }
    @Override
    public String getDescription() {
        return "The best matches are held in the highest rated rooms";
    }
}

final class Random implements TabAlgorithm{

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
    
}

