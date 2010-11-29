package taberystwyth.view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TeamInsertionFrame extends JFrame {
	
	TeamInsertionListener listener = new TeamInsertionListener(this);
	
	JTextField teamName = new JTextField();
	
	JTextField speaker1 = new JTextField();
	JTextField speaker2 = new JTextField();
	
	JCheckBox esl1 = new JCheckBox();
	JCheckBox esl2 = new JCheckBox();
	
	JCheckBox novice1 = new JCheckBox();
	JCheckBox novice2 = new JCheckBox();
	
	JTextField inst1 = new JTextField();
	JTextField inst2 = new JTextField();
	
	JButton clear = new JButton("Clear");
	JButton save = new JButton("Save");
	
	JLabel teamNameLabel = new JLabel("Team Name:");
	JLabel speaker1Label = new JLabel("Speaker Name 1:");
	JLabel speaker2Label = new JLabel("Speaker Name 2:");
	JLabel esl1Label = new JLabel("Esl?:");
	JLabel novice1Label = new JLabel("Novice?:");
	JLabel esl2Label = new JLabel("Esl?:");
	JLabel novice2Label = new JLabel("Novice?:");
	JLabel institution1Label = new JLabel("Institution 1:");
	JLabel institution2Label = new JLabel("Institution 2:");
	
	
	
	public TeamInsertionFrame(){
		setVisible(true);
		setLayout(new GridLayout(11,2));
		add(teamNameLabel);
		add(teamName);
		add(new JPanel());
		add(new JPanel());
		add(speaker1Label);
		add(speaker1);
		add(esl1Label);
		add(esl1);
		add(novice1Label);
		add(novice1);
		add(new JPanel());
		add(new JPanel());
		add(speaker2Label);
		add(speaker2);
		add(esl2Label);
		add(esl2);
		add(novice2Label);
		add(novice2);
		add(new JPanel());
		add(new JPanel());
		add(clear);
		add(save);
		pack();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clear.addActionListener(listener);
		save.addActionListener(listener);
		
	}

	public void clear() {
		System.out.println("Clear");
		
	}

	public void save() {
		System.out.println("Save");
		
	}

}
