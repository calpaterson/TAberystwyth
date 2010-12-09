package taberystwyth.view;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import taberystwyth.controller.TeamInsertionListener;
import taberystwyth.db.SQLConnection;

public class TeamInsertionFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton clear = new JButton("Clear");

	private SQLConnection conn = SQLConnection.getInstance();

	private TeamInsertionListener listener = new TeamInsertionListener(this);

	private JButton save = new JButton("Save");

	private JCheckBox speaker1ESL = new JCheckBox();

	private JLabel speaker1ESLLabel = new JLabel("Esl?:");

	private JTextField speaker1Institution = new JTextField();

	private JLabel speaker1InstitutionLabel = new JLabel("Institution 1:");

	private JTextField speaker1Name = new JTextField();

	private JLabel speaker1NameLabel = new JLabel("Speaker Name 1:");

	private JCheckBox speaker1Novice = new JCheckBox();

	private JLabel speaker1NoviceLabel = new JLabel("Novice?:");

	private JCheckBox speaker2ESL = new JCheckBox();

	private JLabel speaker2ESLLabel = new JLabel("Esl?:");

	private JTextField speaker2Institution = new JTextField();

	private JLabel speaker2InstitutionLabel = new JLabel("Institution 2:");

	private JTextField speaker2Name = new JTextField();

	private JLabel speaker2NameLabel = new JLabel("Speaker Name 2:");

	private JCheckBox speaker2Novice = new JCheckBox();

	private JLabel speaker2NoviceLabel = new JLabel("Novice?:");

	private JCheckBox swing = new JCheckBox();

	private JLabel swingLabel = new JLabel("Swing?");

	private JTextField teamName = new JTextField();
	private JLabel teamNameLabel = new JLabel("Team Name:");

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
		
		setLayout(new GridLayout(13, 2));
		setTitle("Insert Team");

		add(teamNameLabel);
		add(teamName);
		add(swingLabel);
		add(swing);
		add(new JPanel());
		add(new JPanel());
		add(speaker1NameLabel);
		add(speaker1Name);
		add(speaker1InstitutionLabel);
		add(speaker1Institution);
		add(speaker1ESLLabel);
		add(speaker1ESL);
		add(speaker1NoviceLabel);
		add(speaker1Novice);
		add(new JPanel());
		add(new JPanel());
		add(speaker2NameLabel);
		add(speaker2Name);
		add(speaker2InstitutionLabel);
		add(speaker2Institution);
		add(speaker2ESLLabel);
		add(speaker2ESL);
		add(speaker2NoviceLabel);
		add(speaker2Novice);
		add(clear);
		add(save);
		pack();
		// this.setResizable(false);
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
