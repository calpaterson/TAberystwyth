package taberystwyth.controller;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import taberystwyth.view.ResultEntryFrame;

/**
 * A listener which makes the ResultEntryFrame change room when a combobox has been changed
 * It should be attached to the roomBox of the data entry window
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 *
 */
public class RoomBoxListener implements ItemListener {
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        ResultEntryFrame.getInstance().changeRoom();
        
    }
    
}
