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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import taberystwyth.controller.DebugMenuListener;
import taberystwyth.db.TabServer;
import taberystwyth.view.CreationFrame;
import taberystwyth.view.OpenFrame;
import taberystwyth.view.OverviewFrame;

/**
 * The Main class
 * 
 * @author Roberto Sarrionandia and Cal Paterson
 */
public final class Main {
    
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private static final TabServer TABSERVER = TabServer.getInstance();
    
    private Main() {
        /* VOID */
    }
    
    private static String[] args;
    
    /**
     * The main method
     * 
     * @param args
     *            arguments passed to the program
     */
    public static void main(final String[] args) {
        /*
         * Try setting the look and feel. If it doesn't work, we just fail and
         * continue.
         */
        try {
            UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
        } catch (Exception e1) {
            LOG.error("Unable to set the system look and feel, will carry on with the default.",
                            e1);
        }
        
        Main.args = args;
        
        displayIntroDialog(null);
    }
    
    public static void displayIntroDialog(JFrame that) {
        /*
         * Run a "simple" dialog locating the current tab or creating a new one
         */
        boolean problem = true;
        while (problem == true) {
            Object[] options = { "Create a new tab", "Open an existing tab",
                    "Close this program" };
            int n = JOptionPane.showOptionDialog(null,
                            "Create a new tab or open an existing one?",
                            "TAberystwyth",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options,
                            options[1]);
            if (n == 0) {
                /*
                 * Show the frame for creation of a new tab
                 */
                problem = false;
                CreationFrame.getInstance().setVisible(true);
            } else if (n == 1) {
                /*
                 * Show an open dialog box
                 */
                problem = false;
                OpenFrame.getInstance().setVisible(true);
                /*
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
                    JOptionPane.showMessageDialog(null, error, "File Error",
                                    JOptionPane.ERROR_MESSAGE);
                    problem = true;
                }
                */
            } else if (n == 2) {
                LOG.fatal("User closed program");
                System.exit(0);
            }
        }
        
        try {
            Class.forName("taberystwyth.view.OverviewFrame");
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
            OverviewFrame.getInstance().setDebug(true);
        } else {
            OverviewFrame.getInstance().setDebug(false);
        }
        
    }
    
}