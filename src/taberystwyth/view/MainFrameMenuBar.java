package taberystwyth.view;

import javax.swing.JMenuBar;

public class MainFrameMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	public MainFrameMenuBar(OverviewFrameMenuListener menuListener){
		setVisible(true);
		this.add(new MainFrameMenu(menuListener));
		
	}

}
