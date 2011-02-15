package taberystwyth.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

public class ResultEntryFrame extends JFrame {
    
    private Logger LOG = Logger.getLogger(ResultEntryFrame.class);
    private static ResultEntryFrame instance = new ResultEntryFrame();
    
    private ResultEntryFrame() {
        setLayout(new MigLayout("wrap 4", "[left]rel[right]"));
        setTitle("Enter Results");
        
        /*
         * When the window is closed, make it invisible
         */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                ResultEntryFrame.getInstance().setVisible(false);
            }
        });
    }
    
    public static ResultEntryFrame getInstance(){
        return instance;
    }
    
    public void setVisible(boolean b){
        if(b){
            this.refreshComponents();
            super.setVisible(b);
        }
        else super.setVisible(b);
    }

    /**
     * Make sure the components are properly displayed
     */
    private void refreshComponents() {
        // TODO Auto-generated method stub
        
    }
    
    

    
}
