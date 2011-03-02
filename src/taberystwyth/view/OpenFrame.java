package taberystwyth.view;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class OpenFrame extends JFrame {
    
    private  static final OpenFrame INSTANCE = new OpenFrame();
    
    public static OpenFrame getInstance() {
        return INSTANCE;
    }
    
    private DefaultListModel model = new DefaultListModel();
    
    private JScrollPane scrollPane = new JScrollPane(new JList(model));
    
    private JButton cancel = new JButton("Cancel");
    
    private JButton open = new JButton("Open");
    
    private OpenFrame(){
        super();
        setSize(400, 400);
        setLayout(new MigLayout("wrap 2, fill", "[rel]"));
        setTitle("Open Tournament");
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        add(scrollPane, "wrap, grow, span");
        
        add(cancel, "tag cancel, shrink");
        add(open, "tag accept, shrink");
        
        setLocationRelativeTo(null);
        
        // FIXME: Should reflect index
        for (int i = 0; i<100; i++){
            model.add(i, i);
        }
    }
}
