package taberystwyth;

import javax.swing.*;

import taberystwyth.view.OverviewFrame;
import taberystwyth.view.TeamInsertionFrame;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello, World!");
		new OverviewFrame();
		//new TeamInsertionFrame();
		/*
		 * Ensure that the postgres driver is pulled through the class loader
		 */
		/*try {
			Class.forName("org.sqlite.JDBC");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("ERROR: sqlite driver not found.  Check the classpath includes the jar");
			System.exit(255);
		}
		try {
			Connection conn =
			      DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}