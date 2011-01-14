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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

/**
 * A listener to duplicate one text field with another when the original
 * loses focus
 * @author Roberto Sarrionandia
 *
 */
public class TeamFocusListener implements FocusListener {
	
	private JTextField source;
	private JTextField target;
	
	/**
	 * 
	 * @param sourceField The field that the listener is attached to
	 * @param targetField The field to manipulates
	 */
	public TeamFocusListener(JTextField sourceField, JTextField targetField){
		source = sourceField;
		target = targetField;
	}

	public void focusGained(FocusEvent arg0) {
		// VOID

	}

	public void focusLost(FocusEvent arg0) {
		if (target.getText().equals("")) {
			target.setText(source.getText());
		}

	}

}
