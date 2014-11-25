package com.sonification.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sonification.data.Data;
import com.sonification.data.DataConverter;
 
 
public class Graph extends JPanel {

	private static final int SIZE_WIDTH = 800;
	private static final int SIZE_HEIGHT = 400;
	private static final int GRID_SIZE = 20;
	private GeneralPath path = new GeneralPath();
	private GeneralPath[] curves;
	private int curveDrawn = 0;
	private Double[] smallest;
	private Double[] largest;
	private Double multiplyVar = 1.0;
	private Color[] colors;
	private int position = 0;
	
	public Graph(String filepath){
		this(new File(filepath));
	}
	
	public Graph(File file){
		this(new DataConverter(file).readCSVFile());
		
	}
	
	public Graph(Data data){
		Random rnd = new Random((long) Math.random()); // for colors of path
		
		smallest = new Double[data.getData()[0].length];
		largest = new Double[data.getData()[0].length];
		for (int i = 0; i < data.getData()[0].length; i++){
			for (String s : data.get(i)){
				if (isDouble(s)){
					double v = Double.parseDouble(s);
					if (smallest[i] == null) { smallest[i] = v; largest[i] = v; }
					if (v < smallest[i]) { smallest[i] = v; }
					if (v > largest[i]) { largest[i] = v; }
				}
			}
		}
		curves = new GeneralPath[data.getData()[0].length];
		colors = new Color[data.getData()[0].length];
		for (int i = 0; i < curves.length; i++){
			// Avoid columns with just String data, TODO check for other points besides just (i)[0]
			while (!isDouble(data.get(i)[0])) {
				i++;
			}
			GeneralPath p = new GeneralPath();
			multiplyVar = SIZE_HEIGHT / (largest[i] - smallest[i]);
			System.out.println(largest[i] + " - " + smallest[i] + " => " + (largest[i] - smallest[i]));
			System.out.println(multiplyVar);
			// Avoid rows with just String data
			int j = 0;
			while (!isDouble(data.get(i)[j])){ j++; }
			p.moveTo(0 + GRID_SIZE, SIZE_HEIGHT - (multiplyVar * (Double.parseDouble(data.get(i)[j]) - smallest[i])) + GRID_SIZE);
			System.out.println(p.getCurrentPoint());
			while (j < data.get(i).length) {
				if (isDouble(data.get(i)[j])){
					double v = SIZE_HEIGHT - (multiplyVar * (Double.parseDouble(data.get(i)[j]) - smallest[i]));
					p.lineTo(j*(SIZE_WIDTH / data.get(i).length) + GRID_SIZE, v + GRID_SIZE);
				}
				j++;
			}
			curves[i] = p;
			colors[i] = new Color(rnd.nextFloat(),rnd.nextFloat(),rnd.nextFloat());
			
		}
	}
	
	/**
	 * set the position of the marker
	 * @param pos - int value measuring position
	 */
	public void setPosition(int pos){
		this.position = pos;
	}
	
	/**
	 * get the position of the marker
	 * @return pos - int value measuring position
	 */
	public int getPosition(){
		return this.position;
	}
	
	/**
	 * set the curve being drawn (if 0, all curves are drawn)
	 * @param curve
	 */
	public void setCurveDrawn(int curve){
		this.curveDrawn = curve;
		if ((this.curveDrawn >= curves.length) || (this.curveDrawn < 0)){
			this.curveDrawn = 0;
		}
		if (curve == -1){
			this.curveDrawn = curves.length - 1;
		}
	}
	
	/**
	 * get the curve being drawn (if 0, all curves are drawn)
	 * @return curveDrawn
	 */
	public int getCurveDrawn(){
		return this.curveDrawn;
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
		
		/*
		 * Drawing curves
		 */
		g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (int i = curveDrawn; i < (curveDrawn == 0 ? curves.length : curveDrawn + 1); i++){
			if (curves[i] != null){
				// TODO make sure colors aren't similar / hurt
				g2d.setColor(colors[i]);
				g2d.draw(curves[i]);
			}
		}
	}
	
	public static boolean isDouble(String str){
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Graph g = new Graph("/Users/Student/Downloads/download.csv");
		f.add(g);
		f.pack();
		f.setVisible(true);
		f.addKeyListener(new KeyListener(){

			private Graph g;
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// 39 is right, 37 is left
				if (arg0.getKeyCode() == 39){
					g.setPosition(g.getPosition() + 1);
					g.repaint();
				}

				if (arg0.getKeyCode() == 37){
					g.setPosition(g.getPosition() - 1);
					g.repaint();
				}
				
				// 38 is up, 40 is down
				if (arg0.getKeyCode() == 38){
					g.setCurveDrawn(g.getCurveDrawn() + 1);
					g.repaint();
				}

				if (arg0.getKeyCode() == 40){
					g.setCurveDrawn(g.getCurveDrawn() - 1);
					g.repaint();
				}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
			
			public KeyListener setGraph(Graph g){
				this.g = g;
				return this;
			}
			
		}.setGraph(g));
	}
}