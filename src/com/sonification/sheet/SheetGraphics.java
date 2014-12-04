package com.sonification.sheet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SheetGraphics extends JPanel {

	private static final int GRID_SIZE = 20;
	private static final int SIZE_WIDTH = 800;
	private static final int SIZE_HEIGHT = 400;
	
	private GeneralPath path = new GeneralPath();
	
	public SheetGraphics(){
		this.setBackground(Color.white);
		this.setSize(SIZE_WIDTH, SIZE_HEIGHT);
		this.addMouseListener(new MouseListener(){
			SheetGraphics sg = null;
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				System.out.println(arg0.getX() + ", " + arg0.getY());
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public MouseListener setSheetGraphics(SheetGraphics sg){
				this.sg = sg;
				return this;
			}
		}.setSheetGraphics(this));
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
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(SIZE_WIDTH + GRID_SIZE, SIZE_HEIGHT + GRID_SIZE * 2);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SheetGraphics g = new SheetGraphics();
		//Graph g = new Graph();
		f.add(g);
		f.pack();
		f.setVisible(true);
	}
}
