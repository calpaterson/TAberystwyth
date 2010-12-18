package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import taberystwyth.db.SQLConnection;
import taberystwyth.view.JudgeInsertionFrame;
import taberystwyth.view.OverviewFrame;

public class JudgeInsertionFrameListener implements ActionListener {
	
	private JudgeInsertionFrame frame;
	
	private SQLConnection conn = SQLConnection.getInstance();

	public void actionPerformed(ActionEvent e) {
		frame = JudgeInsertionFrame.getInstance();
		if (e.getActionCommand().equals("Save")){
			String statement = "insert into judge (name, rating) values (" +
							   "\"" +
							   frame.getJudgeName().getText() +
							   "\", \"" +
							   frame.getRating().getText() +
							   "\");";
			conn.execute(statement);
		} else if (e.getActionCommand().equals("Clear")){
			frame.getJudgeName().setText("");
			frame.getRating().setText("");
		}
	}

}
