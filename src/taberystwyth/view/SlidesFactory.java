package taberystwyth.view;

import java.io.*;
import java.sql.ResultSet;

import taberystwyth.db.SQLConnection;

public class SlidesFactory {

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
