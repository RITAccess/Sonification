package com.sonification.swing.ui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sun.jna.Platform;

/**
 * Execute Sonification Application
 * @author Melissa Young
 */
public class SonificationLauncher {

	public static void main(String[] args) {
		
		try {
			if (Platform.isMac()) {
				System.getProperty("apple.laf.useScreenMenuBar", "true");
				System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Sonification");
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException ex) {ex.printStackTrace();
		} catch (InstantiationException ex) { ex.printStackTrace();
		} catch (IllegalAccessException ex) { ex.printStackTrace(); 
		} catch (UnsupportedLookAndFeelException ex) {ex.printStackTrace(); 
		}
		
		MainWindow mainWindow = new MainWindow();
		/**
		 * To be added: Icon images 
		 */
		mainWindow.setTitle("Sonification");
		
	}
	
}