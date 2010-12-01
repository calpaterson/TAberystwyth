package taberystwyth;

import javax.swing.*;

import taberystwyth.db.SQLConnection;
import taberystwyth.view.OverviewFrame;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			e.printStackTrace();
		}
		new OverviewFrame();
		//new TeamInsertionFrame();

	}

}