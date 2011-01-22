package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

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
                /*
                 * This is a "proper" error...not much can be done
                 */
                e.printStackTrace();
                JOptionPane.showMessageDialog(AllocationFrame.getInstance(), 
                        "Allocation has thrown an SQL Error.", 
                        "SQL Error", JOptionPane.ERROR_MESSAGE);
            } catch (SwingTeamsRequiredException e) {
                JOptionPane.showMessageDialog(AllocationFrame.getInstance(), 
                        "The total number of teams needs to be divisble by " + 
                        "4.  You need to add " + e.getRequired() + 
                        " swing team(s)", "Swing Teams Required",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (LocationsRequiredException e) {
                JOptionPane.showMessageDialog(AllocationFrame.getInstance(), 
                        "Not enough locations in order to run this round.  " +
                        "You need to add " + e.getRequired() + " location(s).", 
                        "Not enough locations", 
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (JudgesRequiredException e) {
                JOptionPane.showMessageDialog(AllocationFrame.getInstance(), 
                        "Not enough judges in order to run this round.  " +
                        "You need to add " + e.getRequired() + " judge(s).", 
                        "Not enough judges", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
		}
		
		else{
			System.out.println("Not Implemented");
		}

	}

}
