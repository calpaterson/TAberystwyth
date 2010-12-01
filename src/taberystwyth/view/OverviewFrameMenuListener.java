package taberystwyth.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OverviewFrameMenuListener implements ActionListener {
	OverviewFrame overviewFrame;
	
	public OverviewFrameMenuListener(OverviewFrame of){
		overviewFrame = of;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Open")){
			
		}
		
		if(e.getActionCommand().equals("Save")){
			
		}
		
		if(e.getActionCommand().equals("Quit")){
			System.exit(0);
		}
		
		if(e.getActionCommand().equals("Speakers")){
			
		}
		
		if(e.getActionCommand().equals("Judges")){
			
		}
		
		if(e.getActionCommand().equals("Locations")){
			
		}
		
		if(e.getActionCommand().equals("Draw Round")){
			
		}
		
		if(e.getActionCommand().equals("View Rounds")){
			
		}
		
		if(e.getActionCommand().equals("About")){
			
		}

	}

}
