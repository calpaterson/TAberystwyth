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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import taberystwyth.db.TabServer;

public final class XHTMLFactory {
    
    private HashMap<String, String> substitutionMap = new HashMap<String, String>();
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
        
        // insert round
        substitutionMap.put("<!--ROUND-->", Integer.toString(round));
        
        // insert motion
        query = "select \"text\" from motions where \"round\" = " + round
                        + ";";
        statement = conn.createStatement();
        rs = statement.executeQuery(query);
        rs.next();
        substitutionMap.put("<!--MOTION-->", rs.getString(1));
        rs.close();
        statement.close();
        
        // insert the tournament name
        query = "select \"name\" from tournament_name";
        statement = conn.createStatement();
        rs = statement.executeQuery(query);
        rs.next();
        substitutionMap.put("<!--TOURNAMENT_NAME-->", rs.getString(1));
        rs.close();
        statement.close();
        
        LOG.info("Finished inserting the three general substitutions "
                        + "into the subtitution map");
        
        /*
         * Find the three templates that are global to the round
         */
        ArrayList<File> globalTemplates = new ArrayList<File>(3);
        final String titleTemplatePath = "/draw-title-slide.html";
        final String motionTemplatePath = "/motion-slide.html";
        final InputStream titleTemplateStream = this.getClass()
                        .getResourceAsStream(titleTemplatePath);
        final InputStream motionTemplateStream = this.getClass()
                        .getResourceAsStream(motionTemplatePath);
        
        /*
         * Build the title and motion slides, these can be done with only the
         * previous three substitution substitutions
         */
        final File titleFile = new File(root.getAbsolutePath()
                        + System.getProperty("file.separator")
                        + "title-slide.html");
        titleFile.createNewFile();
        writeWithSubstitutions(titleTemplateStream, titleFile);
        LOG.info("Wrote title slide");
        
        final File motionFile = new File(root.getAbsolutePath()
                        + System.getProperty("file.separator")
                        + "motion.html");
        motionFile.createNewFile();
        writeWithSubstitutions(motionTemplateStream, motionFile);
        LOG.info("Wrote motion slide");
        
        return root;
    }
    
    private String getFullTabTable() {
        // TODO Auto-generated method stub
        return null;
    }
    
    private void writeWithSubstitutions(InputStream templateStream,
                    File outputFile) throws IOException {
        /*
         * Create the output file and it's writers
         */
        FileWriter fr = new FileWriter(outputFile);
        BufferedWriter br = new BufferedWriter(fr);
        
        /*
         * Write the template out to the output file with the substitutions
         */
        br.write(applySubstitutionMap(templateStream));
        
        templateStream.close();
        br.close();
        fr.close();
    }
    
    private String applySubstitutionMap(InputStream input)
                    throws IOException {
        StringBuilder buffer = new StringBuilder(1000);
        int i = input.read();
        char current;
        do {
            current = (char) i;
            buffer.append(current);
            i = input.read();
        } while (i != -1);
        return applySubstitutionMap(buffer.toString());
    }
    
    private String applySubstitutionMap(String input) {
        String output = input;
        for (Entry<String, String> e : substitutionMap.entrySet()) {
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
