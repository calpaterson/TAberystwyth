package taberystwyth.view;

import javax.swing.JFrame;

import org.apache.log4j.Logger;


/**
 * @author Roberto Sarrionandia [r@sarrionandia.com]
 *  A frame to show a list of rooms that the user can select from 
 *  to show the data entry frame for that particular room
 */
public class DataEntryListFrame extends JFrame {
    
    private static final Logger LOG = Logger.getLogger(LegacyResultEntryFrame.class);
    private static DataEntryListFrame instance = new DataEntryListFrame();

    
    public DataEntryListFrame(){
        
    }
    
    /**
     * @return The instance of the DataEntryListFrame
     */
    public DataEntryListFrame getInstance(){
        return instance;
    }
}
