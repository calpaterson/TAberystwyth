package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import taberystwyth.view.OverviewFrame;

public class DebugMenuListener implements ActionListener {
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("FIXME")) {
            
        } else {
            JOptionPane.showMessageDialog(OverviewFrame.getInstance(),
                    "Not implemented!", "Not implemented",
                    JOptionPane.ERROR_MESSAGE);
            
        }
    }
    
}
