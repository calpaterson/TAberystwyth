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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import taberystwyth.controller.TeamFocusListener;
import taberystwyth.controller.TeamInsertionListener;
import taberystwyth.db.SQLConnection;

final public class TeamInsertionFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton clear = new JButton("Clear");

	private transient SQLConnection conn = SQLConnection.getInstance();

	private TeamInsertionListener listener = new TeamInsertionListener(this);

	private JButton save = new JButton("Save");

	private JCheckBox speaker1ESL = new JCheckBox();

	private JLabel speaker1ESLLabel = new JLabel("Esl?:");

	private JTextField speaker1Institution = new JTextField(20);

	private JLabel speaker1InstitutionLabel = new JLabel("Institution 1:");

	private JTextField speaker1Name = new JTextField(20);

	private JLabel speaker1NameLabel = new JLabel("Speaker Name 1:");

	private JCheckBox speaker1Novice = new JCheckBox();

	private JLabel speaker1NoviceLabel = new JLabel("Novice?:");

	private JCheckBox speaker2ESL = new JCheckBox();

	private JLabel speaker2ESLLabel = new JLabel("Esl?:");

	private JTextField speaker2Institution = new JTextField(20);

	private JLabel speaker2InstitutionLabel = new JLabel("Institution 2:");

	private JTextField speaker2Name = new JTextField(20);

	private JLabel speaker2NameLabel = new JLabel("Speaker Name 2:");

	private JCheckBox speaker2Novice = new JCheckBox();

	private JLabel speaker2NoviceLabel = new JLabel("Novice?:");

	private JCheckBox swing = new JCheckBox();

	private JLabel swingLabel = new JLabel("Swing?");

	private JTextField teamName = new JTextField(20);
	private JLabel teamNameLabel = new JLabel("Team Name:");
	
	private TeamFocusListener  institutionDuplicator 
		= new TeamFocusListener(speaker1Institution, speaker2Institution);
	

	private static TeamInsertionFrame instance = new TeamInsertionFrame();
	
	
	
	public static TeamInsertionFrame getInstance(){
		return instance;
	}
	
	private TeamInsertionFrame() {
		/*
		 * When the window is closed, make it invisible
		 */
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				LocationInsertionFrame.getInstance().setVisible(false);
			}
		});
		
		setLayout(new MigLayout("wrap 2", "[left]rel[right]"));
		setTitle("Insert Team");

		add(teamNameLabel);
		add(teamName, "span");
		add(swingLabel);
		add(swing);
		add(speaker1NameLabel);
		add(speaker1Name, "span");
		add(speaker1InstitutionLabel);
		add(speaker1Institution, "span");
		add(speaker1ESLLabel);
		add(speaker1ESL);
		add(speaker1NoviceLabel);
		add(speaker1Novice);
		add(speaker2NameLabel);
		add(speaker2Name, "span");
		add(speaker2InstitutionLabel);
		add(speaker2Institution, "span");
		add(speaker2ESLLabel);
		add(speaker2ESL);
		add(speaker2NoviceLabel);
		add(speaker2Novice);
		
		speaker1Institution.addFocusListener(institutionDuplicator);
		
		add(clear);
		add(save);
		pack();
		setResizable(false);
		setLocationRelativeTo(OverviewFrame.getInstance());
		clear.addActionListener(listener);
		save.addActionListener(listener);
	}

	public static synchronized long getSerialversionuid() {
		return serialVersionUID;
	}

	public synchronized JCheckBox getSpeaker1ESL() {
		return speaker1ESL;
	}

	public synchronized JTextField getSpeaker1Institution() {
		return speaker1Institution;
	}

	public synchronized JTextField getSpeaker1Name() {
		return speaker1Name;
	}

	public synchronized JCheckBox getSpeaker1Novice() {
		return speaker1Novice;
	}

	public synchronized JCheckBox getSpeaker2ESL() {
		return speaker2ESL;
	}

	public synchronized JTextField getSpeaker2Institution() {
		return speaker2Institution;
	}

	public synchronized JTextField getSpeaker2Name() {
		return speaker2Name;
	}

	public synchronized JCheckBox getSpeaker2Novice() {
		return speaker2Novice;
	}

	public synchronized JCheckBox getSwing() {
		return swing;
	}

	public synchronized JTextField getTeamName() {
		return teamName;
	}
}
