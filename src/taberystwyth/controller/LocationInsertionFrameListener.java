/* This file is part of TAberystwyth, a debating competition organiser
 * Copyright (C) 2010, Roberto Sarrionandia and Cal Paterson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import taberystwyth.db.SQLConnection;
import taberystwyth.view.LocationInsertionFrame;
import taberystwyth.view.OverviewFrame;

public class LocationInsertionFrameListener implements ActionListener {

	private LocationInsertionFrame frame;

	public LocationInsertionFrameListener(LocationInsertionFrame frame){
		this.frame = frame;
	}
	private SQLConnection conn = SQLConnection.getInstance();

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Save")) {
			String statement = "insert into locations (name, rating) values (" + 
							   "\"" +
							   frame.getLocationName().getText() + 
							   "\", \"" + 
							   frame.getRating().getText() +
							   "\");";
			conn.execute(statement);
			frame.getLocationName().setText("");
			frame.getRating().setText("");
		}
		if (e.getActionCommand().equals("Clear")){
			frame.getLocationName().setText("");
			frame.getRating().setText("");
		}
	}

}
