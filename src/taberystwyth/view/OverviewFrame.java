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
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import taberystwyth.controller.OverviewFrameMenuListener;
import taberystwyth.controller.TeamListListener;
import taberystwyth.db.TabServer;

final public class OverviewFrame extends JFrame implements Observer {
    
    private static final long serialVersionUID = 1L;
    private OverviewFrameMenuListener menuListener = new OverviewFrameMenuListener();
    
    private final static Logger LOG = Logger.getLogger(OverviewFrame.class);
    
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
    
    private JMenuBar menu;
    
    private OverviewFrame() {
        
        setLayout(new BorderLayout());
        setTitle("TAberystwyth");
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
        model.removeAllElements();
        try {
            Connection sql = TabServer.getConnectionPool().getConnection();
            Statement statement = sql.createStatement();
            ResultSet rs = statement.executeQuery("select \"name\" from " + table + ";");
            int index = 0;
            while (rs.next()) {
                String entry = rs.getString(1);
                /*
                 * If it's a team, append the institution of the team
                 */
                if (table.equals("teams")) {
                    entry += " (" + getInstitution(entry) + ")";
                }
                model.add(index, entry);
                ++index;
            }
            rs.close();
            statement.close();
            sql.close();
        } catch (SQLException e) {
            LOG.error("Unable to update overview frame", e);
        }
    }
    
    private String getInstitution(final String teamName) {
        String query = null;
        String returnValue = null;
        try {
            Connection sql = TabServer.getConnectionPool().getConnection();
            /*
             * Get the speakers on the team
             */
            PreparedStatement teamStatement = sql
                            .prepareStatement("select speaker1, speaker2 from teams where teams.name = ?;");
            teamStatement.setString(1, teamName);
            ResultSet rs = teamStatement.executeQuery();
            rs.next();
            String speaker1 = rs.getString("SPEAKER1");
            String speaker2 = rs.getString("SPEAKER2");
            /*
             * Get the institution of speaker1
             */
            query = "select (institution) from speakers where speakers.name = '"
                            + speaker1 + "'";
            Statement instStatement = sql.createStatement();
            rs = instStatement.executeQuery(query);
            rs.next();
            String inst1 = rs.getString("INSTITUTION");
            rs.close();
            
            /*
             * Get the institution of speaker2
             */
            query = "select (institution) from speakers where speakers.name = '"
                            + speaker2 + "'";
            Statement statement = sql.createStatement();
            rs = statement.executeQuery(query);
            rs.next();
            String inst2 = rs.getString("INSTITUTION");
            rs.close();
            
            /*
             * Compare them
             */
            if (!inst1.equals(inst2)) {
                returnValue = "Mixed";
            } else {
                returnValue = inst1;
            }
            
        } catch (SQLException e) {
            LOG.error("Unable to find what institution two speakers are from.",
                            e);
        }
        
        return returnValue;
    }
    
    public void update(Observable o, Object arg) {
        LOG.info("Updating view");
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
}
