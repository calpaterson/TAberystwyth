package taberystwyth.view;

import java.awt.GridLayout;

import javax.swing.*;
import taberystwyth.db.SQLConnection;

public class LocationInsertionFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	SQLConnection conn = SQLConnection.getInstance();
	
	JTextField name = new JTextField();
	JLabel nameLabel = new JLabel("Name:");
	JTextField rating = new JTextField();
	JLabel ratingLabel = new JLabel("Rating:");
	
	public LocationInsertionFrame(){
		setVisible(true);
		setLayout(new GridLayout(2,2));
		setTitle("Insert Location");
		
		add(nameLabel);
		add(name);
		add(ratingLabel);
		add(rating);
		
		pack();
	}
	
}
