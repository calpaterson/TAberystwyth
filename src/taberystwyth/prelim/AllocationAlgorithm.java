/**
 * 
 */
package taberystwyth.prelim;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import taberystwyth.db.SQLConnection;

/**
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 * 
 * This interface ensures that all algorithms for allocating teams, judges
 * or locations supply a name and a description, and have a method to 
 * perform the allocation
 *
 */
public abstract class AllocationAlgorithm {
	
    String name;
    String description;
    
    
	
	public synchronized String getName() {
        return name;
    }

    public synchronized String getDescription() {
        return description;
    }

    /**
	 * Run the algorithm
     * @throws SQLException 
	 */
	abstract ArrayList<String> allocate() throws SQLException;
	
    public TreeMap<String, Integer> getLocationMap() throws SQLException{
        TreeMap<String,Integer> location2quality = 
            new TreeMap<String,Integer>();
        String query = "select name from locations;";
        ResultSet rs = SQLConnection.getInstance().executeQuery(query);
        while (rs.next()){
            location2quality.put(rs.getString("name"), rs.getInt("rating"));
        }
        return location2quality;
    }
    
    public TreeMap<Integer, ArrayList<String>> getJudgeMap() 
            throws SQLException{
        TreeMap<Integer, ArrayList<String>> quality2name = 
            new TreeMap<Integer,ArrayList<String>>();
        String query = "select name, rating from judges;";
        ResultSet rs = SQLConnection.getInstance().executeQuery(query);
        while (rs.next()){
            if (quality2name.get(rs.getInt("rating")) == null){
                quality2name.put(rs.getInt("rating"), 
                        new ArrayList<String>(
                                Arrays.asList(rs.getString("name"))));
            }
        }
        return quality2name;
    }

}
