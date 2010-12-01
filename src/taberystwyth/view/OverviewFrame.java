package taberystwyth.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import taberystwyth.db.SQLConnection;

public class OverviewFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	OverviewFrameMenuListener menuListener = new OverviewFrameMenuListener(this);
	SQLConnection conn = SQLConnection.getInstance();
	
	JList speakerList = new JList();
	JList judgeList = new JList();
	JList locationList = new JList();

	public OverviewFrame(){
		setLayout(new BorderLayout());
		setTitle("TAberystwyth");
		
		/*
		 * Add menu bar
		 */
		add(new MainFrameMenuBar(menuListener), BorderLayout.NORTH);
		
		/*
		 * Holding panel
		 */
		JPanel holdingPanel = new JPanel(new BorderLayout());
		
		/*
		 * Title Panel
		 */
		JPanel titlePanel = new JPanel(new GridLayout(1,3));
		titlePanel.add(new JLabel("Judges"));
		titlePanel.add(new JLabel("Teams"));
		titlePanel.add(new JLabel("Locations"));
		holdingPanel.add(titlePanel, BorderLayout.NORTH);
		
		/*
		 * View Panel
		 */
		JPanel viewPanel = new JPanel(new GridLayout(1,3));
		viewPanel.add(new JScrollPane(speakerList));
		viewPanel.add(new JScrollPane(judgeList));
		viewPanel.add(new JScrollPane(locationList));
		holdingPanel.add(viewPanel, BorderLayout.CENTER);
		add(holdingPanel, BorderLayout.CENTER);
		setVisible(true);
		pack();
	}

	public void open() {
		// TODO Auto-generated method stub
		
	}

	public void save() {
		// TODO Auto-generated method stub
		
	}

	public void quit() {
		System.exit(0);		
	}

	public void insertSpeakers() {
		new TeamInsertionFrame();	
	}

	public void insertJudges() {
		// TODO Auto-generated method stub
		
	}

	public void insertLocations() {
		// TODO Auto-generated method stub
		
	}

	public void drawRound() {
		// TODO Auto-generated method stub
		
	}

	public void viewRounds() {
		// TODO Auto-generated method stub
		
	}

	public void about() {
		// TODO Auto-generated method stub
		
	}
	
	public void refreshSpeakers() throws SQLException{
		speakerList.removeAll();
		ResultSet speakers = conn.executeQuery("select (name) from speaker;");
		while(speakers.next()){
			speakerList.add(new JLabel(speakers.getString("NAME")));
		}
	}
}
