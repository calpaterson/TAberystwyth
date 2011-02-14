package taberystwyth.allocation.options;

import java.util.ArrayList;

public class AlgorithmListBuilder {
    
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
