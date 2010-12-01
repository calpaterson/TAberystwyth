package taberystwyth.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainFrameMenu extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	JMenu fileMenu = new JMenu("File");
	JMenu roundsMenu = new JMenu("Rounds");
	JMenu insertMenu = new JMenu("Insert");
	JMenu helpMenu = new JMenu("Help");
	
	JMenuItem new_ = new JMenuItem("New");
	JMenuItem open = new JMenuItem("Open");
	JMenuItem save = new JMenuItem("Save");
	JMenuItem quit = new JMenuItem("Quit");
	
	JMenuItem speakers = new JMenuItem("Teams");
	JMenuItem judges = new JMenuItem("Judges");
	JMenuItem locations = new JMenuItem("Locations");
	
	JMenuItem about = new JMenuItem("About");
	
	JMenuItem drawRound = new JMenuItem("Draw Round");
	JMenuItem viewRounds = new JMenuItem("View Rounds");
	
	public MainFrameMenu(OverviewFrameMenuListener menuListener){
		fileMenu.add(new_);
		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.add(quit);
		
		insertMenu.add(speakers);
		insertMenu.add(judges);
		insertMenu.add(locations);
		
		roundsMenu.add(drawRound);
		roundsMenu.add(viewRounds);
		
		helpMenu.add(about);
		
		add(fileMenu);
		add(insertMenu);
		add(roundsMenu);
		add(helpMenu);
		
		open.addActionListener(menuListener);
		save.addActionListener(menuListener);
		quit.addActionListener(menuListener);
		speakers.addActionListener(menuListener);
		judges.addActionListener(menuListener);
		locations.addActionListener(menuListener);
		drawRound.addActionListener(menuListener);
		viewRounds.addActionListener(menuListener);
		about.addActionListener(menuListener);
	}
	
}
