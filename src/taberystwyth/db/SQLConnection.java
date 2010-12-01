package taberystwyth.db;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;

public class SQLConnection {
	
	private static SQLConnection instance = new SQLConnection();
	public static SQLConnection getInstance(){
		return instance;
	}
	
	Connection conn;
	String SQLError = "There was some kind of problem accessing the database.";
	
	private SQLConnection(){
		try {
			/*
			 * Ensure that the SQL driver is pulled through the class loader
			 */
			Class.forName("org.sqlite.JDBC");
			
			/*
			 * Load the database FIXME: this needs to be way more flexible
			 */
			conn = DriverManager.getConnection("jdbc:sqlite:taberystwyth.tab");
			
			/*
			 * If the tables don't already exist, load them.
			 */
			HashSet<String> expected = new HashSet<String>(){
				private static final long serialVersionUID = 1L;
				{
				add("LOCATION");
				add("PANEL");
				add("RESULTS");
				add("ROOM");
				add("SPEAKER");
				add("SPEAKER_POINTS");
				add("TEAM");
				add("SQLITE_AUTOINDEX_PANEL_1"); // automatic
			}};
			HashSet<String> actual = new HashSet<String>();
			
			/*
			 * Fill up the actual
			 */
			ResultSet rs = conn.getMetaData().getTables(null, null, null, null);
			while (rs.next()){
			    actual.add(rs.getString("TABLE_NAME"));
			}
			
			/*
			 * If not equal then load the sql files
			 */
			if (!actual.equals(expected)){
				String[] sqlFiles = {"location", "panel", "results", "room", "speaker", "speaker_points",
						"team"};
				for (String s: sqlFiles){
					System.out.println("SQLConnection: evaluating " + s + ".sql");
					evaluateSQLFile("data/" + s + ".sql");
				}
			}
			
			rs.close();
			} catch (Exception e) {
			String error = "There was some kind of SQL initialisation problem and I cannot continue.";
			JOptionPane.showMessageDialog(null, error);
			e.printStackTrace();
			System.exit(255);
		}
	}
	
	/**
	 * Execute an SQL query against the database
	 * @param query query
	 * @return resultset
	 */
	public synchronized ResultSet executeQuery(String query){
		ResultSet returnValue = null;
		try {
			Statement stmt = conn.createStatement();
			returnValue = stmt.executeQuery(query);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, SQLError);
			e.printStackTrace();
		}
		return returnValue;
	}
	
	/**
	 * Execute an SQL statement against the database
	 * @param statement statement
	 * @return success or failure
	 */
	public synchronized boolean execute(String statement){
		boolean returnValue = false;
		try {
			Statement stmt = conn.createStatement();
			returnValue = stmt.execute(statement);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, SQLError);
			e.printStackTrace();
		}
		return returnValue;
	}
	
    /**
     * Evaluates a given SQL file against the given connection.
     * @param conn connection to SQL database
     * @param filePath path of SQL file
     * @throws IOException if the file is  not found
     * @throws SQLException if there is some problem with the SQL server
     */
    private synchronized void evaluateSQLFile(String filePath) throws IOException, SQLException {
        char[] cbuf = new char[2000];
        new BufferedReader(new FileReader(new File(filePath))).read(cbuf);
        //System.out.println(new String(cbuf));
        conn.createStatement().execute(new String(cbuf));
    }

}
