package taberystwyth.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class OverviewFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public OverviewFrame(){
		setLayout(new BorderLayout());
		
		/*
		 * Add menu bar
		 */
		add(new MainFrameMenuBar(), BorderLayout.NORTH);
		
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
		viewPanel.add(new JScrollPane(new JList()));
		viewPanel.add(new JScrollPane(new JList()));
		viewPanel.add(new JScrollPane(new JList()));
		holdingPanel.add(viewPanel, BorderLayout.CENTER);
		add(holdingPanel, BorderLayout.CENTER);
		setVisible(true);
		pack();
	}
}
