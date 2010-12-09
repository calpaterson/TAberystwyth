package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import taberystwyth.db.SQLConnection;
import taberystwyth.view.LocationInsertionFrame;
import taberystwyth.view.OverviewFrame;

public class LocationInsertionFrameListener implements ActionListener {

	private LocationInsertionFrame frame;
	private SQLConnection conn = SQLConnection.getInstance();

	public LocationInsertionFrameListener(LocationInsertionFrame frame) {
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Save")) {
			String statement = "insert into location (name, location) values (" + 
							   "\"" +
							   frame.getLocationName().getText() + 
							   "\", \"" + 
							   frame.getRating().getText() +
							   "\");";
			conn.execute(statement);
		}
		if (e.getActionCommand().equals("Clear")){
			frame.getLocationName().setText("");
			frame.getRating().setText("");
		}
		try {
			OverviewFrame.getInstance().refreshLocation();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
