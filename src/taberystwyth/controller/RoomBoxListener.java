package taberystwyth.controller;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import taberystwyth.view.ResultEntryFrame;

public class RoomBoxListener implements ItemListener {
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        ResultEntryFrame.getInstance().changeRoom();
        
    }
    
}
