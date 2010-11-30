package taberystwyth.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainFrameMenu extends JMenuBar {
	JMenu fileMenu = new JMenu("File");
	JMenu roundsMenu = new JMenu("Rounds");
	
	JMenuItem open = new JMenuItem("Open");
	JMenuItem save = new JMenuItem("Save");
	JMenuItem quit = new JMenuItem("Quit");
	
	JMenuItem drawRound = new JMenuItem("Draw Round");
	JMenuItem viewRounds = new JMenuItem("View Rounds");
	
	public MainFrameMenu(){
		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.add(quit);
		
		roundsMenu.add(drawRound);
		roundsMenu.add(viewRounds);
		
		add(fileMenu);
		add(roundsMenu);
	}
	
}
