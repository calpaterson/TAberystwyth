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

package taberystwyth.db;

import java.sql.SQLException;
import java.util.Random;

public class RandomTabGenerator {
    SQLConnection sql = SQLConnection.getInstance();
    
    private static RandomTabGenerator instance = new RandomTabGenerator();
    
    public static RandomTabGenerator getInstance() {
        return instance;
    }
    
    private Random gen = new Random();
    
    private final int N_TEAMS = 100;
    
    private RandomTabGenerator() {
        /* VOID */
    }
    
    public void generate() {
        synchronized (sql) {
            sql.setChangeTracking(false);
            int i = 0;
            while (i < N_TEAMS) {
                genTeam();
                ++i;
            }
            sql.setChangeTracking(true);
        }
    }
    
    private String genTeam() {
        String speaker1;
        String speaker2;
        while (true) {
            try {
                speaker1 = genSpeaker();
                speaker2 = genSpeaker();
                String name = speaker1 + " and " + speaker2;
                sql.execute("insert into teams (name, speaker1, speaker2) values("
                        + "'"
                        + name
                        + "', '"
                        + speaker1
                        + "', '"
                        + speaker2
                        + "');");
                return name;
            } catch (SQLException e) {
                // FIXME: remove failed inserts
            }
        }
    }
    
    private String genSpeaker() throws SQLException {
        String name = genName();
        String institution = institutions[gen.nextInt(institutions.length)];
        sql.execute("insert into speakers (name, institution) values(" + "'"
                + name + "','" + institution + "');");
        return name;
    }
    
    private String genName() {
        return firstNames[gen.nextInt(firstNames.length)] + " "
                + gen.nextInt(25);
    }
    
    private String[] institutions = {
            "University of Aberdeen",
            "University of Abertay Dundee",
            "Aberystwyth University",
            "Anglia Ruskin University",
            "University of the Arts London",
            "Aston University, Birmingham",
            "Bangor University",
            "University of Bath",
            "Bath Spa University",
            "University of Bedfordshire",
            "University of Birmingham",
            "Birmingham City University",
            "University Centre At Blackburn College, Blackburn",
            "University of Bolton",
            "Bournemouth University",
            "University of Bradford",
            "University of Brighton",
            "University of Bristol",
            "Brunel University",
            "University of Buckingham",
            "Buckinghamshire New University, High Wycombe",
            "University of Cambridge",
            "Canterbury Christ Church University",
            "Cardiff University",
            "University of Central Lancashire, Preston",
            "University of Chester, Chester and Warrington",
            "University of Chichester",
            "City University London",
            "Coventry University",
            "Cranfield University",
            "University of Cumbria",
            "De Montfort University, Leicester",
            "University of Derby",
            "University of Dundee",
            "Durham University",
            "University of East Anglia, Norwich",
            "University of East London",
            "Edge Hill University, Ormskirk",
            "University of Edinburgh",
            "Edinburgh Napier University",
            "University of Essex",
            "University of Exeter",
            "University of Glamorgan",
            "University of Glasgow",
            "Glasgow Caledonian University",
            "University of Gloucestershire",
            "University of Greenwich",
            "Glyndwr University, Wrexham",
            "Heriot-Watt University",
            "University of Hertfordshire",
            "University of Huddersfield",
            "University of Hull",
            "Hull York Medical School (HYMS)",
            "Imperial College London",
            "Keele University, Staffordshire",
            "University of Kent, Canterbury and Medway",
            "Kingston University",
            "Lancaster University",
            "University of Leeds",
            "Leeds Metropolitan University",
            "University of Leicester",
            "University of Lincoln",
            "University of Liverpool",
            "Liverpool Hope University",
            "Liverpool John Moores University",
            "University of London",
            "London Metropolitan University",
            "London South Bank University",
            "Loughborough University",
            "University of Manchester",
            "Manchester Metropolitan University",
            "Middlesex University, London",
            "Newcastle University",
            "University of Northampton",
            "Northumbria University, Newcastle upon Tyne",
            "University of Nottingham",
            "Nottingham Trent University",
            "The Open University",
            "University of Oxford",
            "Oxford Brookes University",
            "Peninsula College of Medicine and Dentistry",
            "University of Plymouth",
            "University of Portsmouth",
            // "Queen's University Belfast", // FIXME: Apostrophes!
            "Queen Margaret University, Edinburgh",
            "University of Reading",
            "The Robert Gordon University, Aberdeen",
            "Roehampton University, London",
            "Royal College of Art, London",
            "University of St Andrews",
            "University of Salford",
            "University of Sheffield",
            "Sheffield Hallam University",
            "University of Southampton",
            "Southampton Solent University",
            "Staffordshire University",
            "University of Stirling",
            "University of Strathclyde",
            "University of Sunderland",
            "University of Surrey, Guildford",
            "University of Sussex, Falmer and Brighton",
            "Swansea Metropolitan University",
            "Swansea University",
            "University of Teesside, Middlesbrough",
            "Thames Valley University",
            "University of Ulster",
            "University of Wales",
            "University of Wales Institute, Cardiff (UWIC)",
            "University of Wales, Newport",
            "University of Wales, Trinity Saint David",
            "University of Warwick, Coventry",
            "University of Westminster, London",
            "University of the West of England, Bristol",
            "University of the West of Scotland, Ayr, Hamilton, Dumfries and Paisley",
            "University of Winchester", "University of Wolverhampton",
            "University of Worcester", "University of York",
            "York St John University" };
    
    private String[] firstNames = { "Abigail", "Alexander", "Amelia", "Amy",
            "Benjamin", "Callum", "Charlie", "Charlotte", "Chloe", "Daniel",
            "Ella", "Ellie", "Emily", "Emma", "Ethan", "George", "Grace",
            "Hannah", "Harry", "Jack", "James", "Jessica", "Joseph", "Joshua",
            "Katie", "Lewis", "Lily", "Lucy", "Luke", "Matthew", "Megan",
            "Mia", "Mohammed", "Molly", "Oliver", "Olivia", "Samuel",
            "Sophie", "Thomas", "William" };
    
    String[] locations = {
            "Bath",
            "Blaenavon Industrial Landscape",
            "Blenheim Palace",
            "Canterbury Cathedral, St. Augustines Abbey and St. Martins Church",
            "Castles and Town Walls of King Edward I in Gwynedd",
            "Cornwall and West Devon Mining Landscape",
            "Derwent Valley Mills", "Durham Castle and Cathedral",
            "Edinburgh Old Town and New Town",
            "Frontiers of the Roman Empire (Antonine Wall)",
            "Frontiers of the Roman Empire (Hadrians Wall)",
            "Giants Causeway", "Heart of Neolithic Orkney",
            "Ironbridge Gorge", "Jurassic Coast", "Kew Gardens",
            "Liverpool Maritime Mercantile City", "Maritime Greenwich",
            "New Lanark", "Pontcysyllte Aqueduct", "Saltaire", "St. Kilda",
            "St. Margarets Church",
            "Stonehenge, Avebury and Associated Sites",
            "Studley Royal Park and Fountains Abbey", "Tower of London",
            "Westminster Palace, Westminster Abbey" };
    
}
