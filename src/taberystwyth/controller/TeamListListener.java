package taberystwyth.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;

import org.apache.log4j.Logger;

public class TeamListListener implements MouseListener {
    
    private static final Logger LOG = Logger.getLogger(TeamListListener.class);

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		if(arg0.getButton() == 3){
			JList source = (JList) arg0.getSource();
			int index = source.locationToIndex(arg0.getPoint());
			source.setSelectedIndex(index);
			LOG.debug("Selected: " + source.getSelectedValue()); //FIXME open edit frame
		}
	}

}
