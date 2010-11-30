package taberystwyth.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class OverviewFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public OverviewFrame(){
		setLayout(new BorderLayout());
		add(new MainFrameMenu(), BorderLayout.NORTH);
		JPanel viewPanel = new JPanel(new GridLayout(1,3));
		viewPanel.add(new JLabel("Judges"));
		viewPanel.add(new JLabel("Teams"));
		viewPanel.add(new JLabel("Speakers"));
		add(viewPanel);
		setVisible(true);
		pack();
	}
}
