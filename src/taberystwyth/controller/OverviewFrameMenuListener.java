package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import taberystwyth.db.SQLConnection;
import taberystwyth.view.JudgeInsertionFrame;
import taberystwyth.view.LocationInsertionFrame;
import taberystwyth.view.OverviewFrame;
import taberystwyth.view.TeamInsertionFrame;
import taberystwyth.view.ViewRoundFrame;

public class OverviewFrameMenuListener implements ActionListener {
	OverviewFrame overviewFrame;

	public OverviewFrameMenuListener(OverviewFrame overviewFrame) {
		this.overviewFrame = overviewFrame;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New Tab")){
			JFileChooser fc = new JFileChooser();
			int option = fc.showDialog(overviewFrame, "Create");
			if (option == JFileChooser.APPROVE_OPTION){
				File tab = fc.getSelectedFile();
				try {
					if(!tab.createNewFile()){
						throw new IOException("File already exists");
					}
					SQLConnection.getInstance().setDatabase(tab);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		else if (e.getActionCommand().equals("Open Tab")){
			JFileChooser fc = new JFileChooser();
			int option = fc.showOpenDialog(overviewFrame);
			if (option == JFileChooser.APPROVE_OPTION){
				File tab = fc.getSelectedFile();
				SQLConnection.getInstance().setDatabase(tab);
			}
		}
	    else if (e.getActionCommand().equals("Quit")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("Teams")) {
			TeamInsertionFrame.getInstance().setVisible(true);
		} else if (e.getActionCommand().equals("Locations")) {
			LocationInsertionFrame.getInstance().setVisible(true);
		} else if (e.getActionCommand().equals("Judges")) {
			JudgeInsertionFrame.getInstance().setVisible(true);
		//} else if (e.getActionCommand().equals("View Rounds")) {
		//	new ViewRoundFrame(); FIXME
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
