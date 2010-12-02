package taberystwyth.view;

import javax.swing.JMenuBar;

public class OverviewFrameMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	public OverviewFrameMenuBar(OverviewFrameMenuListener menuListener){
		setVisible(true);
		this.add(new OverviewFrameMenu(menuListener));
		
	}

}
