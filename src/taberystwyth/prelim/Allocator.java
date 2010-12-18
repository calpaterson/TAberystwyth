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
	
	public void allocate(){
		/*
		 * Subquery used inside the loop the builds teams
		 */
		String subQuery;
		ResultSet subrs;
		
		/*
		 * Build the list of teams
		 */
		TreeMap<String,Integer> teamNames = new TreeMap<String,Integer>();
		String query = "select name from team;";
		ResultSet rs = conn.executeQuery(query);
		try {
			while(rs.next()){
				String teamName = rs.getString("name");
				
				/*
				 * 
				 */
				int teamPoints = 0;
				subQuery = "select position from result where team = '" + 
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
		} catch (SQLException e) {
			conn.panic(e, "Unable to cycle through the list of teams");
		}
		
		/*
		 * Build the list of locations
		 */
		TreeMap<String,Integer> locationNames = new TreeMap<String,Integer>();
		query = "select name, rating from location;";
		rs = conn.executeQuery(query);
		try {
			while(rs.next()){
				locationNames.put(rs.getString("name"), 
						rs.getInt("rating"));
			}
			rs.close();
		} catch (SQLException e){
			conn.panic(e, "Unable to look through the list of locations");
		}
		
		/*
		 * Couple of pieces of behaviour to ensure that the number of teams is
		 * divisible by four
		 */
		int swingTeamsNeeded = 0;
		if (teamNames.size() % 4 != 0){
			swingTeamsNeeded = 4 - (teamNames.size() % 4);
	         System.out.println("Teams: " + teamNames.size());
	         System.out.println("Rem: " + teamNames.size() % 4);
		}
		if (swingTeamsNeeded != 0){
			String complaint;
			if (swingTeamsNeeded == 1){
				complaint = "Number of teams not divisible by four.  " +
				"You need to add a (single) swing team.";
			} else {
				complaint = "Number of teams not divisible by four.  " +
				"You need to add " + swingTeamsNeeded + 
				" swing teams.";
			}
			JOptionPane.showMessageDialog(OverviewFrame.getInstance(), 
					complaint, "Add swing teams", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		/*
		 * Ensure that there are enough locations
		 */
		int locationsRequired = teamNames.size() / 4;
		int locationsShort = locationsRequired - locationNames.size();
		if (locationsShort > 0){
			String complaint = "You need to add at least " + locationsShort +
			"locations";
			JOptionPane.showMessageDialog(OverviewFrame.getInstance(), 
					complaint, "Add locations", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		/*
		 * Printing!
		 */
//		System.out.println("Allocator.allocate()");
//		for (String team: teamNames.keySet()){
//			System.out.println(team + ": " + teamNames.get(team));
//		}
//		for (String location: locationNames.keySet()){
//			System.out.println(location + ": " + locationNames.get(location));
//		}
//		System.out.println(locationNames.firstKey());
	}

	private int getCurrentRound() {
		int returnValue = 0; 

		/*
		 * Check that there are tables in results, because if there aren't, this
		 * is the first round.
		 */
		String query = "select count (*) from result;";
		ResultSet rs = conn.executeQuery(query);
		int n_tables = 0;
		try{
			rs.next();
			n_tables = rs.getInt(1);
			rs.close();
		} catch (SQLException e){
			conn.panic(e, "Unable to count the number of tables in results.  Query was:\n" +
					query);
		}
		if (n_tables < 1) {
			returnValue = 0;
		} else {
			query = "select max (round_number) from result;";
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
