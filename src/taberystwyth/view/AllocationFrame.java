package taberystwyth.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class AllocationFrame extends JFrame {
	
	JLabel drawTypeLabel = new JLabel("Type of Draw");
	JComboBox drawTypeBox = new JComboBox();
	DefaultComboBoxModel drawTypes = new DefaultComboBoxModel();

	JButton cancel = new JButton("Cancel");
	JButton allocate = new JButton("Allocate");
	
    
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
        
        /*
         * Set up comboboxes
         */
        drawTypes.addElement("Normal");
        
        
        add(drawTypeLabel);
        add(drawTypeBox);
        add(cancel, "tag cancel");
        add(allocate, "tag apply");
        pack();
        setMinimumSize(getSize());
    }
    
}
