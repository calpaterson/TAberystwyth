package taberystwyth;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Class.forName("taberystwyth.db.SQLConnection");
			Class.forName("taberystwyth.prelim.Allocator");
			Class.forName("taberystwyth.view.OverviewFrame");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}