package taberystwyth.view;

import java.awt.GridLayout;

import javax.swing.*;
import taberystwyth.db.SQLConnection;

public class LocationInsertionFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	SQLConnection conn = SQLConnection.getInstance();
	
	/*
	 * Textfields
	 */
	JTextField name = new JTextField();
	JLabel nameLabel = new JLabel("Name:");
	JTextField rating = new JTextField();
	JLabel ratingLabel = new JLabel("Rating:");
	
	/*
	 * Buttons
	 */
	JButton clear = new JButton("Clear");
	JButton save = new JButton("Save");
	
	public LocationInsertionFrame(){
		setVisible(true);
		setLayout(new GridLayout(3,2));
		setTitle("Insert Location");
		
		add(nameLabel);
		add(name);
		add(ratingLabel);
		add(rating);
		
		add(clear);
		add(save);
		
		clear.addActionListener(new LocationInsertionFrameListener(this));
		save.addActionListener(new LocationInsertionFrameListener(this));
		
		pack();
	}
	
}
