/* This file is part of TAberystwyth, a debating competition organiser
 * Copyright (C) 2010, Roberto Sarrionandia and Cal Paterson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import taberystwyth.db.SQLConnection;
import taberystwyth.allocation.Allocator;
import taberystwyth.view.AboutDialog;
import taberystwyth.view.AllocationFrame;
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
	    if (e.getActionCommand().equals("Quit")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("Teams")) {
			TeamInsertionFrame.getInstance().setVisible(true);
		} else if (e.getActionCommand().equals("Locations")) {
			LocationInsertionFrame.getInstance().setVisible(true);
		} else if (e.getActionCommand().equals("Judges")) {
			JudgeInsertionFrame.getInstance().setVisible(true);
		} else if (e.getActionCommand().equals("Draw Preliminary Round")){
			AllocationFrame.getInstance().setVisible(true);
		} else if (e.getActionCommand().equals("About")){
		    AboutDialog.getInstance().setVisible(true);
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
