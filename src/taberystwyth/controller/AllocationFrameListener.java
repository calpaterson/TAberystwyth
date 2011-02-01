package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import taberystwyth.allocation.Allocator;
import taberystwyth.allocation.exceptions.JudgesRequiredException;
import taberystwyth.allocation.exceptions.LocationsRequiredException;
import taberystwyth.allocation.exceptions.SwingTeamsRequiredException;
import taberystwyth.allocation.options.JudgeAllocation;
import taberystwyth.allocation.options.LocationAllocation;
import taberystwyth.allocation.options.TeamAllocation;
import taberystwyth.view.AllocationFrame;

public class AllocationFrameListener implements ActionListener, ItemListener{

	public void actionPerformed(ActionEvent event) {
		
		if (event.getActionCommand().equals("Cancel")){
			AllocationFrame.getInstance().setVisible(false);
		}
		
		else if(event.getActionCommand().equals("Allocate")){
			try {
			    // FIXME
			    Allocator.getInstance().allocate(TeamAllocation.WUDC,
			            JudgeAllocation.BALANCED, 
			            LocationAllocation.RANDOM
			            );
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

	public void itemStateChanged(ItemEvent e) {
		AllocationFrame.getInstance().updateDescriptions();
	}

}
