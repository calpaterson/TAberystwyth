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

package taberystwyth.view;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import taberystwyth.controller.LocationInsertionFrameListener;

public class LocationInsertionFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	/*
	 * Textfields
	 */
	private JTextField locationName = new JTextField(20);
	private JLabel locationNameLabel = new JLabel("Name:");
	private JTextField rating = new JTextField(2);
	private JLabel ratingLabel = new JLabel("Rating:");

	/*
	 * Buttons
	 */
	private JButton clear = new JButton("Clear");
	private JButton save = new JButton("Save");

	public synchronized JTextField getLocationName() {
		return locationName;
	}

	public synchronized JTextField getRating() {
		return rating;
	}

	private static LocationInsertionFrame instance = new LocationInsertionFrame();

	public static LocationInsertionFrame getInstance() {
		return instance;
	}

	private LocationInsertionFrame() {;
		setLayout(new MigLayout("wrap 2", "[left]rel[right]"));
		setTitle("Insert Location");

		/*
		 * When the window is closed, make it invisible
		 */
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				LocationInsertionFrame.getInstance().setVisible(false);
			}
		});

		add(locationNameLabel);
		add(locationName,"span");
		add(ratingLabel);
		add(rating);

		add(clear);
		add(save);

		LocationInsertionFrameListener listener = 
			new LocationInsertionFrameListener(this);
		
		clear.addActionListener(listener);
		save.addActionListener(listener);

		pack();
		setResizable(false);
		setLocationRelativeTo(OverviewFrame.getInstance());
	}

}
