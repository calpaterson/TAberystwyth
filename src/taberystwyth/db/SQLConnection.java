package taberystwyth.db;

import java.sql.*;

import javax.swing.JOptionPane;

public class SQLConnection {
	
	private static SQLConnection instance = new SQLConnection();
	public static SQLConnection getInstance(){
		return instance;
	}
	
	Connection conn;
	String SQLError = "There was some kind of problem accessing the database.";
	
	private SQLConnection(){
		/*
		 * Ensure that the Sqlite driver is pulled through the class loader
		 */
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) {
			String error = "There was some kind of problem loading important libraries. \n" +
				"Your computer is probably not supported.";
			JOptionPane.showMessageDialog(null, error);
			e.printStackTrace();
			System.exit(255);
		}
		try {
			 conn = DriverManager.getConnection("jdbc:sqlite:taberystwyth.tab");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized ResultSet query(String query){
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
	
	public synchronized boolean insert(String insert){
		boolean returnValue = false;
		try {
			Statement stmt = conn.createStatement();
			returnValue = stmt.execute(insert);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, SQLError);
			e.printStackTrace();
		}
		return returnValue;
	}

}
