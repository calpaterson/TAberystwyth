package taberystwyth.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import taberystwyth.controller.CreationFrameListener;

import net.miginfocom.swing.MigLayout;

public class CreationFrame extends JFrame {
    
    private static final CreationFrame INSTANCE = new CreationFrame();
    
    public static CreationFrame getInstance() {
        return INSTANCE;
    }
    
    private File file;
    
    private CreationFrame() {
        super();
        setLayout(new MigLayout("wrap 2", "[left]rel[right]"));
        setTitle("Create Tournament");
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        CreationFrameListener listener = CreationFrameListener.getInstance();
        
        cancelButton.addActionListener(listener);
        cancelButton.addActionListener(listener);
        
        createButton.setEnabled(false);
        createButton.addActionListener(listener);
        
        cancelButton.addActionListener(listener);
        
        add(touramentNameLabel);
        add(tournamentName, "span");
        add(cancelButton, "tag cancel");
        add(createButton, "tag apply");
        
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        
        /*
         * Add a listener to prevent empty tournament names
         */
        tournamentName.addKeyListener(new KeyListener() {
            
            private void check(){
                if (tournamentName.getText().isEmpty()){
                    createButton.setEnabled(false);
                } else {
                    createButton.setEnabled(true);
                }
            }
            
            @Override
            public void keyTyped(KeyEvent e) {
                check();
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                check();
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                check();
            }
        });
        
        
        /*
         * When the window is closed, make it invisible FIXME WRONG
         */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                //FIXME: do something
            }
        });
    }
    
    public void setFile(File file) {
        this.file = file;
        createButton.setEnabled(true);
    }

    public File getFile() {
        return file;
    }

    private JLabel touramentNameLabel = new JLabel("Full Tournament Name");
    private JTextField tournamentName = new JTextField(40);
    
    private JButton createButton = new JButton("Create");
    private JButton cancelButton = new JButton("Cancel");

    public String getTournamentName() {
        return tournamentName.getText();
    }
    
}
