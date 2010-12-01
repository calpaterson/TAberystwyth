package taberystwyth.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;

import taberystwyth.db.SQLConnection;

public class OverviewFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	OverviewFrameMenuListener menuListener = new OverviewFrameMenuListener(this);
	SQLConnection conn = SQLConnection.getInstance();
	
	/*
	 * Models
	 */
	DefaultListModel speakerModel;
	DefaultListModel judgeModel;
	DefaultListModel locationModel;

	private static OverviewFrame instance = new OverviewFrame();
	public static OverviewFrame getInstance(){
		return instance;
	}
	private OverviewFrame(){
		setLayout(new BorderLayout());
		setTitle("TAberystwyth");
		
		/*
		 * Set up the models...
		 */
		speakerModel = new DefaultListModel();
		judgeModel = new DefaultListModel();
		locationModel = new DefaultListModel();
		
		/*
		 * ...and the lists
		 */
		JList speakerList = new JList(speakerModel);
		JList judgeList = new JList(judgeModel);
		JList locationList = new JList(locationModel);
		
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
		setSize(new Dimension(500,500));
		try {
			refreshSpeakers();
			//refreshJudges(); //FIXME:
			refreshLocation();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		refreshList("speaker", speakerModel);
	}
	
	public void refreshJudges() throws SQLException{
		refreshList("panel", judgeModel);
	}
	
	public void refreshLocation() throws SQLException{
		refreshList("location", locationModel);
	}
	
	private void refreshList(String table, DefaultListModel model) throws SQLException{
		model.removeAllElements();
		ResultSet rs = conn.executeQuery("select (name) from " + table + ";");
		int index = 0;
		while(rs.next()){
			System.out.println("Inserting " + rs.getString("NAME"));
			model.add(index, rs.getString("NAME"));
			++index;
		}
	}
}
