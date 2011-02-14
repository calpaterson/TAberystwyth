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

package taberystwyth;

import java.io.File;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import taberystwyth.controller.DebugMenuListener;
import taberystwyth.db.TabServer;
import taberystwyth.view.OverviewFrame;

/**
 * The Main class
 * 
 * @author Roberto Sarrionandia and Cal Paterson
 */
public final class Main {
    
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private static  final TabServer TABSERVER = TabServer.getInstance();
    
    private Main() {
        /* VOID */
    }
    
    /**
     * The main method
     * 
     * @param args
     *            arguments passed to the program
     */
    public static void main(final String[] args) {
        /*
         * Set up logging
         */
        BasicConfigurator.configure();
        
        /*
         * Try setting the look and feel. If it doesn't work, we just fail and
         * continue.
         */
        try {
            UIManager
                    .setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1) {
            LOG.error(
                    "Unable to set the system look and feel, will carry on with the default.",
                    e1);
        }
        
        /*
         * Run a "simple" dialog locating the current tab or creating a new one
         */
        boolean problem = true;
        while (problem == true) {
            Object[] options = { "Create a new tab", "Open an existing tab",
                    "Close this program" };
            int n = JOptionPane.showOptionDialog(null,
                    "Create a new tab or open an existing one?",
                    "TAberystwyth", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (n == 0) {
                /*
                 * Create a new tab
                 */
                problem = false;
                JFileChooser jfc = new JFileChooser();
                jfc.setFileFilter(new FileNameExtensionFilter("Tab files",
                        "tab"));
                jfc.showDialog(null, "Create");
                File selection = jfc.getSelectedFile();
                try {
                    TABSERVER.createDatabase(selection);
                } catch (Exception e) {
                    String error = "Unable to create selected tab";
                    LOG.error(error, e);
                    JOptionPane.showMessageDialog(null,
                            error, "File Error",
                            JOptionPane.ERROR_MESSAGE);
                    problem = true;
                }
            } else if (n == 1) {
                /*
                 * Open an existing tab
                 */
                problem = false;
                JFileChooser jfc = new JFileChooser();
                jfc.setFileFilter(new FileNameExtensionFilter("Tab files",
                        "tab"));
                jfc.showDialog(null, "Open");
                File selection = jfc.getSelectedFile();
                try {
                    TABSERVER.openDatabase(selection);
                } catch (Exception e) {
                    String error = "Unable to open chosen tab";
                    LOG.error(error, e);
                    JOptionPane.showMessageDialog(null,
                            error, "File Error",
                            JOptionPane.ERROR_MESSAGE);
                    problem = true;
                }
            } else if (n == 2) {
                LOG.fatal("User closed program");
                System.exit(0);
            }
        }
        
        try {
            TABSERVER.getConnectionPool().getConnection();
        } catch (SQLException e1) {
            LOG.error("something", e1);
        }
        
        System.exit(1);
        
        try {
            /*
             * Class.forName("taberystwyth.db.SQLConnection");
             * Class.forName("taberystwyth.allocation.Allocator");
             * Class.forName("taberystwyth.view.OverviewFrame");
             */
        } catch (Exception e) {
            LOG.fatal("Unable to load the singleton classes.", e);
            return;
        }
        
        /*
         * If the debug argument is passed, add the debug menu FIXME: Clean
         * this up so that the overviewframe menu has an option to display the
         * debug menu or not - this stuff shouldn't be here.
         */
        if (args.length > 0 && args[0].equals("--debug")) {
            LOG.info("Entering debug mode.");
            
            final JMenu debugMenu = new JMenu("Debug");
            final JMenuItem generateMorningTab = new JMenuItem("Generate Tab");
            generateMorningTab.addActionListener(new DebugMenuListener());
            debugMenu.add(generateMorningTab);
            
            OverviewFrame.getInstance().getMenu().add(debugMenu);
        }
        
    }
    
}