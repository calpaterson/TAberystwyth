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

package taberystwyth.view;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import taberystwyth.db.TabServer;

/**
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 * @author Cal Paterson
 * 
 */
public class ViewRoundFrame extends JFrame {

	JLabel roundLabel = new JLabel("Round:");
	JLabel motionLabel = new JLabel("Motion:");

	JComboBox rounds = new JComboBox();

	JTextField motion = new JTextField();

	JButton clear = new JButton("Clear");
	JButton view = new JButton("View");

	Vector<JLabel> roundOptions = new Vector<JLabel>();

	Logger LOG = Logger.getLogger(ViewRoundFrame.class);

	public ViewRoundFrame() {
		setVisible(true);
		setTitle("View Rounds");

		setLayout(new GridLayout(3, 2));

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

		try {
			Connection sql = TabServer.getConnectionPool().getConnection();
			PreparedStatement stmt = sql
					.prepareStatement("select distinct round from room;");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				rounds.addItem(rs.getString("round"));
			}

		} catch (SQLException e) {
			LOG.error("Unable to select roundnumbers", e);
		}

	}
}
