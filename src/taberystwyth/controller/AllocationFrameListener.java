package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import taberystwyth.allocation.Allocator;
import taberystwyth.allocation.exceptions.JudgesRequiredException;
import taberystwyth.allocation.exceptions.LocationsRequiredException;
import taberystwyth.allocation.exceptions.SwingTeamsRequiredException;
import taberystwyth.allocation.options.Balanced;
import taberystwyth.allocation.options.Random;
import taberystwyth.allocation.options.TabAlgorithm;
import taberystwyth.allocation.options.WUDC;
import taberystwyth.view.AllocationFrame;

public class AllocationFrameListener implements ActionListener, ItemListener {
    
    private static final Logger LOG = Logger.getLogger(AllocationFrame.class);
    
    @Override
    public void actionPerformed(ActionEvent event) {
        
        if (event.getActionCommand().equals("Cancel")) {
            AllocationFrame.getInstance().setVisible(false);
        }

        else if (event.getActionCommand().equals("Allocate")) {
            try {                
                Allocator.getInstance().allocate(new WUDC(), new Balanced(), new Random());
            } catch (SQLException e) {
                /*
                 * This is a "proper" error...not much can be done
                 */
                LOG.error("Allocation caused an sql exception", e);
                JOptionPane.showMessageDialog(AllocationFrame.getInstance(),
                                "Allocation has thrown an SQL Error.",
                                "SQL Error", JOptionPane.ERROR_MESSAGE);
            } catch (SwingTeamsRequiredException e) {
                LOG.error("Need swing teams", e);
                JOptionPane.showMessageDialog(
                                AllocationFrame.getInstance(),
                                "The total number of teams needs to be divisble by "
                                                + "4.  You need to add "
                                                + e.getRequired()
                                                + " swing team(s)",
                                "Swing Teams Required",
                                JOptionPane.INFORMATION_MESSAGE);
            } catch (LocationsRequiredException e) {
                LOG.error("Need more locations", e);
                JOptionPane.showMessageDialog(
                                AllocationFrame.getInstance(),
                                "Not enough locations in order to run this round.  "
                                                + "You need to add "
                                                + e.getRequired()
                                                + " location(s).",
                                "Not enough locations",
                                JOptionPane.INFORMATION_MESSAGE);
            } catch (JudgesRequiredException e) {
                LOG.error("Need more judges", e);
                JOptionPane.showMessageDialog(
                                AllocationFrame.getInstance(),
                                "Not enough judges in order to run this round.  "
                                                + "You need to add "
                                                + e.getRequired()
                                                + " judge(s).",
                                "Not enough judges",
                                JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        AllocationFrame.getInstance().updateDescriptions();
    }
    
}
