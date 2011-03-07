/*
 * This file is part of TAberystwyth, a debating competition organiser
 * Copyright (C) 2010, Roberto Sarrionandia and Cal Paterson
 * 
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

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

import org.apache.log4j.Logger;

import net.miginfocom.swing.MigLayout;
import taberystwyth.db.Generator;

/**
 * Simple frame that does the generation of random tabs
 */
public class GenerateFrame extends JFrame implements ActionListener,
                PropertyChangeListener {
    
    private static final long serialVersionUID = 1L;
    
    private static final GenerateFrame INSTANCE = new GenerateFrame();
    
    /**
     * @return the instance
     */
    public static GenerateFrame getInstance() {
        return INSTANCE;
    }
    
    private JButton generateButton;
    
    private static JProgressBar progressBar;
    
    /**
     * Instantiates a new generation frame.
     */
    private GenerateFrame() {
        setLayout(new MigLayout("wrap 1", "[center]"));
        setTitle("Generate");
        
        final Logger LOG = Logger.getLogger(GenerateFrame.class);
        
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
        
        progressBar = new JProgressBar();
        
        generateButton.addActionListener(this);
        
        add(progressBar);
        add(generateButton);
        
        pack();
        setResizable(false);
        setLocationRelativeTo(OverviewFrame.getInstance());
    }
    
    private class Generation extends SwingWorker<Void, Void> {
        
        private final Logger LOG = Logger.getLogger(Generation.class);
        
        @Override
        protected Void doInBackground() throws Exception {
            
            Generator generator = Generator.getInstance();
            
            int maxProgress = Generator.N_TEAMS + Generator.N_JUDGES
                            + Generator.N_LOCATIONS;
            int currentProgress = 0;
            
            for (int i = 0; i <= Generator.N_TEAMS; ++i) {
                generator.genTeam();
                ++currentProgress;
                setProgress((int) Math.round((100.0 / maxProgress)
                                * currentProgress));
            }
            
            for (int i = 0; i <= Generator.N_JUDGES; ++i) {
                generator.genJudge();
                ++currentProgress;
                setProgress((int) Math.round((100.0 / maxProgress)
                                * currentProgress));
            }
            
            for (int i = 0; i <= Generator.N_LOCATIONS; ++i) {
                generator.genLocation();
                ++currentProgress;
                setProgress((int) Math.round((100.0 / maxProgress)
                                * currentProgress));
            }
            
            return null;
        }
        
        @Override
        protected void done() {
            setProgress(0);
            generateButton.setEnabled(true);
            setCursor(null);
            OverviewFrame.getInstance().setEnabled(true);
            GenerateFrame.getInstance().setVisible(false);
        }
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        generateButton.setEnabled(false);
        OverviewFrame.getInstance().setEnabled(false);
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
