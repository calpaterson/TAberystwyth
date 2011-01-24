/**
 * 
 */
package taberystwyth.prelim;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import taberystwyth.db.SQLConnection;

/**
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 * 
 *         The abstract class for team allocation algorithms. Algorithms should
 *         inherit this class.
 * 
 */
public abstract class TeamAllocationAlgorithm extends AllocationAlgorithm {
    public ArrayList<String> getTeamNames() throws SQLException {
        ArrayList<String> teamNames = new ArrayList<String>();
        String query = "select name from teams;";
        ResultSet rs = SQLConnection.getInstance().executeQuery(query);
        while (rs.next()) {
            teamNames.add(rs.getString("name"));
        }
        return teamNames;
    }
    
    public HashMap<String, Integer> getTeamPoints() throws SQLException{
        HashMap<String,Integer> teamScores = new HashMap<String,Integer>();
        for (String name:getTeamNames()){
            int teamPoints = 0;
            String query = "select position from team_results where team = '" + name + "';";
            ResultSet rs = SQLConnection.getInstance().executeQuery(query);
            while(rs.next()){
                int position = rs.getInt("position");
                if (position == 1){
                    teamPoints += 3;
                } else if (position == 2){
                    teamPoints += 2;
                } else if (position == 3){
                    teamPoints += 1;
                } else {
                    // void
                }
            }
        }
        return teamScores;
    }
}
