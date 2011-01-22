package taberystwyth.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class AllocationFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private static AllocationFrame instance = new AllocationFrame();
    
    public static AllocationFrame getInstance() {
        return instance;
    }
    
    private AllocationFrame(){
        setLayout(new MigLayout("wrap 2, flowx, fillx", "[left]rel[right]"));
        setTitle("Allocate");
        
        /*
         * When the window is closed, make it invisible
         */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                AllocationFrame.getInstance().setVisible(false);
            }
        });
        add(new JLabel("Hello!"));
        add(new JTextField(20));
        pack();
        setMinimumSize(getSize());
    }
    
}
