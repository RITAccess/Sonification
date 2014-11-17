package com.sonification.swing.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class MainWindow extends JFrame {
	
	MenuAndToolBar menuBar = new MenuAndToolBar();
	GraphPanel graph = new GraphPanel();
	
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
		
		GraphPanel viewPanel = new GraphPanel();
		mainPanel.add(viewPanel);
		mainPanel.add(playButton);
		
		mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(mainScroll);
		this.setTitle("Data Sonification");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
}


