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

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;

import taberystwyth.db.SQLConnection;

public final class XHTMLFactory {
    
    private HashMap<String, String> subs = 
        new HashMap<String, String>();
    private static XHTMLFactory instance = new XHTMLFactory();
    
    private XHTMLFactory(){
        /* VOID */
    }
    
    public XHTMLFactory getInstance(){
        return instance;
    }
    
    public File generateSlides() throws SQLException, IOException{
        /*
         * Create the temporary directory that all of the xhtml will be put in
         */
        File root = getTemporaryDirectory();
        
        /*
         * Common
         */
        String query;
        ResultSet rs;     
        SQLConnection sql = SQLConnection.getInstance();
        
        /*
         * Get the current round
         */
        query = "select max(round) from rooms;";
        rs = SQLConnection.getInstance().executeQuery(query);
        int round = rs.getInt(1);
        
        /*
         * Update the substitution map
         */
        subs.clear();
        
        subs.put("<!--ROUND-->", Integer.toString(round));
                
        query = "select text from motion where round = " + round + ";";
        rs = sql.executeQuery(query);
        rs.next();
        subs.put("<!--MOTION-->", rs.getString(1));
        
        query = "select name from tournament_name";
        rs = sql.executeQuery(query);
        rs.next();
        subs.put("<!--TOURNAMENT_NAME-->", rs.getString(1));
        
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
        query = "select round, location, first_prop, first_op, second_prop" +
            "second_op from rooms";
        final int FIRST_PROP = 3;
        final int FIRST_OP = 4;
        final int SECOND_PROP = 5;
        final int SECOND_OP = 6;
        rs = sql.executeQuery(query);
        while(rs.next()){         
            /*
             * Get the chair for this room
             */
            String chairQuery = "select (name) from judging_panels where " +
                "isChair = 1";
            ResultSet chairRS = sql.executeQuery(chairQuery);
            chairRS.next();
            String chair = chairRS.getString(1);
            subs.put("<!--CHAIR-->", chair);
            
            /*
             * Get the wings
             */
            String wingsQuery = "select (name) from judging_panels where " +
                "isChair = 0 order by random()";
            ResultSet wingsRS = sql.executeQuery(wingsQuery);
            wingsRS.next();
            String wings = wingsRS.getString(1);
            while(wingsRS.next()){
                wings += ", ";
                wings += wingsRS.getString(1);
            }
            subs.put("<!--WINGS-->", wings);
            
            /*
             * Get the teams
             */
            subs.put("<!--FIRST_PROP-->", rs.getString(FIRST_PROP));
            subs.put("<!--FIRST_OP-->", rs.getString(FIRST_OP));
            subs.put("<!--SECOND_PROP-->", rs.getString(SECOND_PROP));
            subs.put("<!--SECOND_OP-->", rs.getString(SECOND_OP));
            
            // FIXME: There should be some deterministic ordering of slides
            writeWithSubstitutions(root, "motion-slide.xhtml",
                    round + "-" + chair + ".xhtml");
        }
        
        /*
         * Build the scroller
         */
        // FIXME: no idea
        
        return null;
    }
    
    private String getFullTabTable() {
        // TODO Auto-generated method stub
        return null;
    }

    private void writeWithSubstitutions(File root, String templateName,
            String outputName) 
        throws IOException{
        File titlePage = new File(root, outputName);
        if(!titlePage.createNewFile()){
            throw new IOException("Was not able to create " + templateName);
        }
        FileWriter fr = new FileWriter(titlePage);
        BufferedWriter br = new BufferedWriter(fr);
        InputStream template = 
            this.getClass().getResourceAsStream(templateName);
        br.write(applySubstitutionMap(template));
        br.close();
        fr.close();
    }
    
    private String applySubstitutionMap(InputStream input){
        String output = input.toString();
        return applySubstitutionMap(output);
    }
    
    private String applySubstitutionMap(String input){
        String output = input;
        for (Entry<String,String> e: subs.entrySet()){
            output = output.replace(e.getKey(), e.getValue());
        }
        return output;
    }
    
    private File getTemporaryDirectory() throws IOException{
        /*
         * The name of the directory
         */
        String basePath = System.getProperty("java.io.tmpdir");
        String directoryName = "taberystwyth-" + System.currentTimeMillis();
        
        /*
         * If the directory cannot be made, bomb.
         */
        File temporaryDirectory = new File(basePath, directoryName);
        if(!temporaryDirectory.mkdir()){
            throw new IOException("Was not able to create the temporary" +
                    "directory " +  temporaryDirectory.getAbsolutePath());
        }
        
        return temporaryDirectory;
    }
    
/*    public static File generateSlides(int round) throws Exception {
        
        File output = null;
        
        FileInputStream fStream = new FileInputStream("html/slide.html");
        DataInputStream dStream = new DataInputStream(fStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                dStream));
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("1.html"));
        
        String chair = null, judges = null, location = null, fprop = null, fop = null, sop = null, sprop = null;
        
        chair = "Mike Keary";
        judges = "Tom Coyle, Maya";
        location = "A14";
        fprop = "ABER A";
        fop = "ABER HAMZA";
        sprop = "ABER D";
        sop = "SOAS A";
        
        String line = reader.readLine();
        while (line != null) {
            
            
             * Replace tags with actual information
             
            line = line.replace("<!--CHAIR-->", chair);
            line = line.replace("<!--JUDGES-->", judges);
            line = line.replace("<!--ROOM-->", location);
            line = line.replace("<!--1PROP-->", fprop);
            line = line.replace("<!--1OP-->", fop);
            line = line.replace("<!--2PROP-->", sprop);
            line = line.replace("<!--2OP-->", sop);
            
            writer.write(line);
            
            line = reader.readLine();
        }
        
        dStream.close();
        writer.flush();
        writer.close();
        
        return output;
    }*/
    
}
