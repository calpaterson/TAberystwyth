package taberystwyth.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeamInsertionListener implements ActionListener {
	TeamInsertionFrame frame;
	
	public TeamInsertionListener(TeamInsertionFrame f){
		frame = f;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Clear")){
			frame.clear();
		}
		
		if(e.getActionCommand().equals("Save")){
			frame.save();
		}

	}

}
