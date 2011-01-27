package taberystwyth.allocation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import taberystwyth.allocation.options.JudgeAllocation;
import taberystwyth.allocation.options.LocationAllocation;
import taberystwyth.allocation.options.TeamAllocation;
import taberystwyth.allocation.exceptions.*;
import taberystwyth.db.SQLConnection;

public class Allocator {
    
    SQLConnection conn = SQLConnection.getInstance();
    ResultSet rs;
    String query;
    
    private static Allocator instance = new Allocator();
    
    public static Allocator getInstance(){
        return instance;
    }
    
    private Allocator(){};
    
    public void allocate(TeamAllocation teamAlgo,
                         JudgeAllocation judgeAlgo,
                         LocationAllocation locationAlgo) 
        throws SQLException, 
               SwingTeamsRequiredException,
               LocationsRequiredException,
               JudgesRequiredException{
        /*
         * Figure out the current round
         */
        int round = 0; // FIXME
        
        /*
         * Generate matches
         */
        query = "select count(*) / 4 from teams;";
        rs = conn.executeQuery(query);
        rs.next();
        int nMatches = rs.getInt(1);
        ArrayList<Match> matches = new ArrayList<Match>();
        int rank = 0;
        while (matches.size() > nMatches){
            matches.add(new Match(rank));
            ++rank;
        }
        rs.close();
        
        /*
         * Allocate teams
         */
        if (teamAlgo == TeamAllocation.WUDC){
        }
        
        /*
         * Allocate Judges
         */
        if (judgeAlgo == JudgeAllocation.BALANCED){
            
        }
        
        /*
         * Allocate locations
         */
        if (locationAlgo == LocationAllocation.RANDOM){
            
        } else if (locationAlgo == LocationAllocation.BEST_TO_BEST){
            
        }
        
        for (Match m: matches){
            String insert = "insert into rooms first_prop, second_prop," + 
                "first_op, second_prop, location, round values(" +
                m.getFirstProp() + ", " +
                m.getSecondProp() + ", " +
                m.getFirstOp() + ", " +
                m.getSecondOp() + ", " +
                m.getLocation() + ", " +
                round;
        }
    }
}
