package taberystwyth.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import taberystwyth.controller.OverviewFrameMenuListener;
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
		 * When the window is closed, exit the program
		 */
		addWindowListener(new WindowAdapter() {
	        @Override
			public void windowClosing(WindowEvent evt) {
	            System.exit(0);
	        }
		});
		
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
		add(new OverviewFrameMenu(new OverviewFrameMenuListener(this)), BorderLayout.NORTH);
		
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
		viewPanel.add(new JScrollPane(judgeList));
		viewPanel.add(new JScrollPane(speakerList));
		viewPanel.add(new JScrollPane(locationList));
		holdingPanel.add(viewPanel, BorderLayout.CENTER);
		add(holdingPanel, BorderLayout.CENTER);
		setVisible(true);
		pack();
		setSize(new Dimension(500,500));
		try {
			refreshTeams();
			//refreshJudges(); //FIXME:
			refreshLocation();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void refreshTeams() throws SQLException{
		refreshList("team", speakerModel);
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
			String entry = rs.getString("NAME");
			/*
			 * If it's a team, append the institution of the team
			 */
			if (table.equals("team")){
				entry += " (" + getInstitution(entry) + ")";
			}
			model.add(index, entry);
			++index;
		}
	}
	
	private String getInstitution(String teamName) {
		String query = null;
		String returnValue = null;
		try{
			/*
			 * Get the speakers on the team
			 */
			query = "select speaker1, speaker2 from team where team.name = '" + 
				teamName + "';";
			ResultSet rs = conn.executeQuery(query);
			rs.next();
			String speaker1 = rs.getString("SPEAKER1");
			String speaker2 = rs.getString("SPEAKER2");
			
			/*
			 * Get the institution of speaker1
			 */
			query = "select (institution) from speaker where speaker.name = '" +
				speaker1 + "'";
			rs = conn.executeQuery(query);
			rs.next();
			String inst1 = rs.getString("INSTITUTION");
			rs.close();
			
			/*
			 * Get the institution of speaker2
			 */
			query = "select (institution) from speaker where speaker.name = '" +
				speaker2 + "'";
			rs = conn.executeQuery(query);
			rs.next();
			String inst2 = rs.getString("INSTITUTION");
			rs.close();
			
			/*
			 * Compare them
			 */
			if (!inst1.equals(inst2)){
				returnValue = "Mixed";
			} else {
				returnValue = inst1;
			}
			
			
		} catch (SQLException e){
			conn.panic(e, "Unable to find what institution two speakers are from.  Query was:\n"
					+ query);
		}
		return returnValue;
	}
}
