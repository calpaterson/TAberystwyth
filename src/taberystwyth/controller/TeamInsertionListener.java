package taberystwyth.controller;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JPanel;

import taberystwyth.db.SQLConnection;
import taberystwyth.view.OverviewFrame;
import taberystwyth.view.TeamInsertionFrame;

public class TeamInsertionListener implements ActionListener {
	TeamInsertionFrame frame;
	SQLConnection conn = SQLConnection.getInstance();

	public TeamInsertionListener(TeamInsertionFrame frame) {
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Clear")) {
			frame.getTeamName().setText("");
			frame.getSwing().setSelected(false);

			frame.getSpeaker1Name().setText("");
			frame.getSpeaker1Institution().setText("");
			frame.getSpeaker1ESL().setSelected(false);
			frame.getSpeaker1Novice().setSelected(false);

			frame.getSpeaker2Name().setText("");
			frame.getSpeaker2Institution().setText("");
			frame.getSpeaker2ESL().setSelected(false);
			frame.getSpeaker2Novice().setSelected(false);
		}

		if (e.getActionCommand().equals("Save")) {
			String speaker1Insert = "insert into speaker (institution, name, esl, novice) values("
					+ "\""
					+ frame.getSpeaker1Institution().getText()
					+ "\""
					+ ", "
					+ "\""
					+ frame.getSpeaker1Name().getText()
					+ "\""
					+ ", "
					+ "\""
					+ frame.getSpeaker1ESL().isSelected()
					+ "\""
					+ ", "
					+ "\""
					+ frame.getSpeaker1Novice().isSelected()
					+ "\"" + ");";
			String speaker2Insert = "insert into speaker (institution, name, esl, novice) values("
					+ "\""
					+ frame.getSpeaker2Institution().getText()
					+ "\""
					+ ", "
					+ "\""
					+ frame.getSpeaker2Name().getText()
					+ "\""
					+ ", "
					+ "\""
					+ frame.getSpeaker2ESL().isSelected()
					+ "\""
					+ ", "
					+ "\""
					+ frame.getSpeaker2Novice().isSelected()
					+ "\"" + ");";
			String teamInsert = "insert into team (speaker1, speaker2, name) values("
					+ "\""
					+ frame.getSpeaker1Name().getText()
					+ "\""
					+ ", "
					+ "\""
					+ frame.getSpeaker2Name().getText()
					+ "\""
					+ ", "
					+ "\"" + frame.getTeamName().getText() + "\"" + ");";

			conn.execute(speaker1Insert);
			conn.execute(speaker2Insert);
			conn.execute(teamInsert);
		}
		;
	}

}
