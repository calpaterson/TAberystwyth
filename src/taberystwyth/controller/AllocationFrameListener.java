package taberystwyth.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import taberystwyth.prelim.Allocator;
import taberystwyth.view.AllocationFrame;

public class AllocationFrameListener implements ActionListener {

	public void actionPerformed(ActionEvent event) {
		
		if (event.getActionCommand().equals("Cancel")){
			AllocationFrame.getInstance().setVisible(false);
		}
		
		else if(event.getActionCommand().equals("Allocate")){
			Allocator.getInstance().allocate();
		}
		
		else{
			System.out.println("Not Implemented");
		}

	}

}
