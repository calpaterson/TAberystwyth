package taberystwyth.view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import taberystwyth.db.Generator;
import taberystwyth.db.SQLConnection;

import net.miginfocom.swing.MigLayout;

public class GenerateFrame extends JFrame implements ActionListener, PropertyChangeListener {
    
    private static final GenerateFrame INSTANCE = new GenerateFrame();
    
    public static GenerateFrame getInstance(){
        return INSTANCE;
    }
    
    private JButton generateButton;
    
    private static JProgressBar progressBar;
    
    private GenerateFrame() {
        setLayout(new MigLayout("wrap 1", "[center]"));
        setTitle("Generate");
        
        /*
         * When the window is closed, make it invisible
         */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                JudgeInsertionFrame.getInstance().setVisible(false);
            }
        });
        
        generateButton = new JButton("Generate");
        
        Generator gen = Generator.getInstance();
        int length = Generator.N_TEAMS + Generator.N_JUDGES + Generator.N_LOCATIONS;
        progressBar = new JProgressBar(0, length);
        
        generateButton.addActionListener(this);
        
        add(progressBar);
        add(generateButton);
        
        pack();
        setResizable(false);
        setLocationRelativeTo(OverviewFrame.getInstance());
    }
    
    private class Generation extends SwingWorker<Void,Void>{

        @Override
        protected Void doInBackground() throws Exception {
            Generator generator = Generator.getInstance();
            SQLConnection sql = SQLConnection.getInstance();
            
            sql.setChangeTracking(false);
            int progress = 0;
            
            for (int i = 0; i <= Generator.N_TEAMS; ++i){
                generator.genTeam();
                ++progress;
                setProgress(progress);
            }
            
            for (int i = 0; i <= Generator.N_JUDGES; ++i){
                generator.genJudge();
                ++progress;
                setProgress(progress);
            }
            
            for (int i = 0; i <= Generator.N_LOCATIONS; ++i){
                generator.genLocation();
                ++progress;
                setProgress(progress);
            }
            
            sql.setChangeTracking(true);
            return null;
        }
        
        @Override
        protected void done(){
            setProgress(0);
            generateButton.setEnabled(true);
            setCursor(null);
            GenerateFrame.getInstance().setVisible(false);
        }
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        generateButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Generation generation = new Generation();
        generation.addPropertyChangeListener(this);
        generation.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            progressBar.setValue((Integer) evt.getNewValue());
        }
    }
}
