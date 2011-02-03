/*
 * This file is part of TAberystwyth, a debating competition organiser
 * Copyright (C) 2010, Roberto Sarrionandia and Cal Paterson
 * 
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package taberystwyth.allocation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

import taberystwyth.allocation.options.JudgeAllocation;
import taberystwyth.allocation.options.LocationAllocation;
import taberystwyth.allocation.options.TeamAllocation;
import taberystwyth.allocation.exceptions.*;
import taberystwyth.db.SQLConnection;

/**
 * A singleton class for the allocation algorithm
 * 
 * @author Roberto Sarrionandia and Cal Paterson
 */
public final class Allocator {
    
    private static final long INITIAL_SEED = 0L;
    private transient final Random randomGenerator = new Random(INITIAL_SEED);
    
    private transient final SQLConnection conn = SQLConnection.getInstance();
    
    private static Allocator instance = new Allocator();
    
    /**
     * Returns the (single instance) of this singleton
     * 
     * @return the instance
     */
    public static Allocator getInstance() {
        return instance;
    }
    
    /**
     * Private constructor
     */
    private Allocator() {/* VOID */
    };
    
    /**
     * Allocates teams, judges and locations to each other and puts the results
     * in the database
     * 
     * @param teamAlgo
     * @param judgeAlgo
     * @param locationAlgo
     * @throws SQLException
     * @throws SwingTeamsRequiredException
     * @throws LocationsRequiredException
     * @throws JudgesRequiredException
     */
    public void allocate(final TeamAllocation teamAlgo,
            final JudgeAllocation judgeAlgo,
            final LocationAllocation locationAlgo) throws SQLException,
            SwingTeamsRequiredException, LocationsRequiredException,
            JudgesRequiredException {
        
        /*
         * Two names that will be used repeatedly
         */
        ResultSet rs;
        String query;
        
        /*
         * Figure out the current round
         */
        int round;
        rs = conn.executeQuery("select max(round) from rooms;");
        if (rs.next()) {
            round = rs.getInt(1) + 1;
        } else {
            round = 0;
        }
        rs.close();
        
        /*
         * Generate matches
         */
        query = "select count(*) / 4 from teams;";
        rs = conn.executeQuery(query);
        rs.next();
        final int nMatches = rs.getInt(1);
        final ArrayList<Match> matches = new ArrayList<Match>();
        int rank = 0;
        while (matches.size() < nMatches) {
            matches.add(new Match(rank));
            ++rank;
        }
        rs.close();
        
        /*
         * Allocate teams
         */
        if (teamAlgo == TeamAllocation.WUDC) {
            final TreeMap<Integer, ArrayList<String>> pools = getLeveledPools();
            
            final int highestTeamScore = pools.lastKey();
            
            // FIXME: What if highest team score is zero? (Initial round)
            /*
             * For every possible (leveled) pool...
             */
            rank = 0;
            for (int treeIndex = highestTeamScore; treeIndex >= 0; --treeIndex) {
                /*
                 * ...if the pool exists
                 */
                if (pools.containsKey(treeIndex)) {
                    /*
                     * ...then build random rooms
                     */
                    ArrayList<String> pool = pools.get(treeIndex);
                    int poolSizeDiv4 = pool.size() / 4;
                    for (int i = 0; i < poolSizeDiv4; ++i) {
                        
                        Match match = matches.get(rank);
                        
                        /*
                         * Give all of the teams a random position
                         */
                        match.setFirstProp(pool.remove(randomGenerator
                                .nextInt(pool.size())));
                        match.setFirstOp(pool.remove(randomGenerator
                                .nextInt(pool.size())));
                        match.setSecondProp(pool.remove(randomGenerator
                                .nextInt(pool.size())));
                        match.setSecondOp(pool.remove(randomGenerator
                                .nextInt(pool.size())));
                        
                        rank++;
                    }
                }
            }
            
        }
        
        /*
         * Allocate Judges
         */
        if (judgeAlgo == JudgeAllocation.BALANCED) {
            query = "select name from judges order by rating desc";
            rs = conn.executeQuery(query);
            /*
             * Allocate wings in a round-robin way
             */
            int index = 0;
            while (rs.next()) {
                Match match = matches.get(index);
                if (match.hasChair()) {
                    match.addWing(rs.getString(1));
                } else {
                    match.setChair(rs.getString(1));
                }
                /*
                 * loop back over the matches
                 */
                ++index;
                if (index >= matches.size()) {
                    index = 0;
                }
            }
        }
        
        /*
         * Allocate locations
         */
        if (locationAlgo == LocationAllocation.RANDOM) {
            rs = conn
                    .executeQuery("select name from locations order by random();");
            int i = 0;
            while (rs.next() && i < matches.size()) {
                matches.get(i).setLocation(rs.getString(1));
                ++i;
            }
            
        } else if (locationAlgo == LocationAllocation.BEST_TO_BEST) {
            /* VOID */
        }
        
        for (Match m : matches) {
            String roomInsert = "insert into rooms (first_prop, second_prop, "
                    + "first_op, second_op, location, round) values(" + "'"
                    + m.getFirstProp() + "'" + ", " + "'" + m.getSecondProp()
                    + "', " + "'" + m.getFirstOp() + "', " + "'"
                    + m.getSecondOp() + "', " + "'" + m.getLocation() + "', "
                    + round + ");";
            conn.execute(roomInsert);
            System.out.println(roomInsert);
            String chairInsert = "insert into judging_panels (name, round, "
                    + "room, isChair) values(" + "'" + m.getChair() + "'"
                    + ", " + round + ", '" + m.getLocation() + "'" + ", "
                    + "1);";
            conn.execute(chairInsert);
            System.out.println(chairInsert);
            for (String w : m.getWings()) {
                String wingInsert = "insert into judging_panels (name, round, "
                        + "room, isChair) values(" + "'" + w + "'" + ", "
                        + round + ", " + "'" + m.getLocation() + "'" + ", "
                        + "0);";
                conn.execute(wingInsert);
                System.out.println(wingInsert);
            }
            
        }
    }
    
