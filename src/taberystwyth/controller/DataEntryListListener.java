package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import taberystwyth.view.DataEntryListFrame;

public class DataEntryListListener implements ActionListener {
    DataEntryListFrame frame;
    
    public DataEntryListListener(DataEntryListFrame frame){
        this.frame = frame;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Enter Data")) {
            frame.enterData();      
        }
    }
    
}
