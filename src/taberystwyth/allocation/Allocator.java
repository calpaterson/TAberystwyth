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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import taberystwyth.allocation.exceptions.JudgesRequiredException;
import taberystwyth.allocation.exceptions.LocationsRequiredException;
import taberystwyth.allocation.exceptions.SwingTeamsRequiredException;
import taberystwyth.allocation.options.TabAlgorithm;
import taberystwyth.db.TabServer;

/**
 * A singleton class for the allocation algorithm
 * 
 * @author Roberto Sarrionandia and Cal Paterson
 */
public final class Allocator {
    
    private static final long INITIAL_SEED = 0L;
    private transient final Random randomGenerator = new Random(INITIAL_SEED);
    private Connection conn;
    Logger LOG = Logger.getLogger(Allocator.class);
    
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
    private Allocator() {
        try {
            conn = TabServer.getConnectionPool().getConnection();
        } catch (SQLException e) {
            LOG.error("Can't connect to database", e);
        }
        
    };
    
    /**
     * Allocates teams, judges and locations to each other and puts the results
     * in the database.
     * 
     * @param teamAlgo
     *            the team algo
     * @param judgeAlgo
     *            the judge algo
     * @param locationAlgo
     *            the location algo
     * @throws SQLException
     *             the sQL exception
     * @throws SwingTeamsRequiredException
     *             the swing teams required exception
     * @throws LocationsRequiredException
     *             the locations required exception
     * @throws JudgesRequiredException
     *             the judges required exception
     */
    public void allocate(String motion, final TabAlgorithm teamAlgo,
                    final TabAlgorithm judgeAlgo,
                    final TabAlgorithm locationAlgo) throws SQLException,
                    SwingTeamsRequiredException, LocationsRequiredException,
                    JudgesRequiredException {
        try {
            /*
             * Two names that will be used repeatedly
             */
            ResultSet rs;
            PreparedStatement stmt;
            
            /*
             * Figure out the current round
             */
            int round;
            stmt = conn.prepareStatement("select max(\"round\") from rooms;");
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                round = rs.getInt(1) + 1;
            } else {
                round = 0;
            }
            rs.close();
            LOG.debug("Current round is: " + round);
            
            /*
             * Store the motion
             */
            stmt = conn.prepareStatement(
                "insert into motions (\"text\", \"round\") values (?,?);");
            stmt.setString(1, motion);
            stmt.setInt(2, round);
            stmt.execute();
            stmt.close();
            
            /*
             * Generate matches
             */
            stmt = conn.prepareStatement("select count(*) / 4 from teams;");
            rs = stmt.executeQuery();
            rs.next();
            final int nMatches = rs.getInt(1);
            LOG.debug("Will generate " + nMatches + " rooms");
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
            if (teamAlgo.getName().equals("WUDC")) {
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
            LOG.debug("Allocated teams");
            
            /*
             * Allocate Judges
             */
            if (judgeAlgo.getName().equals("Balanced")) {
                stmt = conn.prepareStatement(
                      "select \"name\" from judges order by \"rating\" desc");
                rs = stmt.executeQuery();
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
            LOG.debug("Allocated judges");
            
            /*
             * Allocate locations
             */
            if (locationAlgo.getName().equals("Random")) {
                stmt = conn.prepareStatement(
                        "select \"name\" from locations order by rand();");
                rs = stmt.executeQuery();
                int i = 0;
                while (rs.next() && i < matches.size()) {
                    matches.get(i).setLocation(rs.getString(1));
                    ++i;
                }
            } else if (locationAlgo.getName().equals("Best to Best")) {
                /* VOID */
            }
            LOG.debug("Allocated locations");
            
            /*
             * Now put all the matches into the database
             */
            for (Match m : matches) {
                String s = "insert into rooms "
                                + "(\"first_prop\", \"second_prop\","
                                + " \"first_op\", \"second_op\", "
                                + "\"location\", \"round\") values (?,?,?,?,?,?);";
                PreparedStatement p = conn.prepareStatement(s);
                p.setString(1, m.getFirstProp());
                p.setString(2, m.getSecondProp());
                p.setString(3, m.getFirstOp());
                p.setString(4, m.getSecondOp());
                p.setString(5, m.getLocation());
                p.setInt(6, round);
                p.execute();
                p.close();
                
                s = "insert into judging_panels " + "(\"name\", "
                                + "\"round\", " + "\"room\", "
                                + "\"isChair\") " + "values (?,?,?,?);";
                p = conn.prepareStatement(s);
                p.setString(1, m.getChair());
                p.setInt(2, round);
                p.setString(3, m.getLocation());
                p.setBoolean(4, true);
                p.execute();
                p.close();
                
                for (String w : m.getWings()) {
                    s = "insert into judging_panels " + "(\"name\", "
                                    + "\"round\", " + "\"room\", "
                                    + "\"isChair\") " + "values (?,?,?,?);";
                    p = conn.prepareStatement(s);
                    p.setString(1, w);
                    p.setInt(2, round);
                    p.setString(3, m.getLocation());
                    p.setBoolean(4, false);
                    p.execute();
                    p.close();
                }
            }
            conn.commit();
            LOG.debug("Everything went fine and is committed");
        } catch (SQLException e) {
            conn.rollback();
            LOG.debug("It didn't go well, rolled back");
            throw e;
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
                            pools.get(i)
                                            .add(pools.get(j)
                                                            .remove(randomElementIndex));
                            
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
    
    private TreeMap<Integer, ArrayList<String>> getPools()
                    throws SQLException {
        /*
         * Construct the map of points to teams (pools)
         */
        TreeMap<Integer, ArrayList<String>> pools = new TreeMap<Integer, ArrayList<String>>();
        
        /*
         * Get the map of team names to points
         */
        HashMap<String, Integer> points = getTeamPoints();
        
        /*
         * For each team, add it to the map of pools
         */
        for (Entry<String, Integer> entry : points.entrySet()) {
            String team = entry.getKey();
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
        ResultSet rs;
        synchronized (conn) {
            /*
             * Check if any rounds have happened yet, if not set all scores to
             * 0
             */
            PreparedStatement stmt = conn
                            .prepareStatement("select count (*) from team_results;");
            rs = stmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                for (String t : getTeamNames()) {
                    teamScores.put(t, 0);
                }
                return teamScores;
            }
            
            /*
             * Otherwise, calculate the total team score for each team, and add
             * the team to the map
             */
            for (String name : getTeamNames()) {
                int teamPoints = 0;
                stmt = conn.prepareStatement("select \"position\" from team_results where \"team\" = ?;");
                stmt.setString(1, name);
                rs = stmt.executeQuery();
                
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
        PreparedStatement stmt = conn
                        .prepareStatement("select \"name\" from teams;");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            teamNames.add(rs.getString("name"));
        }
        return teamNames;
    }
    
}
