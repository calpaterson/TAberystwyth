/* This file is part of TAberystwyth, a debating competition organiser
 * Copyright (C) 2010, Roberto Sarrionandia and Cal Paterson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package taberystwyth.prelim;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JOptionPane;

import taberystwyth.db.SQLConnection;
import taberystwyth.view.OverviewFrame;

public class Allocator {

	SQLConnection conn = SQLConnection.getInstance();

	private static Allocator instance = new Allocator();

	public static Allocator getInstance() {
		return instance;
	}

	private Allocator() {
	}
	
	public void allocate() throws SQLException, SwingTeamsRequiredException, 
	LocationsRequiredException, JudgesRequiredException{
		/*
		 * Subquery used inside the loop the builds teams
		 */
		String subQuery;
		ResultSet subrs;
		
		/*
		 * Build the list of teams
		 */
		TreeMap<String,Integer> teamNames = new TreeMap<String,Integer>();
		String query = "select name from teams;";
		ResultSet rs = conn.executeQuery(query);
			while(rs.next()){
				String teamName = rs.getString("name");
				
				/*
				 * 
				 */
				int teamPoints = 0;
				subQuery = "select position from team_results where team = '" + 
						teamName + 
						"'";
				subrs = conn.executeQuery(subQuery);
				while (subrs.next()){
					int position = subrs.getInt(1);
					if (position == 1){
						teamPoints += 3;
					} else if (position == 2){
						teamPoints += 2;
					} else if (position == 1){
						teamPoints += 1;
					}
				}
				
				
				teamNames.put(teamName,teamPoints);
			}
			rs.close();
		
		/*
		 * Build the list of judges
		 */
		TreeMap<String, Integer> judgeNames = new TreeMap<String,Integer>();
		query = "select name, rating from judges;";
		rs = conn.executeQuery(query);
		while (rs.next()){
		    judgeNames.put(rs.getString("name"), rs.getInt("rating"));
		}
		
			
		/*
		 * Build the list of locations
		 */
		TreeMap<String,Integer> locationNames = new TreeMap<String,Integer>();
		query = "select name, rating from locations;";
		rs = conn.executeQuery(query);
			while(rs.next()){
				locationNames.put(rs.getString("name"), 
						rs.getInt("rating"));
			}
			rs.close();
		
		/*
		 * Ensure there are enough teams
		 */
		int swingTeamsNeeded = 0;
		if (teamNames.size() % 4 != 0){
			swingTeamsNeeded = 4 - (teamNames.size() % 4);
	         System.out.println("Teams: " + teamNames.size());
	         System.out.println("Rem: " + teamNames.size() % 4);
		}
		if (swingTeamsNeeded > 0){
		    throw new SwingTeamsRequiredException(swingTeamsNeeded);
		}
		
		/*
		 * Ensure that there are enough locations
		 */
		int locationsRequired = teamNames.size() / 4;
		int locationsShort = locationsRequired - locationNames.size();
		if (locationsShort > 0){
			throw new LocationsRequiredException(locationsShort);
		}
		
		/*
		 * Ensure there are enough judges
		 */
		int requiredJudges = teamNames.size() / 4;
		if (judgeNames.size() < requiredJudges){
		    throw new JudgesRequiredException(requiredJudges - 
		            judgeNames.size());
		}
		
	}

	private int getCurrentRound() {
		int returnValue = 0; 

		/*
		 * Check that there are tables in results, because if there aren't, this
		 * is the first round.
		 */
		String query = "select count (*) from team_results;";
		ResultSet rs = conn.executeQuery(query);
		int nEntriesInResults = 0;
		try{
			rs.next();
			nEntriesInResults = rs.getInt(1);
			rs.close();
		} catch (SQLException e){
			conn.panic(e, "Unable to count the number of tables in results.  Query was:\n" +
					query);
		}
		
		if (nEntriesInResults < 1) {
			returnValue = 0;
		} else {
			query = "select max (round) from team_results;";
			returnValue = 0;
			try {
				rs = conn.executeQuery(query);
				rs.next();
				returnValue = rs.getInt(1);
				rs.close();
			} catch (SQLException e) {
				conn.panic(e,
						"Unable to find out the number of rounds.  Query was:\n"
								+ query);
			}
		}
		return returnValue;
	}
	
	private void panic(String m){
		JOptionPane.showMessageDialog(OverviewFrame.getInstance(), 
				m);
	}
	
}
