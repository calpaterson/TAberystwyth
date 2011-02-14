package taberystwyth.allocation.options;

public class WUDC implements TabAlgorithm{

    @Override
    public String getName() {
        return "WUDC";
    }
    @Override
    public String getDescription() {
        return "Teams are allocated using the WUDC algorithm." +
        		" Pools are created of equally strong teams, " +
        		"then teams are promoted until the pool is " +
        		"divisible by four";
    }
    
    @Override
    public String toString(){
        return getName();
    }
    
}
