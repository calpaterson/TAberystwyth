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

package taberystwyth.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

/**
 * A clever wrapper for the Connection class that provides Observer/Observable
 * for notification about changes as well as singleton behaviour
 * 
 * @author Cal Paterson
 */
public class SQLConnection extends Observable implements Runnable {
    
    private static final Logger log = Logger.getLogger(SQLConnection.class);
    
    /**
     * The frequency (in milliseconds) with which this singleton notifies
     * observers 
     */
    private static final int NOTIFY_FREQUENCY = 100;
    
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
     * This flag indicates whether observers are notified of updates. If there
     * are a lot of updates going on very frequently, it's better to disable
     * change tracking, make the change and then call //FIXME
     */
    private boolean changeTracking = true;
    
    public synchronized boolean isChangeTracking() {
        return changeTracking;
    }
    
    public synchronized void setChangeTracking(boolean changeTracking) {
        this.changeTracking = changeTracking;
        setChanged();
    }
    
    /**
     * The current connection
     */
    private Connection conn;
    
    /**
     * The current database file
     */
    private File file;
    
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
            log.fatal("Unable to load database driver");
            panic(e, "Unable to load the database driver."
                    + "Perhaps your computer architecture is not supported?");
        }
        
        /*
         * No initialisation here
         */
    }
    
    public void start() {
       Thread thread = new Thread(instance);
       thread.setName("SQL");
       thread.start();
    }
    
    /**
     * Creates a tab in a given location
     * 
     * @param file
     *            the given location
     * @throws IOException
     * @throws SQLException
     */
    public synchronized void create(File file) throws IOException,
            SQLException {
        if (file.exists()) {
            throw new IOException("tab already exists");
        }
        
        this.file = file;

        conn = DriverManager.getConnection("jdbc:sqlite:"
                + file.getAbsolutePath());
        InputStream schema = this.getClass()
                .getResourceAsStream("/schema.sql");
        evaluateSQLFile(schema);
        
    }
    
    /**
     * Evaluates a given SQL file against the current connection.
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
    private synchronized void evaluateSQLFile(InputStream file) {
        char[] cbuf = new char[4000];
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(file);
            br = new BufferedReader(new InputStreamReader(file));
            br.read(cbuf);
            
            /*
             * FIXME: This block is some disgusting magic that loads all of the
             * SQL statements in the given file
             */
            String fileContents = new String(cbuf);
            String[] statements = fileContents.split(";");
            for (int i = 0; i < (statements.length - 1); ++i) {
                statements[i] = statements[i].concat(";");
                // System.out.println(statements[i]);
                conn.createStatement().execute(statements[i]);
            }
            log.info("Evaluated SQL file");
            /*
             * FIXME: Change this method's argument type to File, so that the 
             * log can hold the absolute file path
             */
        } catch (Exception e) {
            panic(e, "Unable to evaluate SQL file");
        } finally {
            try {
                br.close();
                isr.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        setChanged();
        notifyObservers();
    }
    
    /**
     * Execute an SQL statement against the database
     * 
     * @param statement
     *            statement
     * @return success or failure
     * @throws SQLException 
     */
    @Deprecated
    public synchronized boolean execute(String statement){
        boolean returnValue = false;
        /*
         * Order seems to be important here because the semantics of sqlite are
         * quite odd. Closing the connection is required in order to flush
         * updates to the database file
         */
        try{
        Statement stmt = conn.createStatement();
        returnValue = stmt.execute(statement);
        stmt.close();
        conn.close();
        conn = DriverManager.getConnection("jdbc:sqlite:"
                + file.getAbsolutePath());
        } catch (SQLException e2){
            
        }
        log.info("Executed: " + statement);
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
            log.info("Executed query: " + query);
        } catch (SQLException e) {
            panic(e, "Unable to execute this query against the database:\n"
                    + query);
        }
        return returnValue;
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
    @Deprecated
    public synchronized void panic(Exception e, String reason) {
        String message = reason + "\n" + "The exact exception was:\n"
                + e.getMessage();
        JOptionPane.showMessageDialog(null, message);
        e.printStackTrace();
        System.exit(255);
    }
    
    /**
     * An override that changes the visibility (the thread-safety) of the
     * superclasses' setChanged method
     */
    @Override
    protected synchronized void setChanged() {
        /*
         * We might not be tracking changes at the moment (ie: we are loading
         * the schema into a new tab. If so, do not track changes).
         */
        if (changeTracking) {
            super.setChanged();
        }
    }
    
    /**
     * Set the database file, and loads the schema into it if required.
     * 
     * @param file
     *            sqlite3 file
     * @throws Exception
     */
    public synchronized void set(File file) throws Exception {
        /*
         * First, load the file
         */
        this.file = file;
        conn = DriverManager.getConnection("jdbc:sqlite:"
                + file.getAbsolutePath());
        
        /*
         * This line is required in order to unfuck sqlites' default behaviour,
         * which is to ignore fk constraints
         */
        Statement st = conn.createStatement();
        st.execute("PRAGMA foreign_keys = ON;");
        st.close();
        cycleConn();
        
        log.info("Database set to " + file.getAbsolutePath());
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * This method closes the current connection and opens a new one.
     * 
     * This is complete hack but is absolutely required because if this isn't
     * done then sqlite won't notice any updates that are done to the code.
     * @throws SQLException
     */
    public void cycleConn() throws SQLException{
        synchronized(this){
            conn.close();
            conn = DriverManager.getConnection("jdbc:sqlite:"
                    + file.getAbsolutePath());
            Statement statement = conn.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.close();
            log.info("Cycled connection");
        }
        setChanged();
    }

    public Connection getConn() {
        return conn;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(NOTIFY_FREQUENCY); //FIXME: finalise
                notifyObservers();
            } catch (InterruptedException e) {
                /*
                 * If we are interrupted, then do nothing
                 */
                log.warn("Interrupted", e);
            }
        }
    }
    
    @Override
    public void addObserver(Observer observer){
        super.addObserver(observer);
        log.info("Observer added: " + observer);
        /*
         * setChanged() is not designed for this kind of use, but the 
         * intention here is that this ensures that in the next loop 
         * the observers are told to repull
         */
        setChanged();
    }
    
}
