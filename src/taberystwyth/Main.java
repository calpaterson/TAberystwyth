package taberystwyth;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		try {
			Class.forName("taberystwyth.db.SQLConnection");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Class.forName("taberystwyth.view.OverviewFrame"); //
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}