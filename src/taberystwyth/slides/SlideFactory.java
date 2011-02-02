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
import taberystwyth.db.SQLConnection;

public class SlideFactory {
    
    public static File generateSlides(int round) throws Exception {
        
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
            
            /*
             * Replace tags with actual information
             */
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
    }
    
}
