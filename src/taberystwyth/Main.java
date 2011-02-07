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

import javax.swing.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import taberystwyth.controller.DebugMenuListener;
import taberystwyth.view.OverviewFrame;

/**
 * The Main class
 * 
 * @author Roberto Sarrionandia and Cal Paterson
 */
public final class Main {
    
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private Main(){
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
        
        try {
            
            Class.forName("taberystwyth.db.SQLConnection");
            Class.forName("taberystwyth.allocation.Allocator");
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
                
                final JMenu debugMenu = new JMenu("Debug");
                final JMenuItem generateMorningTab = new JMenuItem(
                        "Generate Tab");
                generateMorningTab.addActionListener(new DebugMenuListener());
                debugMenu.add(generateMorningTab);
                
                OverviewFrame.getInstance().getMenu().add(debugMenu);
        }
    }
    
}