    private TreeMap<Integer, ArrayList<String>> getLeveledPools()
            throws SQLException {
        TreeMap<Integer, ArrayList<String>> pools = getPools();
        
        /*
         * For each pool, starting at the top (highest ranked pool) and working
         * down...
         */
        for (int i = pools.lastKey(); i > 0; --i) {
            /*
             * if the pool exists...
             */
            if (pools.containsKey(i)) {
                /*
                 * and the pools size is not a multiple of 4...
                 */
                while (!((pools.get(i).size() % 4) == 0)) {
                    /*
                     * then pull up (randomly) a member from the pool directly
                     * below this one
                     */
                    for (int j = (i - 1); j > 0; --j) {
                        if (pools.containsKey(j)) {
                            ArrayList<String> lowerPool = pools.get(j);
                            int randomElementIndex = randomGenerator
                                    .nextInt(lowerPool.size());
                            pools.get(i).add(
                                    pools.get(j).remove(randomElementIndex));
                            
                            /*
                             * If the arraylist is empty then delete it
                             */
                            if (lowerPool.size() == 0) {
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
    
    private TreeMap<Integer, ArrayList<String>> getPools() throws SQLException {
        /*
         * Construct the map of points to teams (pools)
         */
        TreeMap<Integer, ArrayList<String>> pools = new TreeMap<Integer, ArrayList<String>>();
        
        /*
         * Get the map of team names to points
         */
        HashMap<String, Integer> points = getTeamPoints();
        
        /*
         * For each team, add it to map of pools
         */
        for (String team : points.keySet()) {
            int innerPoints = points.get(team);
            /*
             * If the pool does not exist, create it
             */
            if (!pools.containsKey(innerPoints)) {
                pools.put(innerPoints, new ArrayList<String>());
            }
            pools.get(innerPoints).add(team);
        }
        
        return pools;
    }
    
    /**
     * Returns a map of teams to team points
     * 
     * @return A HashMap of Team names and Team points
     * @throws SQLException
     */
    protected HashMap<String, Integer> getTeamPoints() throws SQLException {
        HashMap<String, Integer> teamScores = new HashMap<String, Integer>();
        String query;
        ResultSet rs;
        
        /*
         * Check if any rounds have happened yet, if not set all scores to 0
         */
        query = "select count (*) from team_results;";
        rs = SQLConnection.getInstance().executeQuery(query);
        rs.next();
        if (rs.getInt(1) == 0) {
            for (String t : getTeamNames()) {
                teamScores.put(t, 0);
            }
            return teamScores;
        }
        
        /*
         * Otherwise, calculate the total team score for each team, and add the
         * team to the map
         */
        for (String name : getTeamNames()) {
            int teamPoints = 0;
            query = "select position from team_results where team = '" + name
                    + "';";
            rs = SQLConnection.getInstance().executeQuery(query);
            
            /*
             * Sum the team points according to positions taken in rounds
             */
            while (rs.next()) {
                int position = rs.getInt("position");
                if (position == 1) {
                    teamPoints += 3;
                } else if (position == 2) {
                    teamPoints += 2;
                } else if (position == 3) {
                    teamPoints += 1;
                }
            }
            teamScores.put(name, teamPoints);
            
        }
        
        return teamScores;
    }
    
    /**
     * Gets an unordered list of team names
     * 
     * @return List of teams
     * @throws SQLException
     */
    protected ArrayList<String> getTeamNames() throws SQLException {
        ArrayList<String> teamNames = new ArrayList<String>();
        String query = "select name from teams;";
        ResultSet rs = SQLConnection.getInstance().executeQuery(query);
        while (rs.next()) {
            teamNames.add(rs.getString("name"));
        }
        return teamNames;
    }
    
}
