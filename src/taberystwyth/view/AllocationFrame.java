package taberystwyth.view;

import javax.swing.JFrame;

public class AllocationFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private static AllocationFrame instance = new AllocationFrame();
    
    public static AllocationFrame getInstance() {
        return instance;
    }
    
    private AllocationFrame(){
        
    }
    
}
