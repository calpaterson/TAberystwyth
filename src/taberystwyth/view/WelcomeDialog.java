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

package taberystwyth.view;

import java.awt.GridLayout;
import java.io.File;
import java.util.concurrent.SynchronousQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;

import taberystwyth.controller.WelcomeDialogListener;

/**
 * A dialog to offer the user to create a new tab or open an old one that is shown when
 * the program launches.
 * @author Roberto Sarrionandia [r@sarrionandia.com]s
 */
public class WelcomeDialog extends JFrame {
	private static final long serialVersionUID = 1L;
	private SynchronousQueue<File> selection = new SynchronousQueue<File>();
	WelcomeDialogListener listener;

	public WelcomeDialog(){
		listener = new WelcomeDialogListener(this);
		
		this.setLayout(new GridLayout(2,1));
		
		JLabel message = new JLabel("Create a new Tab, or load an existing tab file.");
		JButton create = new JButton("New Tab");
		JButton open = new JButton("Open Existing Tab");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(create);
		buttonPanel.add(open);
		
		this.add(message);
		this.add(buttonPanel);
		
		this.setVisible(true);
		this.pack();
	}

    public SynchronousQueue<File> getSelection() {
        return selection;
    }

}
