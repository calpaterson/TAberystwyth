package taberystwyth.prelim;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class BalancedJudgeAllocationAlgorithm extends JudgeAllocationAlgorithm {
    public BalancedJudgeAllocationAlgorithm() {
        name = "Balanced";
        description = "The top judges become the chairs of each room, " +
        		"further judges are assigned to each room as wings giving " +
        		"a balanced judging pool across all rooms";
    }

    @Override
    ArrayList<String> allocate() throws SQLException {
        TreeMap<Integer, ArrayList<String>> rating2judges = getJudgeMap();
        ArrayList<String> judges = new ArrayList<String>();
        
        while(!rating2judges.isEmpty()){
        	judges.addAll(rating2judges.remove(rating2judges.lastKey()));
        }
        
        return judges;
        
        
    }
}
