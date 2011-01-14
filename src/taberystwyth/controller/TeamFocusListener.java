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
