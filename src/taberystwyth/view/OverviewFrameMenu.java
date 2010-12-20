/* This file is part of TAberystwyth, a debating competition organiser
 * Copyright (C) 2010, Roberto Sarrionandia and Cal Paterson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package taberystwyth.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import taberystwyth.controller.OverviewFrameMenuListener;

public class OverviewFrameMenu extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	JMenu fileMenu = new JMenu("File");
	JMenu roundsMenu = new JMenu("Rounds");
	JMenu insertMenu = new JMenu("Insert");
	JMenu helpMenu = new JMenu("Help");
	
	JMenuItem newTab = new JMenuItem("New Tab");
	JMenuItem openTab = new JMenuItem("Open Tab");
	JMenuItem quit = new JMenuItem("Quit");
	
	JMenuItem speakers = new JMenuItem("Teams");
	JMenuItem judges = new JMenuItem("Judges");
	JMenuItem locations = new JMenuItem("Locations");
	
	JMenuItem about = new JMenuItem("About");
	
	JMenuItem drawRound = new JMenuItem("Draw Preliminary Round");
	JMenuItem viewRounds = new JMenuItem("View Rounds");
	
	public OverviewFrameMenu(OverviewFrameMenuListener menuListener){
		fileMenu.add(newTab);
		fileMenu.add(openTab);
		fileMenu.add(quit);
		
		insertMenu.add(speakers);
		insertMenu.add(judges);
		insertMenu.add(locations);
		
		roundsMenu.add(drawRound);
		roundsMenu.add(viewRounds);
		
		helpMenu.add(about);
		
		add(fileMenu);
		add(insertMenu);
		add(roundsMenu);
		add(helpMenu);
		
		newTab.addActionListener(menuListener);
		openTab.addActionListener(menuListener);
		quit.addActionListener(menuListener);
		speakers.addActionListener(menuListener);
		judges.addActionListener(menuListener);
		locations.addActionListener(menuListener);
		drawRound.addActionListener(menuListener);
		viewRounds.addActionListener(menuListener);
		about.addActionListener(menuListener);
	}
	
}
