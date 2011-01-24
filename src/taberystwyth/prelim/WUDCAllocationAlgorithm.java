package taberystwyth.prelim;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

public class WUDCAllocationAlgorithm extends TeamAllocationAlgorithm {
    
    Random randomGenerator = new Random(0L);
    
    @Override
    ArrayList<String> allocate() throws SQLException {
        ArrayList<String> returnValue = new ArrayList<String>();
        TreeMap<Integer, ArrayList<String>> pools = getLeveledPools();
        
        /**
         * Shuffle the pools to get random positions FIXME
         */
        for (Integer i: pools.keySet()){
            ArrayList<String> pool = pools.get(i);
            ArrayList<String> shuffledPool = new ArrayList<String>();
            while(!pool.isEmpty()){
                shuffledPool.add(pool.get(randomGenerator.nextInt(pool.size())));
            }
            returnValue.addAll(shuffledPool);
        }
        return returnValue;
    }
    
    /**
     * Get the pools after they have been leveled to be 
     * divisble by four
     * @return TreeMap of pools, each is divisble by four
     * @throws SQLException
     */
    private TreeMap<Integer, ArrayList<String>> getLeveledPools()
        throws SQLException{
        TreeMap<Integer, ArrayList<String>> pools = 
            new TreeMap<Integer, ArrayList<String>>();
        
        /*
         * For each pool, starting at the top (highest ranked pool) and 
         * working down...
         */
        for (int i = pools.lastKey(); i > 0; --i){
            /*
             * if the pool exists...
             */
            if (pools.containsKey(i)){
                /*
                 * and the pools size is not a multple of 4...
                 */
                while (!((pools.get(i).size() % 4) == 0)){
                    /*
                     * then pull up (randomly) a member from the pool directly
                     * below this one
                     */
                    for (int j = (i - 1); i > 0; --j){
                        if (pools.containsKey(j)){
                            ArrayList<String> lowerPool = pools.get(j);
                            int randomElementIndex = 
                                randomGenerator.nextInt(lowerPool.size());
                            pools.get(i).add(pools.get(j).remove(randomElementIndex));
                            
                            /*
                             * If the arraylist is empty then delete it
                             */
                            if (lowerPool.size() == 0){
                                pools.remove(j);
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        return pools;
    }
    
    private TreeMap<Integer, ArrayList<String>> getPools() throws SQLException{
        /*
         * Construct the map of points to teams (pools)
         */
        TreeMap<Integer, ArrayList<String>> pools = 
            new TreeMap<Integer, ArrayList<String>>();
        
        /*
         * Get the map of team names to points
         */
        HashMap<String, Integer> points = getTeamPoints();
        
        /*
         * For each team, add it to map of pools
         */
        for (String team: points.keySet()){
           int innerPoints = points.get(team);
           /*
            * If the pool does not exist, create it
            */
           if (!pools.containsKey(innerPoints)){
               pools.put(innerPoints, new ArrayList<String>());
           }
           pools.get(innerPoints).add(team);
        }
        
        return pools;
    }
    
}
