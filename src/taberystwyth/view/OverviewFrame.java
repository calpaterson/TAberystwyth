package taberystwyth.view;

import java.awt.BorderLayout;

import javax.swing.*;

public class OverviewFrame extends JFrame {
	public OverviewFrame(){
		setLayout(new BorderLayout());
		add(new JMenuBar(), BorderLayout.NORTH);
		setVisible(true);
		pack();
	}
}
