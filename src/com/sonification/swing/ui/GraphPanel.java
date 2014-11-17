package com.sonification.swing.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * 
 * Panel that holds the graphs 
 * @author Melissa Young
 *
 */

public class GraphPanel extends JPanel {
	/**
	 * Constructor for the panel that will dispaly the graph
	 * TO-DO: Find out how to add the image to the panel
	 */
	public GraphPanel() {
		super(new GridLayout(0,1));
		this.setSize(16, 16);
		ImageIcon graph = new ImageIcon(createImage(this));
		
	}
	
	/** 
	 * Set up the graph's image to be placed in the panel
	 * TO-DO: Use the data from the CSV file to fill the image
	 * @param panel
	 * @return BufferedImage 
	 */
	private BufferedImage createImage(JPanel panel) {
		Dimension size = panel.getSize();
		BufferedImage newGraph = new BufferedImage(size.width, size.height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = newGraph.createGraphics();
		panel.paint(g2D);
		return newGraph;
	}
	
	/**
	 * Will open and display CSV file
	 * @param file
	 */
	private void openFile(File file) {
		
	
	}
}
///**
// * Create a JComponent that will be used to display the graphs opened
// * @author Melissa
// *
// * TO-DO: Add in a way to view the files in the component
// */
//class GraphView extends JComponent {
//	
//	public GraphView (int x, int y) {
//		super();
//		this.setLocation(x, y);
//		setUpViewPane();
//		repaint();
//	}
//
//	
//	public void paintComponent(Graphics g) {
//		
//		super.paintComponent(g);
//		g.setColor(Color.blue);
//		g.fillRect(0, 0, 200, 200);
//	}
//	
//	public void setUpViewPane() {
//		
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		
//		JPanel viewPanel = new JPanel();
//		viewPanel.setSize((int)screenSize.getWidth(),(int)screenSize.getHeight()/2);
//		
//	}
//}