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

package taberystwyth.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import taberystwyth.controller.OverviewFrameMenuListener;
import taberystwyth.controller.TeamListListener;
import taberystwyth.db.TabServer;

final public class OverviewFrame extends JFrame implements Observer {
    
    private static final long serialVersionUID = 1L;
    private OverviewFrameMenuListener menuListener = new OverviewFrameMenuListener();
    
    private final static Logger LOG = Logger.getLogger(OverviewFrame.class);
    
    private boolean debug = false;
    
    /*
     * Models
     */
    DefaultListModel speakerModel;
    DefaultListModel judgeModel;
    DefaultListModel locationModel;
    
    private static OverviewFrame instance = new OverviewFrame();
    
    public static OverviewFrame getInstance() {
        return instance;
    }
    
    private OverviewFrameMenu menu;
    
    private OverviewFrame(){
        /* VOID */
    }
    
    public void createAndShow() {
        setLayout(new BorderLayout());
        
        /*
         * Set the window title with the name of the tournament
         */
        try {
            Connection conn = TabServer.getConnectionPool().getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(
                            "select \"name\" from tournament_name limit 1;");
            rs.next();
            setTitle(rs.getString(1));
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e){
            LOG.fatal("Unable to get the tournamment name from the " + 
                            "database", e);
            System.exit(1);
        }
        
        /*
         * When the window is closed, exit the program
         */
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        /*
         * Set up the models...
         */
        speakerModel = new DefaultListModel();
        judgeModel = new DefaultListModel();
        locationModel = new DefaultListModel();
        
        /*
         * ...and the lists
         */
        JList speakerList = new JList(speakerModel);
        JList judgeList = new JList(judgeModel);
        JList locationList = new JList(locationModel);
        
        speakerList.addMouseListener(new TeamListListener());
        
        /*
         * Add menu bar
         */
        menu = new OverviewFrameMenu(new OverviewFrameMenuListener());
        add(menu, BorderLayout.NORTH);
        if (debug){
            menu.setDebug(true);
        }
        
        /*
         * Holding panel
         */
        JPanel holdingPanel = new JPanel(new BorderLayout());
        
        /*
         * Title Panel
         */
        JPanel titlePanel = new JPanel(new GridLayout(1, 3));
        titlePanel.add(new JLabel("Judges"));
        titlePanel.add(new JLabel("Teams"));
        titlePanel.add(new JLabel("Locations"));
        holdingPanel.add(titlePanel, BorderLayout.NORTH);
        
        /*
         * View Panel
         */
        JPanel viewPanel = new JPanel(new GridLayout(1, 3));
        viewPanel.add(new JScrollPane(judgeList));
        viewPanel.add(new JScrollPane(speakerList));
        viewPanel.add(new JScrollPane(locationList));
        holdingPanel.add(viewPanel, BorderLayout.CENTER);
        add(holdingPanel, BorderLayout.CENTER);
        pack();
        setSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        setVisible(true);
        
        /*
         * Add myself as an observer and force and update
         */
        TabServer.getInstance().addObserver(this);
    }
    
    private void refreshTeams() {
        refreshList("teams", speakerModel);
    }
    
    private void refreshJudges() {
        refreshList("judges", judgeModel);
    }
    
    private void refreshLocation() {
        refreshList("locations", locationModel);
    }
    
    private void refreshList(String table, DefaultListModel model) {
        try {
            synchronized (model) {
                long start = System.currentTimeMillis();
                
                // What the view has
                Set<String> view = new HashSet<String>(2000);
                for (int i = 0; i < model.getSize(); ++i) {
                    view.add((String) model.get(i));
                }
                
                // What the db has
                Set<String> db = new HashSet<String>(2000);
                Connection sql = TabServer.getConnectionPool()
                                .getConnection();
                Statement statement = sql.createStatement();
                ResultSet rs = statement
                                .executeQuery("select \"name\" from "
                                                + table + ";");
                while (rs.next()) {
                    String entry = rs.getString(1);
                    db.add(entry);
                }
                rs.close();
                statement.close();
                sql.close();
                
                // Find out what is in the db but not in the view
                Set<String> db_t = new HashSet<String>(2000);
                db_t.addAll(db);
                db_t.removeAll(view);
                for (String s:db_t){
                    model.addElement(s);
                    LOG.info("Adding: " + s);
                }
                
                // FIXME: Things may have been deleted from the db, but not
                // registered in the view, so take account of that.
                view.removeAll(db);
                for (String s:view){
                    model.removeElement(s);
                    LOG.info("Removing: " + s);
                }
                
                long end = System.currentTimeMillis();
                LOG.debug("RefreshList() had the lock for " + table + 
                   " for " + ((end - start)) + " millis");
            }
            
        } catch (SQLException e) {
            LOG.error("Unable to update overview frame", e);
        }
    }
    
    private String getInstitution(final String teamName) {
        String query = null;
        String returnValue = null;
        try {
            Connection conn = TabServer.getConnectionPool().getConnection();
            /*
             * Get the speakers on the team
             */
            PreparedStatement teamStatement = conn
                            .prepareStatement("select \"speaker1\", \"speaker2\" from teams "
                                            + " where teams.\"name\" = ?;");
            teamStatement.setString(1, teamName);
            ResultSet rs = teamStatement.executeQuery();
            rs.next();
            String speaker1 = rs.getString(1);
            String speaker2 = rs.getString(2);
            teamStatement.close();
            
            /*
             * Get the institution of speaker1
             */
            query = "select \"institution\" from speakers where "
                            + "speakers.\"name\" = '" + speaker1 + "'";
            Statement instStatement = conn.createStatement();
            rs = instStatement.executeQuery(query);
            rs.next();
            String inst1 = rs.getString(1);
            rs.close();
            instStatement.close();
            
            /*
             * Get the institution of speaker2
             */
            query = "select \"institution\" from speakers where "
                            + "speakers.\"name\" = '" + speaker2 + "'";
            Statement statement = conn.createStatement();
            rs = statement.executeQuery(query);
            rs.next();
            String inst2 = rs.getString(1);
            rs.close();
            statement.close();
            conn.close();
            
            /*
             * Compare them
             */
            if (!inst1.equals(inst2)) {
                returnValue = "Composite";
            } else {
                returnValue = inst1;
            }
            
        } catch (SQLException e) {
            LOG.error("Unable to find what institution two speakers are from.",
                            e);
        }
        
        return returnValue;
    }
    
    @Override
    public void update(Observable o, Object arg) {
        LOG.debug("Updating view");
        refreshTeams();
        refreshJudges();
        refreshLocation();
    }
    
    /*
     * Returns the menubar being used by this frame (used only to enable the
     * debug menu)
     */
    public JMenuBar getMenu() {
        return menu;
    }

    public void setDebug(boolean debug) {
        this.debug=debug;    
    }
}
