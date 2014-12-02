package com.sonification.swing.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
		this.setSize((int)screenSize.getWidth()/2, (int)screenSize.getHeight()/2);
	//	this.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
		
		JButton playButton = new JButton("Play");
		
		// Display message if no file is found
				try {
					Graph viewPanel = new Graph("/Users/Student/Downloads/download.csv");
					mainPanel.add(viewPanel);
					
				} catch(Exception e) {
					JOptionPane.showMessageDialog(this, "File not Found");
					DefaultGraph blank = new DefaultGraph();
					mainPanel.add(blank);
				}
				
				mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
				mainPanel.add(playButton);

		mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(mainScroll);
		this.setTitle("Data Sonification");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
}

class DefaultGraph extends JPanel {

	private static final int SIZE_WIDTH = 800;
	private static final int SIZE_HEIGHT = 400;
	private static final int GRID_SIZE = 20;
	private GeneralPath path = new GeneralPath();
	private int position = 0;
	
	public DefaultGraph() {
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(SIZE_WIDTH + GRID_SIZE, SIZE_HEIGHT + GRID_SIZE * 2);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		int w = this.getWidth();
		int h = this.getHeight();


		/*
		 * Drawing current position
		 */
		path.reset();
		path.moveTo(position * GRID_SIZE + GRID_SIZE, 0);
		path.lineTo(position * GRID_SIZE + GRID_SIZE, SIZE_HEIGHT * 2); // make sure it covers the entire area
		g2d.setStroke(new BasicStroke(15));
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.draw(path);
		
		/*
		 * Drawing grid
		 */
		path.reset();
		for (double t = GRID_SIZE; t < w * 2; t = t + GRID_SIZE *2) {
			path.moveTo(t, 0);
			path.lineTo(t, h);
		}
		for (double t = GRID_SIZE; t < w * 2; t = t + GRID_SIZE *2) {
			path.moveTo(w, t);
			path.lineTo(0, t);
		}
		g2d.setColor(Color.gray);
		g2d.setStroke(new BasicStroke(1));
		g2d.draw(path);
	}
}


