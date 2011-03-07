package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import taberystwyth.view.ResultEntryFrame;

public class ResultEntryFrameListener implements ActionListener {
    
    ResultEntryFrame frame;
    
    public ResultEntryFrameListener(ResultEntryFrame frame){
        this.frame = frame;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.equals("Save")) frame.commit(); 
    }
    
}
