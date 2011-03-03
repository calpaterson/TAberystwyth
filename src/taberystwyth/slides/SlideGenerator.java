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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import taberystwyth.db.TabServer;

public final class SlideGenerator {
    
    private HashMap<String, String> substitutionMap = new HashMap<String, String>();
    private static SlideGenerator instance = new SlideGenerator();
    private static final Logger LOG = Logger.getLogger(SlideGenerator.class);
    
    private SlideGenerator() {
        /* VOID */
    }
    
    public static SlideGenerator getInstance() {
        return instance;
    }
    
    public File generateSlides() throws SQLException, IOException {
        /*
         * Create the temporary directory that all of the html will be put in
         */
        File root = getTemporaryDirectory();
        LOG.debug("The temporary directory is: " + root.getAbsolutePath());
        
        /*
         * Common
         */
        String query;
        Statement statement;
        PreparedStatement prep1, prep2;
        ResultSet rs1, rs2;
        Connection conn = TabServer.getConnectionPool().getConnection();
        
        /*
         * Get the current round
         */
        query = "select max(\"round\") from rooms;";
        statement = conn.createStatement();
        rs1 = statement.executeQuery(query);
        rs1.next();
        int round = rs1.getInt(1);
        rs1.close();
        statement.close();
        LOG.debug("Current round is: " + round);
        
        // insert round
        substitutionMap.put("<!--ROUND-->", Integer.toString(round));
        
        // insert motion
        query = "select \"text\" from motions where \"round\" = " + round
                        + ";";
        statement = conn.createStatement();
        rs1 = statement.executeQuery(query);
        rs1.next();
        substitutionMap.put("<!--MOTION-->", rs1.getString(1));
        rs1.close();
        statement.close();
        
        // insert the tournament name
        query = "select \"name\" from tournament_name";
        statement = conn.createStatement();
        rs1 = statement.executeQuery(query);
        rs1.next();
        substitutionMap.put("<!--TOURNAMENT_NAME-->", rs1.getString(1));
        rs1.close();
        statement.close();
        
        LOG.info("Finished inserting the three general substitutions "
                        + "into the subtitution map");
        
        /*
         * Find the three templates that are global to the round
         */
        ArrayList<File> globalTemplates = new ArrayList<File>(3);
        final String titleTemplatePath = "/title-slide.html";
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
        
        /*
         * Build the match and ballot slides
         * 
         * Loop over the rooms that have been generated for this round
         */
        query = "select \"first_prop\", \"first_op\", \"second_prop\", "
                        + "\"second_op\", \"location\" from rooms where "
                        + "\"round\" = ?;";
        prep1 = conn.prepareStatement(query);
        prep1.setInt(1, round);
        rs1 = prep1.executeQuery();
        int slideNumber = 0;
        while (rs1.next()) {
            /*
             * Put all of the stuff from the rooms table into the substitution
             * map
             */
            String room = rs1.getString(5);
            substitutionMap.put("<!--FIRST_PROP-->", rs1.getString(1));
            substitutionMap.put("<!--FIRST_OP-->", rs1.getString(2));
            substitutionMap.put("<!--SECOND_PROP-->", rs1.getString(3));
            substitutionMap.put("<!--SECOND_OP-->", rs1.getString(4));
            substitutionMap.put("<!--LOCATION-->", rs1.getString(5));
            
            /*
             * Pull out the judges for the current room
             */
            String innerQuery = "select \"name\", \"isChair\" from "
                            + "judging_panels where \"room\" = ? "
                            + "and \"round\" = ?";
            prep2 = conn.prepareStatement(innerQuery);
            prep2.setString(1, room);
            prep2.setInt(2, round);
            rs2 = prep2.executeQuery();
            while (rs2.next()) {
                ArrayList<String> wingsArray = new ArrayList<String>();
                /*
                 * If he's a chair, put him in the substitution map
                 */
                if (rs2.getBoolean(2)) {
                    substitutionMap.put("<!--CHAIR-->", rs2.getString(1));
                } else {
                    wingsArray.add(rs2.getString(1));
                }
                /*
                 * Put all the wings together in commas (there might be no
                 * wings)
                 */
                if (wingsArray.size() > 0) {
                    String wings = "";
                    wings += wingsArray.get(0);
                    for (int i = 1; i < wingsArray.size(); i++) {
                        wings += ", " + wingsArray.get(i);
                    }
                    substitutionMap.put("<!--WINGS-->", wings);
                }
            }
            
            final File matchFile = new File(root.getAbsolutePath()
                            + System.getProperty("file.separator")
                            + "match-" + slideNumber + ".html");
            titleFile.createNewFile();
            final InputStream matchTemplateStream = this.getClass()
                            .getResourceAsStream("/match-slide.html");
            writeWithSubstitutions(matchTemplateStream, matchFile);
            LOG.info("Wrote match slide " + slideNumber);
            
            final File ballotFile = new File(root.getAbsolutePath()
                            + System.getProperty("file.separator")
                            + "ballot-" + slideNumber + ".html");
            titleFile.createNewFile();
            final InputStream ballotTemplateStream = this.getClass()
                            .getResourceAsStream("/ballot.html");
            writeWithSubstitutions(ballotTemplateStream, ballotFile);
            LOG.info("Wrote ballot slide " + slideNumber);
            
            
            ++slideNumber;
        }
        prep1.close();
        rs1.close();
        statement.close();
        
        // If we can, open the directory in the local explorer
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(root);
        } else {
            // FIXME: Do something else
        }
        
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
