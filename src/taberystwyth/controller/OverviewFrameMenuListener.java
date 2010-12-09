package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import taberystwyth.view.LocationInsertionFrame;
import taberystwyth.view.OverviewFrame;
import taberystwyth.view.TeamInsertionFrame;
import taberystwyth.view.ViewRoundFrame;

public class OverviewFrameMenuListener implements ActionListener {
	OverviewFrame overviewFrame;
	TeamInsertionFrame teamInsertionFrame;
	LocationInsertionFrame locationInsertionFrame;
	ViewRoundFrame viewRoundFrame;

	public OverviewFrameMenuListener(OverviewFrame overviewFrame) {
		overviewFrame = overviewFrame;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Quit")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("Teams")) {
			new TeamInsertionFrame();
		} else if (e.getActionCommand().equals("Locations")) {
			new LocationInsertionFrame();
		} else if (e.getActionCommand().equals("View Rounds")) {
			new ViewRoundFrame();
		} else {
			JOptionPane.showMessageDialog(overviewFrame,
					"Not currently implemented!");
		}

	}

	private File getFile(String title) {
		/*
		 * Make a new filechooser, along with the
		 */
		JFileChooser fc = new JFileChooser(title);
		fc.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "Debate Tab Files";
			}

			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".tab")) {
					return true;
				} else if (f.isDirectory()) {
					return true;
				}
				return false;
			}
		});
		int result = fc.showOpenDialog(overviewFrame);
		File returnValue;
		if (result == JFileChooser.APPROVE_OPTION) {
			returnValue = fc.getSelectedFile();
		} else {
			returnValue = null;
		}
		return returnValue;
	}

}
