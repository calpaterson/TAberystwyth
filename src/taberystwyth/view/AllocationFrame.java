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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import taberystwyth.allocation.options.AlgorithmListBuilder;
import taberystwyth.allocation.options.TabAlgorithm;
import taberystwyth.controller.AllocationFrameListener;

/**
 * The frame which allows the user to draw a round and select the algorithms he
 * would like to draw with.
 * 
 * @author Roberto Sarrionandia
 * @author Cal Paterson
 * 
 */
final public class AllocationFrame extends JFrame {
    
    private AllocationFrameListener listener = new AllocationFrameListener();
    
    private JTextField motionField = new JTextField(20);
    
    private JLabel teamDrawTypeLabel = new JLabel("Draw teams with: ");
    private JTextArea teamDrawTypeDescription = new JTextArea(3, 32);
    private JComboBox teamDrawTypeBox = new JComboBox();
    private DefaultComboBoxModel teamDrawTypes = new DefaultComboBoxModel();
    
    private JLabel locationDrawTypeLabel = new JLabel("Draw locations with: ");
    private JTextArea locationDrawTypeDescription = new JTextArea(3, 32);
    private JComboBox locationDrawTypeBox = new JComboBox();
    private DefaultComboBoxModel locationDrawTypes = new DefaultComboBoxModel();
    
    private JLabel judgeDrawTypeLabel = new JLabel("Draw judges with: ");
    private JTextArea judgeDrawTypeDescription = new JTextArea(3, 32);
    private JComboBox judgeDrawTypeBox = new JComboBox();
    private DefaultComboBoxModel judgeDrawTypes = new DefaultComboBoxModel();
    
    private JButton cancel = new JButton("Cancel");
    private JButton allocate = new JButton("Allocate");
    
    private static final long serialVersionUID = 1L;
    private static AllocationFrame instance = new AllocationFrame();
    
    /**
     * Return the instance of the frame
     * 
     * @return The AllocationFrame instance
     */
    public static AllocationFrame getInstance() {
        return instance;
    }
    
    private AllocationFrame() {
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
         * Disable the allocate button initially
         */
        allocate.setEnabled(false);
        
        /*
         * Set up description boxes
         */
        teamDrawTypeDescription.setLineWrap(true);
        teamDrawTypeDescription.setWrapStyleWord(true);
        locationDrawTypeDescription.setLineWrap(true);
        locationDrawTypeDescription.setWrapStyleWord(true);
        judgeDrawTypeDescription.setLineWrap(true);
        judgeDrawTypeDescription.setWrapStyleWord(true);
        
        teamDrawTypeDescription.setBackground(getBackground());
        locationDrawTypeDescription.setBackground(getBackground());
        judgeDrawTypeDescription.setBackground(getBackground());
        
        teamDrawTypeDescription.setEditable(false);
        locationDrawTypeDescription.setEditable(false);
        judgeDrawTypeDescription.setEditable(false);
        
        /*
         * Set up comboboxes
         */
        for (TabAlgorithm ta : AlgorithmListBuilder.getTeamAlgorithms()) {
            teamDrawTypes.addElement(ta);
        }
        
        for (TabAlgorithm ja : AlgorithmListBuilder.getJudgeAlgorithms()) {
            judgeDrawTypes.addElement(ja);
        }
        
        for (TabAlgorithm la : AlgorithmListBuilder.getLocationAlgorithms()) {
            locationDrawTypes.addElement(la);
        }
        
        teamDrawTypeBox.setModel(teamDrawTypes);
        judgeDrawTypeBox.setModel(judgeDrawTypes);
        locationDrawTypeBox.setModel(locationDrawTypes);
        
        /*
         * Add to the frame
         */
        add(new JLabel("Motion: "));
        add(motionField);
        
        add(teamDrawTypeLabel);
        add(teamDrawTypeBox, "wrap");
        add(new JScrollPane(teamDrawTypeDescription), "span");
        
        add(judgeDrawTypeLabel);
        add(judgeDrawTypeBox, "wrap");
        add(new JScrollPane(judgeDrawTypeDescription), "span");
        
        add(locationDrawTypeLabel);
        add(locationDrawTypeBox, "wrap");
        add(new JScrollPane(locationDrawTypeDescription), "span");
        
        add(cancel, "tag cancel");
        add(allocate, "tag apply");
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(OverviewFrame.getInstance());
        
        /*
         * Set up listeners
         */
        motionField.addKeyListener(new KeyListener() {
            private void checkMotionField() {
                if (motionField.getText().equals("")) {
                    allocate.setEnabled(false);
                } else {
                    allocate.setEnabled(true);
                }
            }
            
            @Override
            public void keyTyped(KeyEvent e) {
                checkMotionField();
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                checkMotionField();
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                checkMotionField();
            }
        });
        cancel.addActionListener(listener);
        allocate.addActionListener(listener);
        
        teamDrawTypeBox.addItemListener(listener);
        judgeDrawTypeBox.addItemListener(listener);
        locationDrawTypeBox.addItemListener(listener);
        
        updateDescriptions();
    }
    
    /**
     * Force the frame to update the text areas that contain the descriptions
     * of the algorithms selected
     */
    public void updateDescriptions() {
        //FIXME
        
//          teamDrawTypeDescription.setText(
//          
//          judgeDrawTypeDescription.setText(repos.getJudgeDrawTypeMap().get(
//          judgeDrawTypeBox.getSelectedItem()));
//          locationDrawTypeDescription.setText
//          (repos.getLocationDrawTypeMap().get(
//          locationDrawTypeBox.getSelectedItem()));
         
    }

    public String getMotion() {
        return motionField.getText();
    }
}
