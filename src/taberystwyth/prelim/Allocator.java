package taberystwyth.prelim;

import java.sql.ResultSet;
import java.sql.SQLException;

import taberystwyth.db.SQLConnection;

public class Allocator {

	SQLConnection conn = SQLConnection.getInstance();

	private static Allocator instance = new Allocator();

	public static Allocator getInstance() {
		return instance;
	}

	private Allocator() {
		System.out.println("Allocator.java: " + getCurrentRound());
	}
	
	public void allocate(){
		
	}

	private int getCurrentRound() {
		int returnValue = 0; 

		/*
		 * Check that there are tables in results, because if there aren't, this
		 * is the first round.
		 */
		String query = "select count (*) from results;";
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
			query = "select max (round_number) from results;";
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
}
