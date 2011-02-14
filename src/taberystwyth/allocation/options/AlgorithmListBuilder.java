package taberystwyth.allocation.options;

import java.util.ArrayList;

/**
 * Creates lists of algorithms that can be used by the Allocator
 * 
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 *
 */
public class AlgorithmListBuilder {
    
    //FIXME Might be nice to have sub-interfaces for each type
    
    public static ArrayList<TabAlgorithm> getJudgeAlgorithms(){
        
        ArrayList<TabAlgorithm> list = new ArrayList<TabAlgorithm>();
        
        list.add(new Balanced());
        
        return list;
        
    }
    
    public static ArrayList<TabAlgorithm> getTeamAlgorithms(){
        ArrayList<TabAlgorithm> list = new ArrayList<TabAlgorithm>();
        
        list.add(new WUDC());
        
        return list;
    }
    
    public static ArrayList<TabAlgorithm> getLocationAlgorithms(){
        ArrayList<TabAlgorithm> list = new ArrayList<TabAlgorithm>();
        
        list.add(new BestToBest());
        list.add(new taberystwyth.allocation.options.Random());
        
        return list;
    }
    
}
