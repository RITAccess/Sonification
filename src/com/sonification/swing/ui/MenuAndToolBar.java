package com.sonification.swing.ui;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.sun.jna.Platform;


public class MenuAndToolBar {
	
	static String modifierStr = Platform.getOSType() == Platform.MAC ? "command" : "control";
	static Integer modifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	static int toolbarSize = 4;
	static HashMap<Integer, ArrayList<ActionSet>> toolbarMap = new HashMap<Integer, ArrayList<ActionSet>>();
	/** 
	 * MenuEnum - Contain all the choices found in the top menus 
	 * @author Melissa Young
	 *
	 */
	enum MenuEnum {
		FILE("File"),
		EDIT("Edit"),
		HELP("Help"),
		;
	
		JMenu menu;
	
		MenuEnum(String name){
			this.menu = new JMenu(name);
		}
	}
	
	/**
	 * Enum created to hold all actions, tip text, event keys, etc. for the various actions in sonification
	 * TO-DO: Add actions when they have been created
	 * @author Melissa Young
	 */
	enum ActionSet{
		
	}
	
	/**
	 * Set up the menu bar that will appear at the top of the application
	 * @return JMenuBar
	 */
	
	public JMenuBar getMenuBar() {
		
		JMenuBar mainBar = new JMenuBar();
		mainBar.requestFocusInWindow();
		for (MenuEnum menu : MenuEnum.values()) {
			mainBar.add(menu.menu);
		}
		return mainBar;
	}
}