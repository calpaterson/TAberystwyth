package taberystwyth.prelim;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class BalancedJudgeAllocationAlgorithm extends JudgeAllocationAlgorithm {
    public BalancedJudgeAllocationAlgorithm() {
        name = "Balanced";
    }

    @Override
    ArrayList<String> allocate() throws SQLException {
        TreeMap<Integer, ArrayList<String>> rating2judges = getJudgeMap();
        TreeMap<String, Integer> location2rating = getLocationMap();
        
        /*
         * Build the list of chairs
         */
        ArrayList<String> chairs = new ArrayList<String>();
        while(chairs.size()<rating2judges.size()){
            ArrayList<String> value = rating2judges.get(rating2judges.firstKey());
            chairs.add(value.remove(0));
        }
        return chairs; // FIXME
        
        
    }
}
