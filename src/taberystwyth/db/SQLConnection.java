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

package taberystwyth.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Observable;

import javax.swing.JOptionPane;

import taberystwyth.view.OverviewFrame;

/**
 * A clever wrapper for the Connection class that provides Observer/Observable
 * for notification about changes as well as singleton behavior
 * 
 * @author Cal Paterson
 */
public class SQLConnection extends Observable {

	/**
	 * The instance of this (singleton) object
	 */
	private static SQLConnection instance = new SQLConnection();

	/**
	 * Returns the instance of this (singleton) object
	 * 
	 * @return
	 */
	public static SQLConnection getInstance() {
		return instance;
	}

	/**
	 * The current database file
	 */
	private File dbfile;

	/**
	 * The current connection
	 */
	private Connection conn;

	private boolean changeTracking = true;

	/**
	 * Constructor
	 */
	private SQLConnection() {
		/*
		 * Ensure that the SQL driver is pulled through the class loader
		 */
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			panic(e, "Unable to load the database driver."
					+ "Perhaps your computer architecture is not supported?");
		}

		/*
		 * Load the database FIXME: the user should be consulted about which one
		 */
		setDatabase(new File("taberystwyth.tab"));
	}

	/**
	 * Evaluates a given SQL file against the given connection.
	 * 
	 * @param conn
	 *            connection to SQL database
	 * @param filePath
	 *            path of SQL file
	 * @throws IOException
	 *             if the file is not found
	 * @throws SQLException
	 *             if there is some problem with the SQL server
	 */
	private synchronized void evaluateSQLFile(String filePath) {
		System.out.println("SQLConnection.evaluateSQLFile: evaluating - "
				+ filePath);
		char[] cbuf = new char[2000];
		try {
			new BufferedReader(new FileReader(new File(filePath))).read(cbuf);
			conn.createStatement().execute(new String(cbuf));
		} catch (Exception e) {
			panic(e,
					"Unable to evaluate this file:\n"
							+ new File(filePath).getAbsolutePath());
		}
		System.out.println("SQLConnection.evaluateSQLFile()");
		setChanged();
		notifyObservers();
	}

	/**
	 * There are so many instances where an unfixable situation arises in this
	 * file that I have defined this shorthand.
	 * 
	 * @param e
	 *            an exception
	 * @param reason
	 *            a Very Informative Description of the problem
	 */
	public synchronized void panic(Exception e, String reason) {
	    String message = reason + "\n" + "The exact exception was:\n" +
	        e.getMessage();
		JOptionPane.showMessageDialog(null, message);
		e.printStackTrace();
		System.exit(255);
	}

	/**
	 * Execute an SQL statement against the database
	 * 
	 * @param statement
	 *            statement
	 * @return success or failure
	 */
	public synchronized boolean execute(String statement) {
		boolean returnValue = false;
		try {
			/*
			 * Order seems to be important here because the semantics of sqlite
			 * are quite odd. Closing the connection is required in order to
			 * flush updates to the database file
			 */
			Statement stmt = conn.createStatement();
			returnValue = stmt.execute(statement);
			stmt.close();
			conn.close();
			conn = DriverManager.getConnection("jdbc:sqlite:"
					+ dbfile.getAbsolutePath());
		} catch (SQLException e) {
			panic(e, "Unable to execute this statement against the database:\n"
					+ statement);
		}
		System.out.println("SQLConnection.execute()");
		setChanged();
		notifyObservers();
		return returnValue;
	}

	/**
	 * Execute an SQL query against the database
	 * 
	 * @param query
	 *            query
	 * @return resultset
	 */
	public synchronized ResultSet executeQuery(String query) {
		ResultSet returnValue = null;
		try {
			Statement stmt = conn.createStatement();
			returnValue = stmt.executeQuery(query);
		} catch (SQLException e) {
			panic(e, "Unable to execute this query against the database:\n"
					+ query);
		}
		return returnValue;
	}

	/**
	 * Set the database file, and loads the schema into it if required.
	 * @param file sqlite3 file
	 */
	public synchronized void setDatabase(File file) {
		try {
			/*
			 * First, load the file
			 */
			dbfile = file;
			conn = DriverManager.getConnection("jdbc:sqlite:"
					+ file.getAbsolutePath());
			
			/*
			 * This line is required in order to unfuck sqlites' default
			 * behaviour, which is to ignore fk constraints
			 */
			execute("PRAGMA foreign_keys = ON;");

			/*
			 * FIXME: This section should contain magic that checks the 
			 * version of the database schema, and throws errors if it doesn't
			 * match, etc etc.  Currently, it does not.
			 */
			int version = 0;
			try {
			    Statement stmt = conn.createStatement();
				ResultSet rs = 
				    stmt.executeQuery("select version from version;");
				rs.next();
				rs.getInt("version");
			} catch (SQLException e) {
			    /*
			     * Unable to work out the version of the schema, it's likely
			     * that the file selected is "new".  Ask the user whether he
			     * wants to make this file into a tab file
			     */
			    File schema =  new File("data/schema.sql");
			    JOptionPane.showOptionDialog(null, 
			            "The file " + schema.getAbsoluteFile() + " does not " +
			            "seem to be a tabfile.  Overwrite it with a tab" +
			            "file?", "Not a tab file", 
			            JOptionPane.YES_NO_OPTION,
			            JOptionPane.ERROR_MESSAGE, null, null, null);
				panic(e, "Unable to determine the current version of the tab");
			}

			/*
			 * If the version number is not what we expect it to be, reload
			 * the database.  FIXME: for the moment, we never reload.
			 */

		} catch (SQLException e) {
			panic(e, "Unable to load the following file:\n" + file);
			changeTracking = true;
		}
		System.out.println("SQLConnection.setDatabase()");
		setChanged();
		notifyObservers();
	}

	/**
	 * An override that changes the visibility (the thread-safety) of the 
	 * superclasses' setChanged method
	 */
    public synchronized void setChanged() {
		/*
		 * We might not be tracking changes at the moment (ie: we are loading
		 * the schema into a new tab.  If so, do not track changes).
		 */
		if (changeTracking){
			super.setChanged();
		}
	}

}
