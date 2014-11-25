package com.sonification.swing.ui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.sonification.graph.Graph;

public class MainWindow extends JFrame {
	
	MenuAndToolBar menuBar = new MenuAndToolBar();
	
	public MainWindow() {
		
		this.setName("Sonification");
		BuildMainWindow();
		this.setJMenuBar(menuBar.getMenuBar());
		this.setVisible(true);
		
	}
	
	/**
	 * TO-DO: Work on layout
	 * 		  Add button functionality and hotkeys
	 */
	private void BuildMainWindow() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JPanel mainPanel = new JPanel();
		JScrollPane mainScroll = new JScrollPane(mainPanel);
		this.setSize((int)screenSize.getHeight(), (int)screenSize.getWidth()/2);
		
		JButton playButton = new JButton("Play");
		
		Graph viewPanel = new Graph("/Users/Student/Downloads/download.csv");
		mainPanel.add(viewPanel);
		mainPanel.add(playButton);
		
		mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(mainScroll);
		this.setTitle("Data Sonification");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
}


