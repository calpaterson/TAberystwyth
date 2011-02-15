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

package taberystwyth.slides;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import taberystwyth.db.TabServer;

public final class XHTMLFactory {
    
    private HashMap<String, String> subs = new HashMap<String, String>();
    private static XHTMLFactory instance = new XHTMLFactory();
    private static final Logger LOG = Logger.getLogger(XHTMLFactory.class);
    
    private XHTMLFactory() {
        /* VOID */
    }
    
    public static XHTMLFactory getInstance() {
        return instance;
    }
    
    public File generateSlides() throws SQLException, IOException {
        /*
         * Create the temporary directory that all of the xhtml will be put in
         */
        File root = getTemporaryDirectory();
        LOG.debug("The temporary directory is: " + root.getAbsolutePath());
        
        // If we can, open the directory in the local explorer
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(root);
        }
        
        /*
         * Common
         */
        String query;
        Statement statement;
        ResultSet rs;
        Connection conn = TabServer.getConnectionPool().getConnection();
        
        /*
         * Get the current round
         */
        query = "select max(\"round\") from rooms;";
        statement = conn.createStatement();
        rs = statement.executeQuery(query);
        rs.next();
        int round = rs.getInt(1);
        rs.close();
        statement.close();
        LOG.debug("Current round is: " + round);
        
        /*
         * Update the substitution map
         */
        subs.clear();
        
        // insert round
        subs.put("<!--ROUND-->", Integer.toString(round));
        
        // insert motion
        query = "select \"text\" from motions where \"round\" = " + round
                        + ";";
        statement = conn.createStatement();
        rs = statement.executeQuery(query);
        rs.next();
        subs.put("<!--MOTION-->", rs.getString(1));
        rs.close();
        statement.close();
        
        // insert the tournament name
        query = "select \"name\" from tournament_name";
        statement = conn.createStatement();
        rs = statement.executeQuery(query);
        rs.next();
        subs.put("<!--TOURNAMENT_NAME-->", rs.getString(1));
        rs.close();
        statement.close();
        
        /*
         * Build the title and motion slides, these can be done with only the
         * global substitutions
         */
        writeWithSubstitutions(root, "draw-title-slide.xhtml",
                        "draw-title-slide.xhtml");
        writeWithSubstitutions(root, "motion-slide.xhtml",
                        "motion-slide.xhtml");
        
        /*
         * Build the full tab
         */
        subs.put("<!--FULL_TAB-->", getFullTabTable());
        
        /*
         * Build the match slides
         */
        query = "select \"round\", " + "\"location\", " + "\"first_prop\", "
                        + "\"first_op\", " + "\"second_prop\", "
                        + "\"second_op\" " + "from rooms";
        final int FIRST_PROP = 3;
        final int FIRST_OP = 4;
        final int SECOND_PROP = 5;
        final int SECOND_OP = 6;
        statement = conn.createStatement();
        rs = statement.executeQuery(query);
        while (rs.next()) {
            /*
             * Get the chair for this room
             */
            String chairQuery = "select (\"name\") from judging_panels where "
                            + "\"isChair\" = 1";
            Statement chairStatement = conn.createStatement();
            ResultSet chairRS = chairStatement.executeQuery(chairQuery);
            chairRS.next();
            String chair = chairRS.getString(1);
            chairRS.close();
            chairStatement.close();
            subs.put("<!--CHAIR-->", chair);
            
            /*
             * Get the wings
             */
            String wingsQuery = "select (\"name\") from judging_panels where "
                            + "\"isChair\" = 0 order by random()";
            Statement wingsStatement = conn.createStatement();
            ResultSet wingsRS = wingsStatement.executeQuery(wingsQuery);
            wingsRS.next();
            String wings = wingsRS.getString(1);
            while (wingsRS.next()) {
                wings += ", ";
                wings += wingsRS.getString(1);
            }
            wingsStatement.close();
            subs.put("<!--WINGS-->", wings);
            
            /*
             * Get the teams
             */
            subs.put("<!--FIRST_PROP-->", rs.getString(FIRST_PROP));
            subs.put("<!--FIRST_OP-->", rs.getString(FIRST_OP));
            subs.put("<!--SECOND_PROP-->", rs.getString(SECOND_PROP));
            subs.put("<!--SECOND_OP-->", rs.getString(SECOND_OP));
            
            // FIXME: There should be some deterministic ordering of slides
            writeWithSubstitutions(root, "motion-slide.xhtml", round + "-"
                            + chair + ".xhtml");
        }
        
        /*
         * Build the scroller
         */
        // FIXME: no idea
        
        rs.close();
        statement.close();
        conn.close();
        return root;
    }
    
    private String getFullTabTable() {
        // TODO Auto-generated method stub
        return null;
    }
    
    private void writeWithSubstitutions(File root, String templateName,
                    String outputName) throws IOException {
        File titlePage = new File(root, outputName);
        if (!titlePage.createNewFile()) {
            throw new IOException("Was not able to create " + templateName);
        }
        FileWriter fr = new FileWriter(titlePage);
        BufferedWriter br = new BufferedWriter(fr);
        InputStream template = this.getClass().getResourceAsStream(
                        templateName);
        br.write(applySubstitutionMap(template));
        br.close();
        fr.close();
    }
    
    private String applySubstitutionMap(InputStream input) {
        String output = input.toString();
        return applySubstitutionMap(output);
    }
    
    private String applySubstitutionMap(String input) {
        String output = input;
        for (Entry<String, String> e : subs.entrySet()) {
            output = output.replace(e.getKey(), e.getValue());
        }
        return output;
    }
    
    private File getTemporaryDirectory() throws IOException {
        /*
         * The name of the directory
         */
        String basePath = System.getProperty("java.io.tmpdir");
        String directoryName = "taberystwyth-" + System.currentTimeMillis();
        
        /*
         * If the directory cannot be made, bomb.
         */
        File temporaryDirectory = new File(basePath, directoryName);
        if (!temporaryDirectory.mkdir()) {
            throw new IOException("Was not able to create the temporary"
                            + "directory "
                            + temporaryDirectory.getAbsolutePath());
        }
        
        return temporaryDirectory;
    }
    
}
