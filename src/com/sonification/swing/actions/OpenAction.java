package com.sonification.swing.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javafx.application.Platform;
import javafx.stage.FileChooser;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.sonification.graph.Graph;
/**
 * Respond to requests to open a file
 * @author Melissa Young
 *
 */
public class OpenAction {
	
	private FileChooser.ExtensionFilter extFilterAll;
	private FileChooser.ExtensionFilter extFilterCSV;

	/**
	 * Constructor
	 */
	public OpenAction(String text, Icon actionIcon){
		
		extFilterAll = new FileChooser.ExtensionFilter("All Files", "*.*");
		extFilterCSV = new FileChooser.ExtensionFilter("CSV Files (*.csv)","*,csv","*.CSV");
	}
	
	public void actonPerformed(ActionEvent arg0) {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				FileChooser chooser = new FileChooser();
				chooser.getExtensionFilters().addAll(extFilterAll,extFilterCSV);
				if (chooser.getInitialDirectory()== null) {
					//chooser.setInitialDirectory();
				}
				
				chooser.setTitle("Open");
				File file = chooser.showOpenDialog(null);
				if (file != null) {
					
				}
			}
		});
	}
	
// Will return the file chosen so that it can be given to the graph component and applied. 
	public String openFile() {
		
		String chosenFile = "";
		return chosenFile;
		
	}
}