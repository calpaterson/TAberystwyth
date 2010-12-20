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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import taberystwyth.db.SQLConnection;

public class ViewRoundFrame extends JFrame {
	
	JLabel roundLabel = new JLabel("Round:");
	JLabel motionLabel = new JLabel("Motion:");
	
	JComboBox rounds = new JComboBox();
	
	JTextField motion = new JTextField();
	
	JButton clear = new JButton("Clear");
	JButton view = new JButton("View");
	
	Vector<JLabel> roundOptions = new Vector<JLabel>();
	

	public ViewRoundFrame(){
		setVisible(true);
		setTitle("View Rounds");
		
		setLayout(new GridLayout(3,2));
		
		
		add(roundLabel);
		add(rounds);
		
		addRounds();
		
		add(motionLabel);
		add(motion);
		
		add(clear);
		add(view);
		
		pack();
	}


	private void addRounds() {
		SQLConnection db = SQLConnection.getInstance();
		ResultSet rs = db.executeQuery("select distinct round from room;");
		
		try {
			while(rs.next()){
				rounds.addItem(rs.getString("round"));
			}
		} catch (SQLException e) {
			db.panic(e,"Unable to select roundnumbers");
		}
		

	}
}
