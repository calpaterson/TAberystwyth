package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import taberystwyth.prelim.Allocator;
import taberystwyth.prelim.JudgesRequiredException;
import taberystwyth.prelim.LocationsRequiredException;
import taberystwyth.prelim.SwingTeamsRequiredException;
import taberystwyth.view.AllocationFrame;

public class AllocationFrameListener implements ActionListener {

	public void actionPerformed(ActionEvent event) {
		
		if (event.getActionCommand().equals("Cancel")){
			AllocationFrame.getInstance().setVisible(false);
		}
		
		else if(event.getActionCommand().equals("Allocate")){
			try {
                Allocator.getInstance().allocate();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //FIXME
            } catch (SwingTeamsRequiredException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (LocationsRequiredException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JudgesRequiredException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}
		
		else{
			System.out.println("Not Implemented");
		}

	}

}
