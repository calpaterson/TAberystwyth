/*
 * This file is a part of TAberystwyth.
 * 
 *  TAberystwyth is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */


package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import taberystwyth.db.SQLConnection;
import taberystwyth.view.WelcomeDialog;

/**
 * The action listener for the welcome dialog
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 *
 */
public class WelcomeDialogListener implements ActionListener {
	
	WelcomeDialog dialog;
	SQLConnection sql;
	
	public WelcomeDialogListener(WelcomeDialog wd){
		dialog = wd;
		sql = SQLConnection.getInstance();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("New Tab")){
			//sql.create(null); //FIXME
		}
		else if(e.getActionCommand().equals("Open Existing Tab")){
			JFileChooser chooser = new JFileChooser();
			chooser.showOpenDialog(dialog);
			sql.setDatabase(chooser.getSelectedFile());		
			}

	}

}
