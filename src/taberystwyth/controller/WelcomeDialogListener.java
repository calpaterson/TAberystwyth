/*
 * This file is a part of TAberystwyth.
 * 
 * TAberystwyth is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import taberystwyth.db.SQLConnection;
import taberystwyth.view.WelcomeDialog;

/**
 * The action listener for the welcome dialog
 * 
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 * 
 */
public class WelcomeDialogListener implements ActionListener {
    
    WelcomeDialog dialog;
    SQLConnection sql;
    
    public WelcomeDialogListener(WelcomeDialog wd) {
        dialog = wd;
        sql = SQLConnection.getInstance();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Create a New Tab")) {
            JFileChooser chooser = new JFileChooser();
            
            /*
             * Loop while there is  a problem with the selection
             */
            boolean problem = false;
            do {
                problem = false;
                chooser.showDialog(null, "Create");
                try {
                    sql.create(chooser.getSelectedFile());
                } catch (Exception e1){
                    problem = true;
                    e1.printStackTrace();
                }
            } while (problem == true);
            
        } else if (e.getActionCommand().equals("Open an Existing Tab")) {
            JFileChooser chooser = new JFileChooser();
            chooser.showDialog(null, "Load");
            
            /*
             * While the user manages to cause a problem, ask him again for a 
             * file
             */
            boolean problem = false;
            do {
                problem = false;
                chooser.showOpenDialog(dialog);
                try {
                    sql.set(chooser.getSelectedFile());
                } catch (Exception e1) {
                    problem = true;
                    e1.printStackTrace();
                }
            } while (problem == true);
            
        }
        
    }
    
}
