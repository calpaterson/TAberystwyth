package taberystwyth.view;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import taberystwyth.controller.JudgeInsertionFrameListener;

public class JudgeInsertionFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * Labels
	 */
	private JLabel judgeNameLabel = new JLabel("Name:");
	private JLabel ratingLabel = new JLabel("Rating:");
	
	/*
	 * Textfields
	 */
	private JTextField judgeName = new JTextField();
	private JTextField rating = new JTextField();
	
	/*
	 * Buttons
	 */
	private JButton clear = new JButton("Clear");
	private JButton save = new JButton("Save");
	
	/*
	 * Singleton boilerplate
	 */
	private static JudgeInsertionFrame instance = new JudgeInsertionFrame();
	
	public static JudgeInsertionFrame getInstance(){
		return instance;
	}
	
	private JudgeInsertionFrame(){
		setLayout(new GridLayout(3, 2));
		setTitle("Insert Judges");
		
		/*
		 * When the window is closed, make it invisible
		 */
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				LocationInsertionFrame.getInstance().setVisible(false);
			}
		});
		
		add(judgeNameLabel);
		add(judgeName);
		
		add(ratingLabel);
		add(rating);
		
		add(clear);
		add(save);
		
		pack();
		
		clear.addActionListener(new JudgeInsertionFrameListener());
		save.addActionListener(new JudgeInsertionFrameListener());
		
	}

	public synchronized JTextField getJudgeName() {
		return judgeName;
	}

	public synchronized JTextField getRating() {
		return rating;
	}

	public synchronized JButton getClear() {
		return clear;
	}

	public synchronized JButton getSave() {
		return save;
	}

}
