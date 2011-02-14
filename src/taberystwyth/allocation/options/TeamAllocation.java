package taberystwyth.allocation.options;

final class WUDC implements TabAlgorithm{

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
    
}
